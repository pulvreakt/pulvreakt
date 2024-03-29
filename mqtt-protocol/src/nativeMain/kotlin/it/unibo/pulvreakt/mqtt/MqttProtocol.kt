package it.unibo.pulvreakt.mqtt

import arrow.core.Either
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI

/**
 * Represents the MQTT protocol used in PulvReAKt.
 */
@Suppress("UnusedPrivateProperty")
actual class MqttProtocol actual constructor(
    private val host: String,
    private val port: Int,
    private val username: String?,
    private val password: String?,
    private val coroutineDispatcher: CoroutineDispatcher,
) : Protocol {
    override lateinit var di: DI

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override suspend fun setupChannel(source: Entity, destination: Entity) {
        TODO("Not yet implemented")
    }

    override suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>> {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }
}
