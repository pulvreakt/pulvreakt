package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

@Serializable
sealed interface StateOps

@Serializable
data class Distances(val distances: List<Pair<String, Double>>, val nearest: Pair<String, Double>?) : StateOps

@Serializable
data class Query(val query: String) : StateOps

class StateComp : State<StateOps> {
    override val context: Context by inject()

    private var state = Distances(emptyList(), "" to 0.0)

    override fun get(): StateOps = state

    override fun update(newState: StateOps): StateOps {
        val tmp = state
        when (newState) {
            is Distances -> state = newState
            is Query -> println("Query received: ${newState.query}")
        }
        return tmp
    }
}

suspend fun stateLogic(state: State<StateOps>, behaviour: BehaviourRef<StateOps>) {
    behaviour.receiveFromComponent().collect {
        when (it) {
            is Distances -> state.update(it)
            is Query -> {
                // val queryContent = it.query
                // println("Query: $queryContent")
                behaviour.sendToComponent(state.get())
            }
        }
    }
}
