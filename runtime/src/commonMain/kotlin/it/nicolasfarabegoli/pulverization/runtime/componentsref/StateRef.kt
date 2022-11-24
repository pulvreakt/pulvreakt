package it.nicolasfarabegoli.pulverization.runtime.componentsref

import kotlinx.coroutines.flow.Flow

class StateRef<S> : ComponentRef<S> {
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
