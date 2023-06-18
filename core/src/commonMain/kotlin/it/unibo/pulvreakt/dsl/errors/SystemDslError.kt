package it.unibo.pulvreakt.dsl.errors

sealed interface SystemDslError {
    data class DuplicateDeviceName(val deviceName: String) : SystemDslError
    object EmptyConfiguration : SystemDslError
}
