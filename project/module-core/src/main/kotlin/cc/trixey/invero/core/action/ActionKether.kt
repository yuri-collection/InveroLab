package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.serialize.ActionKetherSerializer
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.bool
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.ActionKether
 *
 * @author Arasple
 * @since 2023/1/18 11:30
 */
@Serializable(with = ActionKetherSerializer::class)
class ActionKether(val script: String) : Action() {

    override fun run(context: Context): CompletableFuture<Boolean> {
        val player = context.player
        val variables = context.variables

        return KetherHandler.invoke(script, player, variables).thenApply { it.bool }
    }

}