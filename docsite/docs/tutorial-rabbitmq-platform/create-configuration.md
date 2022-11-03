---
sidebar_position: 3
---

# Pulverization Configuration

The pulverization framework gives a DSL to configure and declare all the `LogicalDevice` in the system and how they are
composed.
Moreover, based on the platform imported into the project, the DSL is enriched with platform-specific parameters.
With this example, we show how to build a configuration for the `rabbitmq-platform`.

```kotlin
val configuration = pulverizationConfig {
  logicalDevice("device") {
    component(DeviceBehaviour())
    component(DeviceCommunication())
    component(DeviceSensorsContainer())
    component(DeviceState())
  }
  rabbitmq {
    setHostname(System.getenv("RABBITMQ_HOST") ?: "rabbitmq")
  }
}
```

The DSL gives the entry point `pulverizedConfig` which gives you the ability to configure as many `LogicalDevice` types
as you need in the system (in our example, only one device is specified). Moreover, the DSL gives you the possibility to
configure the parameters of the **RabbitMQ** MOM that is used by the pulverization framework to run the system.
The RabbitMQ parameters can be configured via the `rabbitmq` scope.

When configuring a `LogicalDevice`, you must give a "canonical name" to that specific configuration of a device.
Inside the scope `logicalDevice` you can configure the specific components belonging to that logical device. 
