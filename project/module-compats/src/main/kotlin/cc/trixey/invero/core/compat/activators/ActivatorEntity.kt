package cc.trixey.invero.core.compat.activators

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.MenuActivator
import cc.trixey.invero.core.compat.DefActivator
import cc.trixey.invero.ui.bukkit.util.format
import kotlinx.serialization.json.*
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion

/**
 * Invero
 * cc.trixey.invero.core.compat.activators.ActivatorEntity
 *
 * @author Arasple
 * @since 2023/2/25 17:01
 */
@DefActivator(["entity"])
class ActivatorEntity(private val types: List<String> = emptyList()) : MenuActivator<ActivatorEntity>() {

    companion object {

        @SubscribeEvent(ignoreCancelled = true)
        fun e(e: PlayerInteractEntityEvent) {
            // 避免高版本此事件触发两次
            if (MinecraftVersion.majorLegacy >= 10900 && e.hand == EquipmentSlot.OFF_HAND) return

            Invero.API.getRegistry().callActivator(e.player, "ENTITY", e.rightClicked)
        }

    }

    override fun call(player: Player, vararg params: Any): Boolean {
        val entity = params[0] as Entity
        val type = entity.type.name

        if (types.any { it.equals(type, true) }) {
            activate(player, buildMap {
                put("activator_entity", entity)
                put("activator_entity_uuid", entity.uniqueId)
                put("activator_entity_type", type)
                put("activator_entity_location_x", entity.location.x.format())
                put("activator_entity_location_y", entity.location.y.format())
                put("activator_entity_location_z", entity.location.z.format())
                put("activator_entity_name", entity.customName ?: entity.name)
            })
            return true
        }
        return false
    }

    override fun deserialize(element: JsonElement): ActivatorEntity {
        return when (element) {
            is JsonPrimitive -> ActivatorEntity(listOf(element.content))
            is JsonArray -> ActivatorEntity(element.map { it.jsonPrimitive.content })
            else -> error("Unsupported type $element")
        }
    }

    override fun serialize(activator: ActivatorEntity) = buildJsonArray {
        activator.types.forEach { add(it) }
    }

}