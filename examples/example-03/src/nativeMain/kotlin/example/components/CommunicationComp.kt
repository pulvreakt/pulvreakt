package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Communication
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

actual class CommunicationComp : Communication<NeighboursMessage> {
    override val context: Context by inject()

    override fun send(payload: NeighboursMessage) {
        TODO("Not yet implemented")
    }

    override fun receive(): Flow<NeighboursMessage> {
        TODO("Not yet implemented")
    }
}
