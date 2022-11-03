---
sidebar_position: 3
---

# Pulverization Configuration

The pulverization framework gives a DSL to configure and declare all the `LogicalDevice` in the system and how they are
composed.
Moreover, based on the platform imported in the project, the DSL is enriched with platform-specific parameters.
In this example we show how to build a configuration for the `rabbitmq-platform`.

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

The DSL give the entrypoint `pulverizedConfig` which gives you the ability to configure as many `LogicalDevice` types
you need in the system (in our example only one device is specified). Moreover, the DSL give you the possibility to
configure the parameters of the **RabbitMQ** MOM which is used by the pulverization framework to run the system.
The RabbitMQ parameters can be configured via the `rabbitmq` scope.

When configuring a `LogicalDevice`, you must give a "canonical name" to that specific configuration of device.
Inside the scope `logicalDevice` you can configure the specific components belonging to that logical device. 
