package example.units

import example.components.StateComp
import example.components.stateLogic
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.stateLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.pulverizationPlatform
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val platform = pulverizationPlatform(config.getDeviceConfiguration("gps")!!) {
        stateLogic(StateComp(), ::stateLogic)
    }
    val jobs = platform.start()
    jobs.forEach { it.join() }
    platform.stop()
}
