package it.unibo.pulvreakt.modularization.dsl

import it.unibo.pulvreakt.modularization.api.module.Module
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem

class ModularizationScope {
    private val modulesAssociation = mutableMapOf<Module<*, *, *>, Set<Module<*, *, *>>>()

    infix fun <Output> Module<*, *, Output>.connectedTo(otherModule: Module<*, Output, *>) {
        modulesAssociation[this]?.let { modulesAssociation[this] = it + otherModule }
            ?: run { modulesAssociation[this] = setOf(otherModule) }
    }

    infix fun <Output> Module<*, *, Output>.connectedTo(otherModule: ModuleBuilderInput<Output>) {
        modulesAssociation[this]?.let { modulesAssociation[this] = it + otherModule.inputModules }
            ?: run { modulesAssociation[this] = otherModule.inputModules }
    }

    infix fun <Output> ModuleBuilderOutput<Output>.connectedTo(otherModule: Module<*, Output, *>) {
        outputModules.forEach { module ->
            modulesAssociation[module]?.let { modulesAssociation[module] = it + otherModule }
                ?: run { modulesAssociation[module] = setOf(otherModule) }
        }
    }

    infix fun <Output> ModuleBuilderOutput<Output>.connectedTo(otherModule: ModuleBuilderInput<Output>) {
        outputModules.forEach { module ->
            modulesAssociation[module]?.let { modulesAssociation[module] = it + otherModule.inputModules }
                ?: run { modulesAssociation[module] = otherModule.inputModules }
        }
    }

    infix fun <Output> Module<*, *, Output>.and(otherModule: Module<*, *, Output>): ModuleBuilderOutput<Output> =
        ModuleBuilderOutput(setOf(this, otherModule))

    infix fun <Output> ModuleBuilderOutput<Output>.and(otherModule: Module<*, *, Output>): ModuleBuilderOutput<Output> =
        ModuleBuilderOutput(outputModules + otherModule)

    infix fun <Output> ModuleBuilderOutput<Output>.and(otherModule: ModuleBuilderOutput<Output>): ModuleBuilderOutput<Output> =
        ModuleBuilderOutput(outputModules + otherModule.outputModules)

    infix fun <Input> Module<*, Input, *>.andIn(otherModule: Module<*, Input, *>): ModuleBuilderInput<Input> =
        ModuleBuilderInput(setOf(this, otherModule))

    infix fun <Input> ModuleBuilderInput<Input>.andIn(otherModule: Module<*, Input, *>): ModuleBuilderInput<Input> =
        ModuleBuilderInput(inputModules + otherModule)

    infix fun <Input> ModuleBuilderInput<Input>.andIn(otherModule: ModuleBuilderInput<Input>): ModuleBuilderInput<Input> =
        ModuleBuilderInput(inputModules + otherModule.inputModules)

    data class ModuleBuilderInput<Input>(val inputModules: Set<Module<*, Input, *>>)
    data class ModuleBuilderOutput<Output>(val outputModules: Set<Module<*, *, Output>>)

    internal fun build(): ModularizedSystem = modulesAssociation.toMap()
}
