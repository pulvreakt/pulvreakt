package it.unibo.pulvreakt.modularization.runtime.errors

sealed interface BootstrapperError

data class BootstrapFailed(val reason: String) : BootstrapperError
