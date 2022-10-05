package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent

expect class MyCommunication : Communication<String, String>

expect class MyCommunicationComponent : DeviceComponent<String, String, String>, KoinComponent
