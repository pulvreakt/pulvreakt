---
sidebar_position: 3
---

# Pulverization Configuration

The pulverization framework gives a DSL to configure and declare all the **logical device** in the system and how they
are composed.

```kotlin
val configuration = pulverizationConfig {
  logicalDevice("gps") {
    // BehaviourComponent and StateComponent deployableOn Cloud
    BehaviourComponent deployableOn Cloud
    StateComponent deployableOn Cloud
    CommunicationComponent deployableOn Edge
    SensorsComponent deployableOn Device
  }
}
```

The DSL gives the entry point `pulverizationConfig` which gives you the ability to configure as many **logical device**
you need in the system (in our example, only one device is specified).

When configuring a `LogicalDevice`, you must give a "name" to that specific configuration of a device.
Inside the `logicalDevice` scope you can configure the specific components which will form a **deployment unit** and
where that deployment unit will be deployed.
