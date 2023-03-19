package cc.trixey.invero.core.animation

import cc.trixey.invero.core.serialize.CycleModeSerializer
import kotlinx.serialization.Serializable
import taboolib.common.util.Strings

/**
 * TrMenu
 * cc.trixey.invero.core.animation.CycleMode
 *
 * @author Arasple
 * @since 2023/1/13 12:29
 */
@Serializable(with = CycleModeSerializer::class)
enum class CycleMode {

    ONE_WAY,

    LOOP,

    REVERSIBLE,

    RANDOM;

    val isOneway: Boolean
        get() = this == ONE_WAY

    val isLoop: Boolean
        get() = this == LOOP

    val isReversible: Boolean
        get() = this == REVERSIBLE

    val isRandom: Boolean
        get() = this == RANDOM

    companion object {

        fun of(name: String): CycleMode {
            return values().maxBy { Strings.similarDegree(name.uppercase(), it.name) }
        }

    }

}