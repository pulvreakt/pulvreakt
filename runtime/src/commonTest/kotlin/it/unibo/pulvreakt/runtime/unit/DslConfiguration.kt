package it.unibo.pulvreakt.runtime.unit

import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.pulverization

val cap by Capability
val host = Host("name", cap)
val infrastructure = nonEmptySetOf(host)

val configuration = pulverization {
    val dev by logicDevice {
        withBehaviour<FixtureBehaviour>() requires cap
        withState<FixtureState>() requires cap
    }
    deployment(infrastructure, TestProtocol()) {
        device(dev) {
            FixtureBehaviour() startsOn host
            FixtureState() startsOn host
        }
    }
}
