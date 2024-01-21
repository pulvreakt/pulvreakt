package it.unibo.pulvreakt.modularization.api.module

import arrow.core.Either
import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.errors.module.ModuleError

typealias ModuleResult<Result> = Either<ModuleError, Result>

/**
 * A module requiring [Cap] to be executed, taking [Input] and producing an [Output].
 */
interface Module<out Cap : Capabilities, Input, Output> {
    /**
     * The required capabilities to execute this module.
     */
    val capabilities: Cap

    /**
     * The input modules of this module.
     */
    val inputModules: Set<SymbolicModule>

    /**
     * The output modules of this module.
     */
    val outputModules: Set<SymbolicModule>

    /**
     * Build the input parameters coming from the input modules.
     * This method is necessarily when multiple modules are input of this module,
     * and via this method all the data are collected and the [Input] is build.
     * The output of this function will be the input of the [invoke] function.
     * This method may fail with a [ModuleError] if trying to take an input from a module not registered as input of this module.
     */
    fun buildInput(): ModuleResult<Input>

    /**
     * Receive [from] a module its output.
     * This method may fail with a [ModuleError] if the module is not registered as input of this module.
     */
    fun <Type> receive(from: SymbolicModule): ModuleResult<Type>

    /**
     * Defines the module logic via a function taking an [input] and producing an [Output].
     */
    operator fun invoke(input: Input): Output

    /**
     * Execute a round of the component.
     * Typically, this method is already implemented and the user should not call it directly.
     * It performs the [buildInput] to collect the data from all the inputs modules, execute the module logic via the [invoke] function,
     * and finally output produced by the module is returned.
     */
    fun execute(): ModuleResult<Output>
}
