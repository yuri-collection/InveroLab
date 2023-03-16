@file:Suppress("DEPRECATION")

package cc.trixey.invero.core.icon

import cc.trixey.invero.common.util.*
import cc.trixey.invero.core.util.*

/**
 * Invero
 * cc.trixey.invero.core.icon.Util
 *
 * @author Arasple
 * @since 2023/1/16 16:06
 */

//fun Frame.render(agent: AgentPanel, element: IconElement) {
//    val frame = this@render
//    val original = element.itemStack
//    val context = element.context
//
//    fun ItemStack.frameApply(): ItemStack {
//        name?.let { postName(context.parse(name).colored()) }
//        lore?.let { postLore(context.parse(lore).colored(enhancedLore)) }
//        damage?.let { durability = staticDamage ?: it.content.let { s -> context.parse(s) }.cshort }
//
//        customModelData?.let {
//            val model = staticCustomModelData ?: it.content.let { s -> context.parse(s) }.toIntOrNull() ?: 0
//            postModel(model)
//        }
//        glow?.let {
//            if (staticGlow == true || context.parse(it.content).cbool) {
//                postShiny(context.parse(it.content).cbool)
//            }
//        }
//        afterRebasedTexture(this, context)
//        this@render.amount?.let {
//            postAmount(staticAmount ?: context.parse(it.content).toIntOrNull() ?: 1)
//        }
//
//        return this
//    }
//
//    if (texture == null) {
//        element.itemStack = element.itemStack.apply {
//            frameApply()
//            // 当前材质无名称，但之前有，则继承
//            if (name == null && original.hasName()) postName(original.getName()!!)
//            // 当前材质无Lore，但之前有，则继承
//            if (lore.isNullOrEmpty() && original.hasLore()) postLore(original.getLore()!!)
//            // 当前材质的数量继承
//            if (frame.amount == null && original.amount != 1) postAmount(original.amount)
//        }
//    } else texture.generateItem(element.context) { element.itemStack = frameApply() }
//
//    if (slot != null) {
//        element.set(slot.flatRelease(agent.scale))
//    }
//}
//
//fun Frame.afterRebasedTexture(itemStack: ItemStack, context: Context) {
//    flags?.map { flag -> ItemFlag.values().find { it.name.equals(flag, true) } }?.let { flags ->
//        itemStack.itemMeta = itemStack.itemMeta?.also { it.addItemFlags(*flags.toTypedArray()) }
//    }
////    unbreakable?.proceed { itemStack.itemMeta = itemStack.itemMeta?.also { it.isUnbreakable = true } }
//    nbt?.let {
//        ItemTag().apply {
//            putAll(itemStack.getItemTag())
//            putAll(buildNBT { context.parse(it) })
//            saveTo(itemStack)
//        }
//    }
//}
//
//fun Frame.translateUpdate(session: Session, element: IconElement, defaultFrame: Frame) {
//
//    fun ItemStack.update(regenerated: Boolean): ItemStack {
//        val basedName = name ?: defaultFrame.name
//        val basedLore = lore ?: defaultFrame.lore
//        val context = element.context
//
//        if (basedName != null) postName(session.parse(basedName, context).colored())
//        if (!basedLore.isNullOrEmpty()) postLore(session.parse(basedLore, context).colored(enhancedLore))
//
//        if (staticDamage == null) damage?.let { durability = session.parse(it.content).cshort }
//        if (staticAmount == null) postAmount(session.parse(this@translateUpdate.amount?.contentOrNull ?: "1").cint)
//        if (staticCustomModelData == null) customModelData?.let { postModel(session.parse(it.content).cint) }
//        if (staticGlow == null) glow?.let { postShiny(session.parse(it.content).cbool) }
//
//        if (regenerated) afterRebasedTexture(this, context)
//        return this
//    }
//
//    if (texture == null || texture.isStatic()) {
//        element.itemStack = element.itemStack.update(false)
//    } else {
//        texture.generateItem(element.context) {
//            element.itemStack = update(true)
//        }
//    }
//}
//
//fun List<String>.colored(enhanceProcess: Boolean?): List<String> {
//    return if (enhanceProcess != true) {
//        map { it.colored() }
//    } else {
//        val iterator = iterator()
//
//        buildList {
//            while (iterator.hasNext()) {
//                val it = iterator.next()
//                if (it.contains("\\n")) this += it.split("\\n").map { it.colored() }
//                else this += it.colored()
//            }
//        }
//    }
//}