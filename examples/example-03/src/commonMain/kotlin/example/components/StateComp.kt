package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

@Serializable
sealed interface StateOps : StateRepresentation

@Serializable
data class Coordinate(val latitude: Double, val longitude: Double) : StateOps

@Serializable
data class Query(val query: String)

class StateComp : State<StateOps> {
    override val context: Context by inject()

    override fun get(): StateOps {
        TODO("Not yet implemented")
    }

    override fun update(newState: StateOps): StateOps {
        TODO("Not yet implemented")
    }
}
