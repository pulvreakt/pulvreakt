package it.unibo.pulvreakt.modularization.dsl

import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptyListOrNull
import it.unibo.pulvreakt.modularization.api.module.Module
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.module.module
import it.unibo.pulvreakt.modularization.dsl.errors.ConfigurationError
import it.unibo.pulvreakt.modularization.dsl.errors.ModuleNotAvailable

/**
 * Configurations scope for modularization system with the available [modules].
 */
class ModularizationScope(private val modules: Set<Module<*, *, *>>) {
    /**
     * The set of [SymbolicModule]s are inputs of the [Module]s.
     */
    private val modulesAssociation = mutableMapOf<SymbolicModule, Set<SymbolicModule>>()
    private val configurationErrors = mutableListOf<ConfigurationError>()

    /**
     * Builds the input or output of [Module]s.
     */
    infix fun SymbolicModule.and(other: SymbolicModule): ModuleBuilder = ModuleBuilder(setOf(this, other))

    /**
     * Builds the input or output of [Module]s.
     */
    infix fun ModuleBuilder.and(other: SymbolicModule): ModuleBuilder = ModuleBuilder(symbolicModules + other)

    /**
     * Builds the input or output of [Module]s.
     */
    infix fun ModuleBuilder.and(otherBuilder: ModuleBuilder): ModuleBuilder = ModuleBuilder(symbolicModules + otherBuilder.symbolicModules)

    /**
     * Connects a [Module] to an[other] [Module].
     */
    infix fun SymbolicModule.wiredTo(other: SymbolicModule) = ModuleBuilder(setOf(this)) wiredTo ModuleBuilder(setOf(other))

    /**
     * Connects multiple [Module]s to an[other] [Module].
     */
    infix fun ModuleBuilder.wiredTo(other: SymbolicModule) = wiredTo(ModuleBuilder(setOf(other)))

    /**
     * Connects multiple [Module]s to an[other] [Module].
     */
    infix fun SymbolicModule.wiredTo(other: ModuleBuilder) = ModuleBuilder(setOf(this)) wiredTo other

    /**
     * Connects multiple [Module]s to [otherModules].
     */
    infix fun ModuleBuilder.wiredTo(otherModules: ModuleBuilder) {
        when {
            symbolicModules.any { !moduleInContext(it, modules) } ->
                symbolicModules
                    .filter { !moduleInContext(it, modules) }
                    .forEach { configurationErrors.add(ModuleNotAvailable(it)) }

            otherModules.symbolicModules.any { !moduleInContext(it, modules) } ->
                otherModules.symbolicModules
                    .filter { !moduleInContext(it, modules) }.forEach { configurationErrors.add(ModuleNotAvailable(it)) }

            else ->
                otherModules.symbolicModules.forEach { module ->
                    modulesAssociation[module]?.let { modulesAssociation[module] = it + symbolicModules }
                        ?: run { modulesAssociation[module] = symbolicModules }
                }
        }
    }

    /**
     * Aggregates the [symbolicModules] acting as input or outputs of the [Module]s.
     */
    data class ModuleBuilder(val symbolicModules: Set<SymbolicModule>)

    private fun moduleInContext(
        module: SymbolicModule,
        modules: Set<Module<*, *, *>>,
    ): Boolean = module in modules.map { module(it) }

    internal fun build(): ModularizationResult =
        when {
            configurationErrors.isNotEmpty() -> configurationErrors.toNonEmptyListOrNull()!!.left()
            else -> modulesAssociation.right()
        }
}
