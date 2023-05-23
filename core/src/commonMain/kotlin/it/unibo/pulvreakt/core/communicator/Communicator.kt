package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.Initializable
import kotlinx.coroutines.flow.Flow

sealed interface Mode {
    object Local : Mode
    object Remote : Mode
}

interface Communicator : Initializable {
    suspend fun communicatorSetup(source: Component<*>, destination: Component<*>)
    fun setMode(mode: Mode)
    suspend fun sendToComponent(message: ByteArray): Either<String, Unit>
    suspend fun receiveFromComponent(): Either<String, Flow<ByteArray>>
}
