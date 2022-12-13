---
sidebar_position: 2
---

# Components Logic

Once all the _pulverized components_ are defined, we need to specify the logic of a single component.
For **logic** we intend how the component should behave from his perspective.

For example, we want to get the _GPS_ position every 2 seconds and send the value to the behavior component.
In this case, the logic could be the following:

```kotlin
suspend fun mySensorsLogic(sensors: SensorsContainer, behaviour: BehaviourRef<DeviceSensors>) {
  sensors.get<GpsSensor> {
    repeat(10) {
      val payload = DeviceSensors(sense())
      behaviour.sendToComponent(payload)
      delay(2.seconds)
    }
  }
}
```

A more complex logic can be found in the behaviour:

```kotlin
suspend fun gpsBehaviourLogic(
  behaviour: Behaviour<StateOps, NeighboursMessage, DeviceSensors, NoVal, Unit>,
  state: StateRef<StateOps>,
  comm: CommunicationRef<NeighboursMessage>,
  sensors: SensorsRef<DeviceSensors>,
  actuators: ActuatorsRef<NoVal>,
) = coroutineScope {
  var neighboursComm = listOf<NeighboursMessage>()
  val jobComm = launch {
    comm.receiveFromComponent().collect {
      neighboursComm = neighboursComm.filter { e -> e.device != it.device } + it
    }
  }
  val jobSensor = launch {
    sensors.receiveFromComponent().collect {
      state.sendToComponent(Query("query selector")) // This is an example of query: do nothing.
      when (val lastState = state.receiveFromComponent().first()) {
        is Distances -> {
          println("${behaviour.context.deviceID}: ${lastState.distances}")
          lastState.nearest?.let {
            println("Device '${lastState.nearest.first}' is the nearest (${lastState.nearest.second})")
          } ?: println("No information about the nearest")
          val (newState, newComm, _, _) =
            behaviour(lastState, neighboursComm.filter { e -> e.device != behaviour.context.deviceID }, it)
          state.sendToComponent(newState)
          comm.sendToComponent(newComm)
        }

        is Query -> error("The State should not respond with a query")
      }
    }
  }
  jobComm.join()
  jobSensor.join()
}
```

In oder to define a component logic, a specific method signature must be followed.

- **State logic** $$\rightarrow$$ `suspend (state: State<StatePayload>, behaviour: BehaviourRef<StatePayload>) -> Unit`
- **Comm logic** $$\rightarrow$$  
  `suspend (comm: Communication<CommPayload>, behaviour: BehaviourRef<CommPayload>) -> Unit`
- **Sensors logic** $$\rightarrow$$ `suspend (comm: SensorsContainer, behaviour: BehaviourRef<SensorsPayload>) -> Unit`
- **Actuators logic**
  $$\rightarrow$$ `suspend (comm: ActuatorsContainer, behaviour: BehaviourRef<ActuatorsPayload>) -> Unit`
- **Behaviour logic** $$\rightarrow$$
  ```
  suspend (
    behaviour: Behaviour<StatePayload, CommPayload, SensorsPayload, ActuatorsPayload, Outcome>,
    stateRef: StateRef<StatePayload>,
    commRef: CommunicationRef<CommPayload>,
    sensorsRef: SensorsRef<SensorsPayload>,
    actuatorsRef: ActuatorsRef<ActuatorsPayload>,
  ) -> Unit
  ```

## ComponentRef

The `ComponentRef` models the concept of reference to another component. The reference could be **local**, **remote**.
In the case where a component is not defined in the device, the reference do nothing.

This interface enable an intra-component communication abstracting over the place where the component is deployed.
The `ComponentRef` use the `Communicator` as a medium for the communication.  
The concept of `Communicator` is described in the following section.
