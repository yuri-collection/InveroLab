package cc.trixey.invero.core.animation

/**
 * TrMenu
 * cc.trixey.invero.core.animation.CycleString
 *
 * @author Arasple
 * @since 2023/1/13 12:26
 */
class Cyclic<T>(private val value: Array<T>, override var mode: CycleMode = CycleMode.LOOP) : Cycle<T> {

    var index: Int = 0

    val maxIndex = value.indices.last

    var reversing: Boolean = false

    fun isSingle(): Boolean {
        return value.size <= 1
    }

    override fun get(): T {
        return value[index]
    }

    override fun cycle() {
        if (mode.isRandom) {
            index = value.indices.random()
        } else if (mode.isOneway && index < maxIndex) {
            index++
        } else if (mode.isLoop) {
            if (index == maxIndex) index = 0
            else index++
        } else if (mode.isReversable) {
            if (reversing) {
                index--
                if (index == 0) reversing = false
            } else {
                index++
                if (index == maxIndex) reversing = true
            }
        }
    }

}