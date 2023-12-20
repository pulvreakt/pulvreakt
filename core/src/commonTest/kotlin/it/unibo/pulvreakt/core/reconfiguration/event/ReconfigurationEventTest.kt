package it.unibo.pulvreakt.core.reconfiguration.event

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.reconfiguration.event.AbstractReconfigurationEvent
import it.unibo.pulvreakt.api.reconfiguration.event.ReconfigurationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class ReconfigurationEventTest : StringSpec(
    {
        coroutineTestScope = true
        "The ReconfigurationEvent should produce a result for each event" {
            val resultList = mutableListOf<Int>()
            val reconfigurationEvent = object : AbstractReconfigurationEvent<Int>() {
                override fun eventsFlow(): Flow<Int> = flowOf(1, 2, 3, 4, 5, 6)
                override fun condition(event: Int): Boolean = event > 4
                override fun onReconfigurationSuccess(result: ReconfigurationResult.ReconfigurationSuccess<Int>) {
                    resultList.add(result.event)
                }

                override fun onSkipCheck(result: ReconfigurationResult.SkipCheck<Int>) {
                    resultList.add(result.event)
                }
            }
            reconfigurationEvent.eventsFlow().collect {
                val result = if (reconfigurationEvent.condition(it)) {
                    ReconfigurationResult.ReconfigurationSuccess(it)
                } else {
                    ReconfigurationResult.SkipCheck(it)
                }
                reconfigurationEvent.onProcessedEvent(result)
            }
            resultList.size shouldBe 6
            resultList shouldBe listOf(1, 2, 3, 4, 5, 6)
        }
        "The ReconfigurationEvent should produce a twin result flow when the events are processed" {
            val resultList = mutableListOf<ReconfigurationResult<*>>()
            val reconfigurationEvent = object : AbstractReconfigurationEvent<Int>() {
                override fun eventsFlow(): Flow<Int> = flowOf(1, 2, 3, 4, 5, 6)
                override fun condition(event: Int): Boolean = event > 4
            }
            val resultJob = launch(UnconfinedTestDispatcher()) {
                reconfigurationEvent.resultsFlow().take(6).collect { resultList.add(it) }
            }
            reconfigurationEvent.eventsFlow().collect {
                val result = if (reconfigurationEvent.condition(it)) {
                    ReconfigurationResult.ReconfigurationSuccess(it)
                } else {
                    ReconfigurationResult.SkipCheck(it)
                }
                reconfigurationEvent.onProcessedEvent(result)
            }
            resultJob.join()
            resultList.size shouldBe 6
        }
        "The ReconfigurationEvent should handle the error in the flow of events" {
            val resultList = mutableListOf<ReconfigurationResult<*>>()
            val exception = Exception("Error")
            val reconfigurationEvent = object : AbstractReconfigurationEvent<Int>() {
                override fun eventsFlow(): Flow<Int> = flow {
                    for (i in 1..6) {
                        if (i == 4) {
                            throw exception
                        } else {
                            emit(i)
                        }
                    }
                }

                override fun condition(event: Int): Boolean = event > 4
            }
            val resultJob = launch(UnconfinedTestDispatcher()) {
                reconfigurationEvent.resultsFlow().take(4).collect { resultList.add(it) }
            }
            reconfigurationEvent.eventsFlow()
                .catch { err -> reconfigurationEvent.onProcessedEvent(ReconfigurationResult.FailOnReconfiguration(null, err)) }
                .collect {
                    val result = if (reconfigurationEvent.condition(it)) {
                        ReconfigurationResult.ReconfigurationSuccess(it)
                    } else {
                        ReconfigurationResult.SkipCheck(it)
                    }
                    reconfigurationEvent.onProcessedEvent(result)
                }
            resultJob.join()
            resultList shouldContain ReconfigurationResult.FailOnReconfiguration(null, exception)
        }
    },
)
