package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

@Serializable
sealed interface StateOps : StateRepresentation

@Serializable
data class Distances(val distances: List<Pair<String, Double>>) : StateOps

@Serializable
data class Query(val query: String) : StateOps

class StateComp : State<StateOps> {
    override val context: Context by inject()

    override fun get(): StateOps {
        TODO("Not yet implemented")
    }

    override fun update(newState: StateOps): StateOps {
        TODO("Not yet implemented")
    }
}

suspend fun stateLogic(state: State<StateOps>, behaviour: BehaviourRef<StateOps>) {
    behaviour.receiveFromComponent().collect {
        when (it) {
            is Distances -> state.update(it)
            is Query -> {
                val queryContent = it.query
                println("Query: $queryContent")
                behaviour.sendToComponent(state.get())
            }
        }
    }
}
