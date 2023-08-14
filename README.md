[![CI/CD](https://github.com/pulvreakt/pulvreakt/actions/workflows/dispatchers.yml/badge.svg?branch=master)](https://github.com/pulvreakt/pulvreakt/actions/workflows/dispatchers.yml)
![Maven Central](https://img.shields.io/maven-central/v/it.unibo.pulvreakt/core)
[![codecov](https://codecov.io/gh/pulvreakt/pulvreakt/branch/master/graph/badge.svg?token=L3NJH26WWJ)](https://codecov.io/gh/pulvreakt/pulvreakt)
![GitHub issues](https://img.shields.io/github/issues/pulvreakt/pulvreakt)
![GitHub pull requests](https://img.shields.io/github/issues-pr/pulvreakt/pulvreakt)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventional_commits-e10098?logo=semantic-release)](https://github.com/semantic-release/semantic-release)
![GitHub](https://img.shields.io/github/license/pulvreakt/pulvreakt)

# PulvReAKt

> A simple and lightweight Kotlin multiplatform framework to create pulverized systems

📘 Documentation available [here](https://pulvreakt.github.io/pulvreakt)

### Current implemented platforms

|               |       `core`       |     `runtime`      | `rabbitmq-communicator` |
|---------------|:------------------:|:------------------:|:-----------------------:|
| **JVM**       | :heavy_check_mark: | :heavy_check_mark: |   :heavy_check_mark:    |
| **JS**        | :heavy_check_mark: | :heavy_check_mark: |           :x:           |
| **Native***   | :heavy_check_mark: | :heavy_check_mark: |           :x:           |

\* The current native supported platforms are: `linux X64`, `mingw X64`, `macos X64`, `macos ARM`, `iOS`, `watchOS`, `tvOS`.  

> ⚠️ Due to a limitation in [`kotlinx-coroutine`](https://github.com/Kotlin/kotlinx.coroutines/issues/855) the `linux ARM64`, `linux Hfp 32` and `mingw X86` platforms are currently not supported.
