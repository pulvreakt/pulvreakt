package it.nicolasfarabegoli

import it.nicolasfarabegoli.pulverization.core.*

class MyDevice(
    override val id: Int,
    override val sensorsContainer: SensorsContainer<Int>,
    override val actuatorsContainer: ActuatorsContainer<Int>,
    override val communication: Communication<NetworkSend, NetworkReceive>,
    override val state: State<Int>,
    override val behaviour: Behaviour<Int, Unit, SensorPayload, ActuatorPayload, Unit>
) : Device<Int, Int, Unit, NetworkSend, NetworkReceive, Unit>
