package it.nicolasfarabegoli.pulverization.platform.it.nicolasfarabegoli.pulverization.platform

// import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
// import it.nicolasfarabegoli.pulverization.core.Behaviour
// import it.nicolasfarabegoli.pulverization.core.Device
// import it.nicolasfarabegoli.pulverization.core.SensorsContainer
// import it.nicolasfarabegoli.pulverization.core.State
//
// /**
// * Represent the platform in which a [Device] runs.
// */
// interface PulverizationPlatform<I, S, E, SC, PC, SendType, ReceiveType, Outcome> {
//    val repetitions: Int
//    val device: Device<I, S, E, SC, PC, SendType, ReceiveType, Outcome>
//
//    suspend fun contextAcquisition(state: State<S>, sensors: SensorsContainer<I>, communication: suspend () -> ReceiveType)
//    fun computation(state: State<S>, behaviour: Behaviour<S, E, SC, PC, Outcome>)
//    suspend fun dataPropagation(state: State<S>, communication: suspend (payload: SendType) -> Unit)
//    fun actuation(state: State<S>, actuators: ActuatorsContainer<I>)
//
//    private suspend fun cycle() {
//        contextAcquisition(device.state, device.sensorsContainer, device.communication::receive)
//        computation(device.state, device.behaviour)
//        dataPropagation(device.state, device.communication::send)
//        actuation(device.state, device.actuatorsContainer)
//    }
//
//    suspend fun start() {
//        repeat(repetitions) { cycle() }
//    }
//
//    suspend fun infiniteStart() {
//        while (true) {
//            cycle()
//        }
//    }
// }
