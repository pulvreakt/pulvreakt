---
sidebar_position: 1
---

# Create Pulverized Components

In this tutorial, we are going to create a simple pulverized application consisting of a device made up of the following
components:

- **Behaviour**
- **Communication**
- **Sensors**

The objective of the device is to read a value through the `Sensors` component, elaborate that value through
the `Behaviour`component and send the output via the `Communication` component to all the neighbours.

In this scenario, we assume a "fully-connected topology" so that all the devices are connected.

## Behaviour

In this scenario, the `Behaviour` do nothing special; it compute an identity function of the form:

```
(state, neighboursComm, sensedValues) -> (state, communication)
```

In this way, we take the `State`, all the messages coming from the neighbours, and generate a new communication to
propagate to all the neighbours. The function returns the same state and the message generated.
The generated message contains the sensor value read from the `Sensors` component.

```kotlin
class DeviceBehaviour : Behaviour<StateRepr, CommPayload, AllSensorsPayload, Unit, Unit> {
  override val context: Context by inject()

  override fun invoke(
    state: StateRepr,
    export: List<CommPayload>,
    sensedValues: AllSensorsPayload,
  ): BehaviourOutput<StateRepr, CommPayload, Unit, Unit> {
    println("Neighbours info: $export")
    val payload = CommPayload(context.id.show(), sensedValues.deviceSensor)
    return BehaviourOutput(state, payload, Unit, Unit)
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
Each `PulverizedComponent` holds a `Context` that contains some information about the specific instance
of `LogicalDevice` that the component belongs to. Different platforms enrich the context with platform-specific
information that could be exploited by the component or by the layers below.
:::

## State

The `State` in this scenario is used as a demonstration only: it does not provide any contribution to the demo.

To define a new state we should, first of all, define how the state is composed. To do that is useful to define the
representation of the state through a `data class` which implements the `StateRepresentation` interface.

```kotlin
data class StateRepr(val counter: Int) : StateRepresentation
```

Once the state representation is defined we need to create the state component.

```kotlin
class DeviceState : State<StateRepr> {
  override val context: Context by inject()

  private var internalState: StateRepr = StateRepr(0)

  override fun get(): StateRepr = internalState

  override fun update(newState: StateRepr): StateRepr {
    val tmp = internalState
    internalState = newState
    return tmp
  }
}
```

The `DeviceState` implements the `State` interface with requires a `StateRepresentation` and we give the data class
already created.

Only two methods are to be implemented `get()` and `update()` which respectively return the current state and update the
state with a new one.

## Communication

The `Communication` component is one of the most important components in a pulverized system: it manages all the
communications with the neighbours' devices.

```kotlin
class DeviceCommunication : Communication<CommPayload> {
  override val context: RabbitmqContext by inject()

  private val connection: Connection by inject()

  suspend fun initialize() {
    // Initializations
  }

  override fun send(payload: CommPayload) {
    val message = OutboundMessage("amq.fanout", "", Json.encodeToString(payload).toByteArray())
    sender.send(Mono.just(message)).block()
  }

  override fun receive(): Flow<CommPayload> {
    return receiver.consumeAutoAck(queue).asFlow().map<Delivery, CommPayload> {
      Json.decodeFromString(it.body.decodeToString())
    }.filter { it.deviceID != context.id.show() }
  }
}
```

The `DeviceComunication` implements the `Communication` interface that requires a type variable representing the type
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

In this demo we use only one sensor, but of course, the framework give you the ability of define how many sensors you
need. First of all, we start to define how the sensor's information are represented.

```kotlin
typealias SensorPayload = Double

@Serializable
data class AllSensorsPayload(val deviceSensor: SensorPayload)
```

:::tip
The class representing the sensors' values **MUST** be annotated with the `@Serializable` annotation since that class
should be reached by the `Behaviour` component.
:::

Next, we define a sensor. The implementation requires implementing the `Sensor` interface that requires information
about the values read by the sensor.

```kotlin
class DeviceSensor : Sensor<SensorPayload> {
  override fun sense(): SensorPayload = Random.nextDouble(0.0, 100.0)
}
```

In this demo, we simulate a sensor with a random number between `0-100`. The only method to implement is `sense()` which
return the sensed value from the sensor.

Now, we completed the step of writing all the pulverized components. Next, we will show how to bind those components to
the "communicators" in order to make the system work.
