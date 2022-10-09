package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MySensorsComponent(override val deviceID: String) : DeviceComponent<SensorPayload, Unit, String>, KoinComponent {
    private val sensorsContainer: SensorsContainer<String> by inject()

    override fun sendToComponent(payload: SensorPayload, to: String) {
        TODO()
    }

    override fun receiveFromComponent(from: String) {
        TODO("Not yet implemented")
    }

    override suspend fun cycle() {
        sensorsContainer.get<MySensor> {
            val sensedValue = sense()
            sendToComponent(SensorPayload.SensorResult("", sensedValue), "")
        }
    }
}
