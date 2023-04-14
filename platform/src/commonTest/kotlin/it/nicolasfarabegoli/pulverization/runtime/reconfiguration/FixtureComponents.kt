package it.nicolasfarabegoli.pulverization.runtime.reconfiguration

import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestReconfigurator(private val flow: MutableSharedFlow<Pair<ComponentType, Host>>) : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: Pair<ComponentType, Host>) {
        flow.emit(newConfiguration)
    }
    override fun receiveReconfiguration(): Flow<Pair<ComponentType, Host>> = flow
}
