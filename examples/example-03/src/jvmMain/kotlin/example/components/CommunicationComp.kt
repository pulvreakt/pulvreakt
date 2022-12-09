package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

@Serializable
data class NeighboursMessage(val deviceId: String, val location: Gps) : CommunicationPayload

class CommunicationComp : Communication<NeighboursMessage> {
    override val context: Context by inject()

    override fun send(payload: NeighboursMessage) {
        TODO("Not yet implemented")
    }

    override fun receive(): Flow<NeighboursMessage> {
        TODO("Not yet implemented")
    }
}
