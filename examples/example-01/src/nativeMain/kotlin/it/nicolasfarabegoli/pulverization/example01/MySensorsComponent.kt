package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendOnlyDeviceComponent
import org.koin.core.component.KoinComponent

actual class MySensorsComponent(override val deviceID: String) : SendOnlyDeviceComponent<SensorPayload, String>, KoinComponent {
    override fun sendToComponent(payload: SensorPayload, to: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
