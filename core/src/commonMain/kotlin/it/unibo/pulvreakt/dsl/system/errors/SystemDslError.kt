package it.unibo.pulvreakt.dsl.system.errors

sealed interface SystemDslError {
    data class DuplicateDeviceName(val deviceName: String) : SystemDslError
    object EmptyConfiguration : SystemDslError
}
