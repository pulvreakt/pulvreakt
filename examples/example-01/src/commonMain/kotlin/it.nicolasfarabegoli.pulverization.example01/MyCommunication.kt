package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent

expect class MyCommunication : Communication<Export, List<Export>, String>

expect class MyCommunicationComponent : SendReceiveDeviceComponent<List<Export>, Export, String>, KoinComponent
