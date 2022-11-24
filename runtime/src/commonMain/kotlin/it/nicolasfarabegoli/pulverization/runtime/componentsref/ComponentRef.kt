package it.nicolasfarabegoli.pulverization.runtime.componentsref

import kotlinx.coroutines.flow.Flow

interface ComponentRef<S> {
    suspend fun sendToComponent(message: S)
    suspend fun receiveFromComponent(): Flow<S>
    suspend fun receiveLastFromComponent(): S
}
