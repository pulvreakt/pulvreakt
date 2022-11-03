---
sidebar_position: 5
---

# Deployment

At this point, all the components are defined. The last step is to create an executable for each component and
containerize it to deploy the system.

To do that, we create a simple main function in kotlin with the following elements:

```kotlin
fun main() = runBlocking {
  val deviceID = System.getenv("DEVICE_ID")?.toID() ?: error("No `DEVICE_ID` variable found")
  pulverizationSetup(deviceID, configuration) {
    registerComponent<DeviceBehaviour>(configuration["device"])
    registerComponent<DeviceState>(configuration["device"])
  }

  val behaviour = DeviceBehaviourComponent()
  behaviour.initialize()
  behaviour.cycle()
}
```

First of all, we must specify the `DEVICE_ID`, in this case, the ID is taken by an environment variable.
Later, we use a DSL for setting up the pulverization framework specifying which components need to be "activated" for
that specific component.

After the setup, the only remaining thing to do is create the instance of our `DeviceComponent` in this case
the `DeviceBehaviourComponent`, initialize and run it.
