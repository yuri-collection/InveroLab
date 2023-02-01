package cc.trixey.invero.ui.common

/**
 * Invero
 * cc.trixey.invero.ui.common.ProxyInventory
 *
 * @author Arasple
 * @since 2023/1/20 13:12
 */
interface ProxyInventory {

    val window: Window

    val containerType: cc.trixey.invero.ui.common.ContainerType
        get() = window.type

    val containerSize: Int
        get() = containerType.containerSize

    val inventoryTitle: String
        get() = window.title

    fun isViewing(): Boolean

    fun open()

    fun close(doCloseInventory: Boolean, updateInventory: Boolean)

    fun clear(slots: Collection<Int>)

}