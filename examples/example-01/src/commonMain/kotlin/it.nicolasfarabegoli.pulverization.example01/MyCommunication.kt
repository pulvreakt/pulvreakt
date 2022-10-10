package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent

expect class MyCommunication : Communication<String, Map<String, String>, String>

expect class MyCommunicationComponent : SendReceiveDeviceComponent<Map<String, String>, String, String>, KoinComponent
