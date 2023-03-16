package cc.trixey.invero.common.util

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

/**
 * Invero
 * cc.trixey.invero.core.util.Item
 *
 * @author Arasple
 * @since 2023/1/16 20:34
 */
fun ItemStack.hasName(): Boolean {
    return itemMeta?.hasDisplayName() == true
}

fun ItemStack.getName(): String? {
    return itemMeta?.displayName
}

fun ItemStack.getLore(): List<String>? {
    return itemMeta?.lore
}

fun ItemStack.hasLore(): Boolean {
    return itemMeta?.hasLore() == true
}

fun ItemStack.postShiny(shiny: Boolean) {
    if (!shiny) {
        itemMeta = itemMeta?.also { it.enchants.clear() }
        return
    }

    val itemMeta = itemMeta
    if (itemMeta is EnchantmentStorageMeta) {
        itemMeta.addStoredEnchant(Enchantment.LURE, 1, true)
    } else {
        itemMeta.addEnchant(Enchantment.LURE, 1, true)
    }
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

    this.itemMeta = itemMeta
}

fun ItemStack.postName(name: String) {
    itemMeta = itemMeta?.also { it.setDisplayName(name) }
}

fun ItemStack.postLore(lore: List<String>) {
    itemMeta = itemMeta?.also { it.lore = lore }
}

fun ItemStack.postAmount(amount: Int) {
    this.amount = amount
}

fun ItemStack.postModel(model: Int) {
    itemMeta = itemMeta?.also { it.setCustomModelData(model) }
}
