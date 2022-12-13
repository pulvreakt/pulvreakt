---
sidebar_position: 1
---

# Create Pulverized Components

In this tutorial, we are going to create a simple pulverized application consisting of a device made up of the following
components:

- **Communication**
- **Behaviour**
- **Sensors**
- **State**

The objective of the device is to read the _GPS_ position through the `Sensors` component, elaborate the distance
between all neighbours through the `Behaviour`component and send his position via the `Communication` component to all
the neighbours.

In this scenario, we assume a "fully-connected topology" so that all the devices are connected.

## Behaviour

In this scenario, the `Behaviour` take the _GPS_ position given by the **sensors** component, take the positions of all
his neighbours and calculate the distance between all of them and save in the **state** the nearest device and his
distance.

```kotlin
class BehaviourComp : Behaviour<StateOps, NeighboursMessage, DeviceSensors, NoVal, Unit> {
  override val context: Context by inject()

  companion object {
    private const val R = 6371
    private const val ANGLE = 180.0
  }

  override fun invoke(
    state: StateOps,
    export: List<NeighboursMessage>,
    sensedValues: DeviceSensors,
  ): BehaviourOutput<StateOps, NeighboursMessage, NoVal, Unit> {
    val (myLat, myLong) = sensedValues.gps
    val distances = export.map { (device, location) ->
      val dLat = (location.lat - myLat) * PI / ANGLE
      val dLon = (location.long - myLong) * PI / ANGLE
      val myLatRand = myLat * PI / ANGLE
      val otherLatRand = location.lat * PI / ANGLE

      val a = sin(dLat / 2) * sin(dLat / 2) +
        sin(dLon / 2) * sin(dLon / 2) * cos(myLatRand) * cos(otherLatRand)
      val c = 2 * atan2(sqrt(a), sqrt(1 - a))
      device to R * c
    }
    val min = distances.minByOrNull { it.second }
    return BehaviourOutput(
      Distances(distances, min),
      NeighboursMessage(context.deviceID, sensedValues.gps),
      NoVal,
      Unit,
    )
  }
}
```

The creation of the behaviour is made via the implementation of the `Behavior` interface which requires the definitions
of several type variables. The first one defines the shape of the state, in particular, the type should implement the
interface `StateRepresentation`. The second generic represents the communication payload and the given type should
implement the `CommunicationPayload` interface. Moving on, the third generic specifies how all the sensor values are
collected; in this type should be available all the sensed values. The last two type-variable represent respectively the
prescriptive actions and the function outcome. In this tutorial since we do not use any of these two parameters, we
leave them empty.

The behaviour logic is implemented in the `invoke()` method. This method should return a `BehaviourOutput` which consist
of: a new state, the communication for the neighbours, prescriptive actions, and an outcome.

:::info
Each `PulverizedComponent` holds a `Context` that contains some information about the specific environment in which the
component is executed. At the time of writing the `Context` only holds the `deviceID`.
:::

:::caution
Since the Kotlin serialization doesn't allow the serialization of the type `Nothing`, a special object is available to
overcome this issue. The object is `NoVal` which represents the absence of value. Similarly, `NoState` and `NoComm` are
object which represents the absence of a state and the absence of communication, respectively.
:::

## State

The `State` in this scenario holds information about the distances from all the neighbours and the nearest neighbour
with his distance.

To define a new state we should, first of all, define how the state is composed. To do that is useful to define the
representation of the state through a `data class` which implements the `StateRepresentation` interface.

```kotlin
@Serializable
sealed interface StateOps : StateRepresentation

@Serializable
data class Distances(
  val distances: List<Pair<String, Double>>,
  val nearest: Pair<String, Double>?
) : StateOps

@Serializable
data class Query(val query: String) : StateOps
```

:::tip
The representation above as an ADT (Algebraic Data Type) enable to define a custom behaviour based on the received
message. For example in this case `Dinstance` represents the effective state, but with `Query` we can "query" the state.
:::

Once the state representation is defined we need to create the state component.

```kotlin
class StateComp : State<StateOps> {
  override val context: Context by inject()

  private var state = Distances(emptyList(), null)

  override fun get(): StateOps = state

  override fun update(newState: StateOps): StateOps {
    val tmp = state
    when (newState) {
      is Distances -> state = newState
      is Query -> println("Query received: ${newState.query}")
    }
    return tmp
  }
}
```

Only two methods are to be implemented `get()` and `update()` which respectively return the current state and update the
state with a new one.

## Communication

The `Communication` component is one of the most important components in a pulverized system: it manages all the
communications with the neighbours' devices.

```kotlin
class CommunicationComp : Communication<NeighboursMessage> {
  override val context: Context by inject()

  private lateinit var sender: Sender
  private lateinit var receiver: Receiver

  private val exchange = "amq.fanout"
  private lateinit var queue: String

  override suspend fun initialize() {
    // Initialization
  }

  override suspend fun send(payload: NeighboursMessage) {
    val message = OutboundMessage(exchange, "", Json.encodeToString(payload).toByteArray())
    sender.send(Mono.just(message)).awaitSingleOrNull()
  }

  override fun receive(): Flow<NeighboursMessage> =
    receiver.consumeAutoAck(queue)
      .asFlow()
      .map { Json.decodeFromString(it.body.decodeToString()) }

  override suspend fun finalize() {
    sender.close()
    receiver.close()
  }
}
```

The `CommunicationComp` implements the `Communication` interface that requires a type variable representing the type
of the message, the component should send and receive from the neighbours.

The `Communication` interface define two methods `send()` and `receive()` which represents the operation of send a
message to the neighbours and the operation of receiving messages from the neighbours, respectively.

:::caution
Is responsibility of the `Communication` component hold information about the topology of the network and how to reach
the neighbours.
:::

In this example, we assume a "fully-connected topology" and, since we use the `rabbitmq-platform` package, we rely on _
RabbitMQ_ to reach all the neighbours using the pre-defined _exchange_ `amq.fanout` that sends the messages to all the
bind queues. In this way, we obtain a fully-connected network topology.

## Sensors

In this demo we use only one sensor, but of course, the framework give you the ability to define how many sensors you
need. First of all, we start to define how the sensor's information are represented.

```kotlin
@Serializable
data class Gps(val long: Double, val lat: Double)

@Serializable
data class DeviceSensors(val gps: Gps)
```

:::tip
The class representing the sensors' values **MUST** be annotated with the `@Serializable` annotation since that class
should be reached by the `Behaviour` component.
:::

Next, we define a sensor. The implementation requires implementing the `Sensor` interface that requires information
about the values read by the sensor.

```kotlin
class GpsSensor : Sensor<Gps> {
  override fun sense(): Gps = Gps(Random.nextDouble(-180.0, 180.0), Random.nextDouble(-90.0, 90.0))
}
```

In this demo, we simulate a _GPS_ sensor. The only method to implement is `sense()` which return the sensed value from
the sensor.

In the end is necessarily to create the `SensorsContainer` a class which hold all the sensors of a device.

```kotlin
class LocalizationSensor : SensorsContainer() {
  override val context: Context by inject()

  override suspend fun initialize() {
    this += GpsSensor()
  }
}
```

Now, we completed the step of writing all the pulverized components.
