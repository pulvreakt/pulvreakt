package it.unibo.pulvreakt.modularization.runtime.api.network.heartbeat

import arrow.core.Either
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import it.unibo.pulvreakt.modularization.runtime.errors.network.NetworkError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class HeartbeatNetwork(retentionTime: Long, dispatcher: CoroutineDispatcher = Dispatchers.Default) : Network {
    protected val heartbeatController: HeartbeatController = HeartbeatController(retentionTime, dispatcher)

    override suspend fun setup(): Either<NetworkError, Unit> {
        return heartbeatController.setup()
    }

    override suspend fun teardown(): Either<NetworkError, Unit> {
        return heartbeatController.teardown()
    }
}
