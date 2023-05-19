package it.unibo.pulvreakt.runtime.spawner

import kotlinx.coroutines.Job

internal interface Spawnable {
    fun spawn(): Job
    suspend fun kill()
}
