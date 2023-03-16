package cc.trixey.invero.core.item

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.icon.IconElement
import cc.trixey.invero.core.util.flatRelease
import cc.trixey.invero.ui.bukkit.api.dsl.set
import cc.trixey.invero.ui.common.Scale
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.common5.cshort
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.modifyMeta

/**
 * Invero
 * cc.trixey.invero.core.item.ItemstackGenerate
 *
 * @author Arasple
 * @since 2023/3/16 23:07
 */
private fun ItemStack.generateProperties(
    frame: Frame,
    context: Context,
): ItemStack {
    return modifyMeta<ItemMeta> {
        // 显示名称
        frame.name?.let {
            setDisplayName(context.parse(it).prefixColored)
        }
        // 显示描述
        frame.lore?.let {
            lore = context.parse(it).loreColored(frame.enhancedLore)
        }
        // [属性] 数量
        frame.amount?.let {
            amount = (frame.staticAmount ?: context.parse(it.content).cint).coerceAtLeast(0)
        }
        // [属性] 损伤值
        frame.damage?.let {
            @Suppress("DEPRECATION")
            durability = frame.staticDamage ?: context.parse(it.content).cshort
        }
        // [属性] 模型数据
        frame.customModelData?.let {
            setCustomModelData(frame.staticCustomModelData ?: context.parse(it.content).cint)
        }
        // [属性] 是否发光
        frame.glow?.let {
            if (frame.staticGlow == true || context.parse(it.content).cbool) {
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
                addUnsafeEnchantment(Enchantment.DURABILITY, 1)
            } else {
                removeEnchantment(Enchantment.DURABILITY)
            }
        }
        // [属性] 不可破坏
        frame.unbreakable?.let {
            if (frame.staticUnbreakable == true || context.parse(it.content).cbool) {
                isUnbreakable = true
            }
        }
        // [属性] 物品标签
        frame.flags?.forEach {
            addItemFlags(ItemFlag.valueOf(it.replace(' ', '_').uppercase()))
        }
    }.also { itemStack ->
        // [属性] NBT
        frame.nbt?.let {
            ItemTag().apply {
                putAll(getItemTag())
                if (frame.staticNBT != null) putAll(frame.staticNBT)
                else putAll(frame.buildNBT { s -> context.parse(s) })
                saveTo(itemStack)
            }
        }
    }
}

fun Frame.renderFor(containerScale: Scale, iconElement: IconElement, setSlot: Boolean = true) {
    val context = iconElement.context
    val previous = iconElement.itemStack

    if (texture == null) {
        iconElement.itemStack = previous.generateProperties(this, context)
    } else {
        texture.generateItem(context) {
            iconElement.itemStack = generateProperties(this@renderFor, context)
        }
    }
    if (setSlot && slot != null) {
        iconElement.set(slot.flatRelease(containerScale))
    }
}

private val String.prefixColored: String
    get() = if (!startsWith("§") && isNotBlank()) "§7$this"
    else this

private fun List<String>.loreColored(enhancedProcess: Boolean?): List<String> {
    return if (enhancedProcess != true) {
        map { it.prefixColored }
    } else {
        val iterator = iterator()

        buildList {
            while (iterator.hasNext()) {
                val it = iterator.next()
                this += it.split("\\n").map { it.prefixColored }
            }
        }
    }
}