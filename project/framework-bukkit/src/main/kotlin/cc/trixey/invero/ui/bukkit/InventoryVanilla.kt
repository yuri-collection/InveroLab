package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.bukkit.api.isRegistered
import cc.trixey.invero.ui.bukkit.nms.handler
import cc.trixey.invero.ui.bukkit.panel.CraftingPanel
import cc.trixey.invero.ui.bukkit.util.clickType
import cc.trixey.invero.ui.bukkit.util.synced
import org.bukkit.Bukkit
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.InventoryVanilla
 *
 * @author Arasple
 * @since 2023/1/20 13:13
 */
class InventoryVanilla(override val window: BukkitWindow) : ProxyBukkitInventory {

    val container = if (containerType.isOrdinaryChest)
        Bukkit.createInventory(Holder(window), containerType.containerSize, inventoryTitle)
    else try {
        Bukkit.createInventory(Holder(window), InventoryType.valueOf(containerType.bukkitType), inventoryTitle)
    } catch (e: Throwable) {
        error("Not supported inventory type (${containerType.bukkitType}) yet")
    }

    override var containerId: Int = -1
    override val hidePlayerInventory: Boolean
        get() = window.hidePlayerInventory

    override fun isVirtual(): Boolean {
        return false
    }

    private var clickCallback: (InventoryClickEvent) -> Boolean = { true }
    private var moveCallback: (InventoryClickEvent) -> Boolean = { true }
    private var collectCallback: (InventoryClickEvent) -> Boolean = { true }
    private var dragCallback: (InventoryDragEvent) -> Boolean = { true }

    fun onClick(handler: (InventoryClickEvent) -> Boolean): InventoryVanilla {
        clickCallback = handler
        return this
    }

    fun onItemsMove(handler: (InventoryClickEvent) -> Boolean): InventoryVanilla {
        moveCallback = handler
        return this
    }

    fun onItemsCollect(handler: (InventoryClickEvent) -> Boolean): InventoryVanilla {
        collectCallback = handler
        return this
    }

    fun onDrag(handler: (InventoryDragEvent) -> Boolean): InventoryVanilla {
        dragCallback = handler
        return this
    }

    override fun clear(slots: Collection<Int>) {
        slots.forEach { set(it, null) }
    }

    override fun get(slot: Int): ItemStack? {
        return if (slot + 1 > containerSize) {
            getPlayerInventory().getItem(slot.outflowCorrect())
        } else {
            container.getItem(slot)
        }
    }

    override fun set(slot: Int, itemStack: ItemStack?) {
        synced {
            if (slot + 1 > containerSize) {
                getPlayerInventory().setItem(slot.outflowCorrect(), itemStack)
            } else {
                container.setItem(slot, itemStack)
            }
        }
    }

    override fun isViewing(): Boolean {
        return viewer.openInventory.topInventory == container
    }

    override fun open() {
        viewer.openInventory(container)
        containerId = handler.getContainerId(viewer)
    }

    fun handleClick(e: InventoryClickEvent) {
        // 默认取消事件
        e.isCancelled = true
        if (!clickCallback(e)) return
        // 点击的坐标
        val slot = e.rawSlot
        // 如果点击玩家背包容器
        if (slot > window.type.slotsContainer.last) {
            if (!hidePlayerInventory && window.anyIOPanel) {

                e.isCancelled = false
                return
            }
        }
        // 转化为 x,y 定位
        val pos = window.scale.convertToPosition(slot)
        // 查找有效面板
        window
            .panels
            .sortedByDescending { it.weight }
            .forEach {
                if (pos in it.area) {
                    val converted = e.clickType
                    if (it.runClickCallbacks(pos, converted, e)) {
                        it.handleClick(pos - it.locate, converted, e)
                    }
                    return
                }
            }
    }

    fun handleDrag(e: InventoryDragEvent) {
        // 默认取消
        e.isCancelled = true
        if (!dragCallback(e)) return
        // 寻找 Panel 交接
        val handler = window
            .panels
            .sortedBy { it.locate }
            .sortedByDescending { it.weight }
            .find {
                e.rawSlots.all { slot -> window.scale.convertToPosition(slot) in it.area }
            }
        // 传递给 Panel 处理
        if (handler != null) {
            val affected = e.rawSlots.map { window.scale.convertToPosition(it) }
            handler.handleDrag(affected, e)
        }
    }

    fun handleItemsMove(e: InventoryClickEvent) {
        // 默认取消
        e.isCancelled = true
        if (!moveCallback(e)) return

        val slot = e.rawSlot
        // playerInventory -> IO Panel
        if (slot > window.type.slotsContainer.last) {
            if (hidePlayerInventory || !window.anyIOPanel) return handleClick(e)
            val insertItem = e.currentItem?.clone() ?: return handleClick(e)
            window
                .getPanelsRecursively()
                .filterIsInstance<CraftingPanel>()
                .sortedBy { it.locate }
                .sortedByDescending { it.weight }
                .also { if (it.isEmpty()) return handleClick(e) }
                .forEach {
                    val previous = insertItem.amount
                    val result = it.insert(insertItem.clone())
                    insertItem.amount = result

                    if (previous != result) {
                        it.renderStorage()
                        it.runCallback()
                    }
                    if (result <= 0) return@forEach
                }
            e.currentItem?.amount = insertItem.amount
        }
        // IO Panel -> playerInventory
        else if (!hidePlayerInventory && window.anyIOPanel) {
            val clickedSlot = window.scale.convertToPosition(slot)

            window
                .panels
                .sortedBy { it.locate }
                .sortedByDescending { it.weight }
                .find { it is CraftingPanel && window.scale.convertToPosition(e.rawSlot) in it.area }
                ?.handleItemsMove(clickedSlot, e)
                .let { if (it == null) return handleClick(e) }
        } else {
            return handleClick(e)
        }
    }

    fun handleItemsCollect(e: InventoryClickEvent) {
        // 默认取消
        e.isCancelled = true
        if (!collectCallback(e)) return
        // 暂时未写双击收集物品的逻辑...
        return handleClick(e)
    }

    fun handleOpenEvent(e: InventoryOpenEvent) {

    }

    fun handleCloseEvent(e: InventoryCloseEvent) {
        if (window.isRegistered()) {
            window.close(doCloseInventory = false, updateInventory = false)
        }
    }


    class Holder(val window: BukkitWindow) : InventoryHolder {

        internal val inventory: Inventory
            get() = (window.inventory as InventoryVanilla).container

        override fun getInventory(): Inventory {
            return inventory
        }

    }

}