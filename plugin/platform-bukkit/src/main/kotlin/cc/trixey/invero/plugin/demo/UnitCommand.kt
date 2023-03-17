package cc.trixey.invero.plugin.demo

import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand

/**
 * Invero
 * cc.trixey.invero.plugin.demo.UnitCommand
 *
 * @author Arasple
 * @since 2023/1/12 21:10
 */
@CommandHeader(name = "idemo", permission = "*")
object UnitCommand {

    @CommandBody
    val basic = construct { showBasic(this) }

    @CommandBody
    val basic_dynamicItem = construct { showRunningItem(this) }

    @CommandBody
    val basic_dynamicTitle = construct { showDynamicTitle(this) }

    @CommandBody
    val freeform_standard = construct { showFreeformStandard(this) }

    @CommandBody
    val freeform_gameoflife = construct { showTheGameOfLife(this) }

    @CommandBody
    val freeform_netesed = construct { showFreeformNetesed(this) }

    @CommandBody
    val scroll = construct { showScrollStandard(this) }

    @CommandBody
    val scroll_2 = construct { showScroll2(this) }

    @CommandBody
    val generator_paged = construct { showGeneratorPaged(this, it) }

    @CommandBody
    val generator_scroll = construct { showGeneratorScroll(this) }

    @CommandBody
    val IO_storage = construct { showIOStoragePanel(this) }

    @CommandBody
    val paged_netesed_paged = construct { showPagedNetesedPaged(this) }

    private fun construct(block: Player.(String?) -> Unit): SimpleCommandBody {
        return subCommand {
            execute { sender, _, args ->
                block(sender, args.split(" ").getOrNull(1))
            }
        }
    }

}
