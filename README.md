![CI/CD Pipeline](https://github.com/nicolasfara/pulverization-framework/actions/workflows/build-and-release.yml/badge.svg)
![Maven Central](https://img.shields.io/maven-central/v/it.nicolasfarabegoli.pulverization-framework/core)
[![codecov](https://codecov.io/gh/nicolasfara/pulverization-framework/branch/master/graph/badge.svg?token=L3NJH26WWJ)](https://codecov.io/gh/nicolasfara/pulverization-framework)
![GitHub issues](https://img.shields.io/github/issues/nicolasfara/pulverization-framework)
![GitHub pull requests](https://img.shields.io/github/issues-pr/nicolasfara/pulverization-framework)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventional_commits-e10098?logo=semantic-release)](https://github.com/semantic-release/semantic-release)
![GitHub](https://img.shields.io/github/license/nicolasfara/pulverization-framework)

# Pulverization Framework

> A simple and lightweight Kotlin mulitplatform framework to create pulverized systems

### Current implemented platforms

|               | `core`               | `platform`           | `rabbitmq-platform`   |
|---------------|:--------------------:|:--------------------:|:---------------------:|
| **JVM**       | :heavy_check_mark:   | :heavy_check_mark:   | :heavy_check_mark:    |
| **JS**        | :heavy_check_mark:   | :x:                  | :x:                   |
| **Native***   | :heavy_check_mark:   | :x:                  | :x:                   |

\* The current native supported platforms are: `linux X64`, `mingw X64`, `macos X64`, `macos ARM`, `iOS`, `watchOS`, `tvOS`.  

> ⚠️ Due to a limitation in [`kotlinx-coroutine`](https://github.com/Kotlin/kotlinx.coroutines/issues/855) the `linux ARM64`, `linux Hfp 32` and `mingw X86` platforms are currently not supported.
