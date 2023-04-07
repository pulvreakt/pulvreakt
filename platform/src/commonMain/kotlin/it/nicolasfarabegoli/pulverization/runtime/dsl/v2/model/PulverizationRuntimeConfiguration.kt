package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

data class PulverizationRuntimeConfiguration<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val componentsRuntimeConfiguration: ComponentsRuntimeContainer<S, C, SS, AS, O>,
    val reconfigurationRules: ReconfigurationRules?,
)
