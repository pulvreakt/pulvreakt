---
sidebar_position: 4
---

# Create Device Component

The `DeviceComponent` represents the deployable unit of a pulverized component. Generally, a device component define the
bindings between the "pure" components and the communicator needed to interact with the other device's component.
Moreover, the `DeviceComponent` specifies the lifecycle of the component defining three methods `initialize()`
, `cycle()` and `finalize()` which initialize the component, handle a single cycle of the component's logic and release
all the resources of the component, respectively.

## Behaviour Device Component

```kotlin
class DeviceBehaviourComponent : DeviceComponent<RabbitmqContext> {
  override val context: RabbitmqContext by inject()
  private val behaviour: DeviceBehaviour by inject()
  private val state: DeviceState by inject()

  private val sensorsCommunicator =
    SimpleRabbitmqReceiverCommunicator<AllSensorsPayload>(
      SensorsComponent to BehaviourComponent,
    )
  private val communication =
    SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(
      BehaviourComponent to CommunicationComponent,
    )

  private var lastNeighboursComm = emptyList<CommPayload>()

  override suspend fun initialize() {
    // initialize the device component
  }

  override suspend fun finalize() {
    // finalize the device component
  }

  override suspend fun cycle() {
    sensorsCommunicator.receiveFromComponent().collect { sensedValues ->
      println("Device ${context.id.show()} received from neighbours: $lastNeighboursComm")
      val (newState, newComm, _, _) = behaviour(state.get(), lastNeighboursComm, sensedValues)
      state.update(newState)
      communication.sendToComponent(newComm)
    }
  }
}
```

The `DeviceBehaviourComponent` is the most rich device component in this example.  
It hold the `Behaviour` and the `State` in the same deployable unit. Moreover, since the behaviour manages the
communications with the other pulverized components, we defined a communicator to interact with the sensors and a
communicator to interact with the communication component.

In the `cycle()` method is implemented the logic of the component. In this case we wait for a new sensor read and after
receiving the result we compute the behaviour function. Later, we update the state with the computed state and foreword
the message to the neighbours.

:::info
This device component is an example of _hybrid_ component because holds either the `Behaviour` and the `State`.
:::

## State Device Component

```kotlin
class DeviceSensorsComponent : DeviceComponent<RabbitmqContext> {
  override val context: RabbitmqContext by inject()

  private val sensorCommunicator = SimpleRabbitmqSenderCommunicator<AllSensorsPayload>(
    SensorsComponent to BehaviourComponent,
  )

  private val sensor: DeviceSensorsContainer by inject()

  init {
    sensor += DeviceSensor()
  }

  override suspend fun initialize() {
    sensorCommunicator.initialize()
  }

  override suspend fun cycle() {
    sensor.get<DeviceSensor> {
      val payload = AllSensorsPayload(this.sense())
      sensorCommunicator.sendToComponent(payload)
    }
  }
}
```

The `DeviceSensorsComponent` manages the sensors (with the help of the `SensorsContainer`) and the communication with
the behaviour.
The logic implemented in the `cycle()` method is quite simple: on each cycle we read the value from the sensor and send
it to the behaviour, that's it.

## Device Communication Component

```kotlin
class DeviceCommunicationComponent : DeviceComponent<RabbitmqContext> {
  override val context: RabbitmqContext by inject()

  private val deviceCommunication: DeviceCommunication by inject()

  private val componentCommunicator =
    SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(
      CommunicationComponent to BehaviourComponent,
    )

  private val deferredReferences: MutableSet<Deferred<Unit>> = mutableSetOf()

  override suspend fun initialize(): Unit = coroutineScope {
    deviceCommunication.initialize()
    componentCommunicator.initialize()
    val deferComp = async {
      componentCommunicator.receiveFromComponent().collect {
        deviceCommunication.send(it)
      }
    }
    val deferComm = async {
      deviceCommunication.receive().collect { componentCommunicator.sendToComponent(it) }
    }
    deferredReferences += setOf(deferComp, deferComm)
  }

  override suspend fun finalize() = deferredReferences.forEach { it.cancelAndJoin() }

  override suspend fun cycle() {}
}
```

Also the `DeviceCommunicationComponent` is quite simple: receives the messages from the neighbours thanks to
the `deviceCommunication` and forward that messages to the behaviour through the `componentCommunicator`.

In this class we exploit the power of the Kotlin coroutines to react asynchronously to the receiving of the messages.
So, all the logic for the messages' handling is implemented in the `initialize()` method, which suspend forever in this
case.
