package it.unibo.pulvreakt.dsl.errors

sealed interface DslError {
    data class SystemError(val error: SystemDslError) : DslError
    data class DeploymentError(val error: DeploymentDslError) : DslError
}
