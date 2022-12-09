package it.nicolasfarabegoli.pulverization.runtime.context

import it.nicolasfarabegoli.pulverization.component.Context

internal expect suspend fun createContext(configFilePath: String = ".pulverization.env"): Context
