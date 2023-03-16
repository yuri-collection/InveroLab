package cc.trixey.invero.core.compat.activators

import cc.trixey.invero.common.MenuActivator
import cc.trixey.invero.core.compat.DefActivator
import kotlinx.serialization.json.JsonElement

/**
 * Invero
 * cc.trixey.invero.core.compat.activators.ActivatorBrevis
 *
 * @author Arasple
 * @since 2023/2/26 14:04
 */
@DefActivator(["shiftf"])
class ActivatorBrevis(private val source: JsonElement) : MenuActivator<ActivatorBrevis>() {

    override fun deserialize(element: JsonElement) = ActivatorBrevis(element)

    override fun serialize(activator: ActivatorBrevis) = activator.source

}