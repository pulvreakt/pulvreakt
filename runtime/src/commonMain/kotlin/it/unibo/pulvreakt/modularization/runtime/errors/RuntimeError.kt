package it.unibo.pulvreakt.modularization.runtime.errors

import it.unibo.pulvreakt.modularization.runtime.errors.network.NetworkError

sealed interface RuntimeError

data class RuntimeNetworkError(val error: NetworkError) : RuntimeError

data class RuntimeBootstrapError(val error: BootstrapperError) : RuntimeError
