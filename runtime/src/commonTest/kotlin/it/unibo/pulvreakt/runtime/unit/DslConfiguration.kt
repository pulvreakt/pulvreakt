package it.unibo.pulvreakt.runtime.unit

import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.pulverization

val cap by Capability
val host = Host("name", cap)
val infrastructure = nonEmptySetOf(host)

val configuration = pulverization<Int> {
    val dev by logicDevice<Int, Unit, Unit, Unit> {
        withBehaviour<FixtureBehavior>() requires cap
        withState<FixtureState>() requires cap
    }
    deployment(infrastructure, TestProtocol()) {
        device(dev) {
            FixtureBehavior() startsOn host
            FixtureState() startsOn host
        }
    }
}
