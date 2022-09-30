package it.nicolasfarabegoli

import it.nicolasfarabegoli.pulverization.core.*

class DummySensorsContainer(override var sensors: Set<Sensor<SensorPayload, Int>>) : SensorsContainer<Int> {
    override fun <S : Sensor<SensorPayload, Int>> addSensor(sensor: S) {
        sensors += sensor
    }
}

class DummyActuatorContainer(override var actuators: Set<Actuator<ActuatorPayload, Int>>) : ActuatorsContainer<Int> {
    override fun <A : Actuator<ActuatorPayload, Int>> addActuator(actuator: A) {
        actuators += actuator
    }
}
