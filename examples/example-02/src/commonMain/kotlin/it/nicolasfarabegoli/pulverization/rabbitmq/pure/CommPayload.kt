package it.nicolasfarabegoli.pulverization.rabbitmq.pure

import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import kotlinx.serialization.Serializable

@Serializable
data class CommPayload(val deviceID: String, val p: Double) : CommunicationPayload
