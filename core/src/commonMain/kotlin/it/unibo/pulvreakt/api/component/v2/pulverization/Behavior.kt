package it.unibo.pulvreakt.api.component.v2.pulverization

import it.unibo.pulvreakt.api.component.v2.Component

data class Input<State, Message, Sensors>(val state: State, val messages: Sequence<Message>, val sensors: Sensors)

data class Output<State, Message, Actuators>(val state: State, val messages: Sequence<Message>, val actuators: Actuators)

interface Behavior<State, Messages, Sensors, Actuators> : Component<Input<State, Messages, Sensors>, Output<State, Messages, Actuators>>
