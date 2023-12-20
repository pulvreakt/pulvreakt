package it.unibo.pulvreakt.core.dsl.fixture

import it.unibo.pulvreakt.api.reconfiguration.event.AbstractReconfigurationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TestReconfigurationEvent1 : AbstractReconfigurationEvent<Int>() {
    override fun eventsFlow(): Flow<Int> = emptyFlow()
    override fun condition(event: Int): Boolean = false
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class TestReconfigurationEvent2 : AbstractReconfigurationEvent<Float>() {
    override fun eventsFlow(): Flow<Float> = emptyFlow()
    override fun condition(event: Float): Boolean = false
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}
