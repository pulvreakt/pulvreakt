---
sidebar_position: 4
---

# Deployment

At this point, all the components and their corresponding logics are defined. The last step is to create an executable
for each **deployment unit** and containerize it to deploy the system.

To do that, we create a simple main function in kotlin with the following elements:

```kotlin
fun main() = runBlocking {
  val platform = pulverizationPlatform(config.getDeviceConfiguration("gps")!!) {
    behaviourLogic(BehaviourComp(), ::gpsBehaviourLogic)
    // stateLogic(StateComp(), ::stateLogic)
    withPlatform { RabbitmqCommunicator(hostname = "rabbitmq") }
    withRemotePlace { defaultRabbitMQRemotePlace() }
  }
  val jobs = platform.start()
  jobs.forEach { it.join() }
  platform.stop()
}
```

Now, we use a DSL for setting up the pulverization framework specifying which components need to be "activated" for
that specific deployment unit.
Moreover, the DSL enable the configuration of the `Communicator` which in this example is `RabbitmqCommunicator`.
The `withRemotePlace` scope we configure the provider of `RemotePlace`: an abstraction of how to reach the other
components.

The DSL returns a platform instance which can be started and stopped. The `start` method returns a set of `Job`
referencing all the components' logic executed. The `stop` method release all the resources allocated by the platform.
