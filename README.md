[![CI/CD](https://github.com/pulvreakt/pulvreakt/actions/workflows/dispatchers.yml/badge.svg?branch=master)](https://github.com/pulvreakt/pulvreakt/actions/workflows/dispatchers.yml)
![Maven Central](https://img.shields.io/maven-central/v/it.unibo.pulvreakt/core)
[![codecov](https://codecov.io/gh/pulvreakt/pulvreakt/branch/master/graph/badge.svg?token=L3NJH26WWJ)](https://codecov.io/gh/pulvreakt/pulvreakt)
![GitHub issues](https://img.shields.io/github/issues/pulvreakt/pulvreakt)
![GitHub pull requests](https://img.shields.io/github/issues-pr/pulvreakt/pulvreakt)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventional_commits-e10098?logo=semantic-release)](https://github.com/semantic-release/semantic-release)
![GitHub](https://img.shields.io/github/license/pulvreakt/pulvreakt)

# PulvReAKt

> A simple and lightweight Kotlin multiplatform framework for system pulverization.

📘 Documentation available [here](https://pulvreakt.github.io/pulvreakt)

## Getting Started

```
implementation("it.unibo.pulvreakt:core:0.8.0")
implementation("it.unibo.pulvreakt:runtime:0.8.0")

// Specific protocols based on the communicator you want to use
implementation("it.unibo.pulvreakt:rabbitmq-communicator:0.8.0")
implementation("it.unibo.pulvreakt:mqtt-communicator:0.8.0")
```

## Usage

```kotlin
val embeddedDevice by Capability
val highCpu by Capability

val smartphone = Host("smartphone", embeddedDevice)
val server = Host("server", highCpu)
val infrastructure = nonEmptySetOf(smartphone, server)

val configResult = pulverization {
  val device by logicDevice {
    withBehaviour<DeviceBehaviour>() requires setOf(embeddedDevice, highCpu)
    withSensors<DeviceSensors>() requires embeddedDevice
    withCommunication<DeviceCommunication>() requires setOf(embeddedDevice, highCpu)
  }
  deployment(infrastructure, MqttProtocol()) {
    device(device) {
      DeviceBehaviour() startsOn server
      DeviceSensors() startsOn smartphone
      DeviceCommunication() startsOn smartphone
    }
  }
}

// On the server `main`

val runtimeResult = either {
  val config = configResult.bind()
  val runtime = PulvreaktRuntime(config, "device", 1, server).bind()
  runtime.start().bind()
}

when (runtimeResult) {
  is Either.Left -> println("Error: ${runtimeResult.value}")
  is Either.Right -> println("Success!")
}
```

### Current implemented platforms

|             |       `core`       |     `runtime`      | `rabbitmq-communicator` |
|-------------|:------------------:|:------------------:|:-----------------------:|
| **JVM**     | :heavy_check_mark: | :heavy_check_mark: |   :heavy_check_mark:    |
| **JS**      | :heavy_check_mark: | :heavy_check_mark: |           :x:           |
| **Native*** | :heavy_check_mark: | :heavy_check_mark: |           :x:           |

\* The current native supported platforms
are: `linux X64`, `mingw X64`, `macos X64`, `macos ARM`, `iOS`, `watchOS`, `tvOS`.

> ⚠️ Due to a limitation in [`kotlinx-coroutine`](https://github.com/Kotlin/kotlinx.coroutines/issues/855)
> the `linux ARM64`, `linux Hfp 32` and `mingw X86` platforms are currently not supported.
