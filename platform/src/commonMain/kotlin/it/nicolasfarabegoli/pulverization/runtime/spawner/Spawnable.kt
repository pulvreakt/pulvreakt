package it.nicolasfarabegoli.pulverization.runtime.spawner

import kotlinx.coroutines.Job

internal interface Spawnable {
    fun spawn(): Job
    suspend fun kill()
}
