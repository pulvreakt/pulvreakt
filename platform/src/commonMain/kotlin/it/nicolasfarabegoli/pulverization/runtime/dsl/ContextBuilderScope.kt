package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.runtime.context.createContext

/**
 * Scope to configure the context.
 */
class ContextBuilderScope {
    private var path: String = ".pulverization.env"
    private var deviceID: String? = null

    /**
     * Specifies where is located the ".pulverization.env" file.
     */
    fun configFilePath(path: String) {
        this.path = path
    }

    /**
     * Configures the DeviceID.
     */
    fun deviceID(id: String) {
        deviceID = id
    }

    internal suspend fun build(): Context {
        return deviceID?.let {
            return object : Context {
                override val deviceID: String = it
            }
        } ?: createContext(path)
    }
}
