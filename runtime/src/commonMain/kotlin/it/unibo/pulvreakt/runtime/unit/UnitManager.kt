package it.unibo.pulvreakt.core.unit

import it.unibo.pulvreakt.core.component.Initializable
import kotlinx.coroutines.flow.Flow

internal interface UnitManager : Initializable {
    fun configurationUpdates(): Flow<NewConfiguration>
}
