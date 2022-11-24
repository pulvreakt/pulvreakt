package it.nicolasfarabegoli.pulverization.runtime

import kotlinx.coroutines.flow.Flow

interface ComponentRef<S> {
    suspend fun sendToComponent(message: S)
    suspend fun receiveFromComponent(): Flow<S>
    suspend fun receiveLastFromComponent(): S
}

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

class BehaviourRef<S> : ComponentRef<S> {
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

class CommunicationRef<S> : ComponentRef<S> {
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

class SensorsRef<S> : ComponentRef<S> {
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

class ActuatorRef<S> : ComponentRef<S> {
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
