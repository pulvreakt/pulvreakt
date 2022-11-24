package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import kotlinx.coroutines.flow.Flow

class BehaviourRef<S>(private val relatedWith: PulverizedComponentType) : ComponentRef<S> {
    override suspend fun sendToComponent(message: S) {
        TODO("Not yet implemented")
    }

    override suspend fun receiveFromComponent(): Flow<S> {
        TODO("Not yet implemented")
    }

    override suspend fun receiveLastFromComponent(): S {
        TODO("Not yet implemented")
    }
}
