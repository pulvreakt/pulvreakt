package it.nicolasfarabegoli.pulverization.runtime.spawner

import kotlinx.coroutines.Job

internal interface Spawnable {
    suspend fun spawn(): Job
    suspend fun kill()
}
