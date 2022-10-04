package it.nicolasfarabegoli.pulverization.communication

import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SensorsCommunicator<P, I> : KoinComponent, SenderCommunicator<P, I> {
    private val sensorsContainer: SensorsContainer<I> by inject()
}
