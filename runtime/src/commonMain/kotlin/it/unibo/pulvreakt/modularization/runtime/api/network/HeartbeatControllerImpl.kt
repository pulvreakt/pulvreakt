package it.unibo.pulvreakt.modularization.runtime.api.network

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.injection.Injection
import it.unibo.pulvreakt.modularization.runtime.api.network.protocol.Heartbeat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

internal class HeartbeatControllerImpl(
    override val retentionTime: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : HeartbeatController, DIAware {
    override val di: DI by lazy {
        Injection.getModule().getOrNull() ?: error(
            """
            Tried to access to the Dependency Injection module before it is initialized.
            Please, initialize the Injection object via the setupModule(DI) method.
            """.trimIndent(),
        )
    }
    private val currentHost: Host<*> by instance()
    private val neighborsStateFlow: MutableStateFlow<NeighborsState> = MutableStateFlow(emptySet())
    private val neighborhood: MutableMap<Host<*>, Long> = mutableMapOf()
    private val scope = CoroutineScope(dispatcher + Job())
    private val periodTimeCheck: Long = retentionTime / 2

    override fun neighborsState(): StateFlow<NeighborsState> = neighborsStateFlow.asStateFlow()

    override suspend fun createHeartbeat(): Heartbeat = Heartbeat(currentHost, Clock.System.now().toEpochMilliseconds())

    override fun receiveHeartbeat(heartbeat: Heartbeat) {
        neighborhood[heartbeat.host] = heartbeat.timestamp
    }

    /**
     * Periodically checks the neighborhood to remove dead hosts.
     * A host is considered dead if it does not send a heartbeat for a time greater than [retentionTime].
     */
    override suspend fun setup(): Either<Nothing, Unit> {
        scope.launch {
            try {
                while (true) {
                    val now = Clock.System.now().toEpochMilliseconds()
                    val deadHosts = neighborhood.filter { (_, timestamp) -> now - timestamp > retentionTime }
                    if (deadHosts.isNotEmpty()) {
                        neighborhood -= deadHosts.keys
                        neighborsStateFlow.value = neighborhood.keys.toSet()
                    }
                    delay(periodTimeCheck)
                }
            } catch (_: CancellationException) {
                // Do nothing
            }
        }
        return Unit.right()
    }

    override suspend fun teardown(): Either<Nothing, Unit> {
        scope.cancel()
        return Unit.right()
    }
}
