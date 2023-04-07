@file:Suppress("UNUSED_PARAMETER")

package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef

typealias StateLogicType<S> = suspend (State<S>, BehaviourRef<S>) -> Unit
typealias ActuatorsLogicType<AS> = suspend (ActuatorsContainer, BehaviourRef<AS>) -> Unit
typealias SensorsLogicType<SS> = suspend (SensorsContainer, BehaviourRef<SS>) -> Unit
typealias CommunicationLogicType<C> = suspend (Communication<C>, BehaviourRef<C>) -> Unit
typealias BehaviourLogicType<S, C, SS, AS, R> = suspend (
    Behaviour<S, C, SS, AS, R>,
    StateRef<S>,
    CommunicationRef<C>,
    SensorsRef<SS>,
    ActuatorsRef<AS>,
) -> Unit

internal suspend fun <S : Any, C : Any, SS : Any, AS : Any, R : Any> defaultBehaviourLogic(
    behaviour: Behaviour<S, C, SS, AS, R>,
    stateRef: StateRef<S>,
    commRef: CommunicationRef<C>,
    sensorsRef: SensorsRef<SS>,
    actuatorsRef: ActuatorsRef<AS>,
): Unit = TODO()

internal suspend fun <S : Any> defaultStateLogic(
    state: State<S>,
    behaviourRef: BehaviourRef<S>,
): Unit = TODO()

internal suspend fun <C : Any> defaultCommunicationLogic(
    comm: Communication<C>,
    behaviourRef: BehaviourRef<C>,
): Unit = TODO()

internal suspend fun <SS : Any> defaultSensorsLogic(
    sensors: SensorsContainer,
    behaviourRef: BehaviourRef<SS>,
): Unit = TODO()

internal suspend fun <AS : Any> defaultActuatorsLogic(
    actuators: ActuatorsContainer,
    behaviourRef: BehaviourRef<AS>,
): Unit = TODO()
