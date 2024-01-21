package it.unibo.pulvreakt.modularization.api.module

/**
 * A symbolic representation of a module.
 */
interface SymbolicModule

/**
 * Smart constructor for [SymbolicModule] from a type of [Module].
 */
inline fun <reified M : Module<*, *, *>> module(): SymbolicModule = object : SymbolicModule {
    override fun equals(other: Any?): Boolean {
        return if (this === other) true
        else if (this.toString() == other.toString()) true
        else false
    }

    override fun toString(): String = M::class.simpleName!!.replaceFirstChar { it.lowercase() }
}

/**
 * Smart constructor for [SymbolicModule] from an instance of [Module].
 */
fun <M : Module<*, *, *>> module(module: M): SymbolicModule = object : SymbolicModule {
    override fun equals(other: Any?): Boolean {
        return if (this === other) true
        else if (this.toString() == other.toString()) true
        else false
    }

    override fun toString(): String = module::class.simpleName!!.replaceFirstChar { it.lowercase() }
}
