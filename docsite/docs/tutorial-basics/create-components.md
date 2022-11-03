---
sidebar_position: 1
---

# Create Components

In this tutorial we are going to create a simple pulverized application consisting of a device made up of the following
components:

- **Behaviour**
- **Communication**
- **Sensors**

The objective of the device is read a value through the `Sensors` component, elaborate that value through
the `Behaviour`component and send the output via the `Communication` component to all the neighbours.

In this scenario we assume a "fully-connected topology" so that all the devices are connected to each other.

## Behaviour Component

In this scenario the `Behaviour` do nothing especial; it compute an identity function of the form:

```
(state, neighboursComm, sensedValues) -> (state, communication)
```

In this way, we take the `State`, all the messages coming from the neighbours and generate a new communication to
propagate to all the neighbours. The function returns the same state and the message generated.
The generated message contain the sensor value read from the `Sesnors` component.

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
of several type variables. The first one define the shape of the state, in particular the type should implement the
interface `StateRepresentation`. The second generic represents the communication payload and the given type should
implement the `CommunicationPayload` interface. Moving on, the third generic specifies how all the sensors values are
collected; in this type should be available all the sensed values. The last two type variable represent respectively the
prescriptive actions and the function outcome. In this tutorial since we do not use any of this two parameters we leave
them empty.

The behaviour logic in implemented in the `invoke()` method. This method should return a `BehaviourOutput` which consist
of: a new state, the communication for the neighbours, prescriptive actions and an outcome.

:::info
Each `PulverizedComponent` holds a `Context`. The context contains some information about the specific instance
of `LogicalDevice` that the component belongs to. Different platform enrich the context with platform-specific
information that could be exploited by the component or by the layers below.
:::

## State

The `State` in this scenario is used as a demonstration only: it does not provide any contribution to the demo.

To define a new state we should, first of all, define how the state is composed. To do that is useful to define the
representation of the state through a `data class` which implement the `StateRepresentation` interface.

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

The ``
