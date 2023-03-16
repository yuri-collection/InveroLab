package cc.trixey.invero.core.util

import cc.trixey.invero.common.adventure.parseMiniMessage
import cc.trixey.invero.common.adventure.parseMiniMessageAndSend
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.HexColor
import taboolib.module.chat.component
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.compat.replacePlaceholder

/**
 * Invero
 * cc.trixey.invero.common.util.TextDecoration
 *
 * @author Arasple
 * @since 2023/2/22 13:06
 */

/**
 * 常规文本格式替换
 * 依次翻译
 * - Kether Inline
 * - PlaceholderAPI
 * - MiniMessage Parse (if supported)
 * - TabooLib Colored
 */
fun String.translateFormattedMessage(player: Player, variables: Map<String, Any?> = emptyMap()) =
    KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .parseMiniMessage()
        .colored()

/**
 * （默认）发送格式化消息
 * 依次翻译
 * - Kether Inline
 * - Placeholder API
 * - TabooLib Colored
 * - MiniMessage Component Send (if supported) (else send noraml message)
 */
fun String.sendFormattedMiniMessageComponent(player: Player, variables: Map<String, Any> = emptyMap()) =
    KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .colored()
        .parseMiniMessageAndSend(player)

/**
 * 发送 TabooLib ComponentText
 * 依次翻译
 * - Kether Inline
 * - Placeholder API
 * - TabooLib Component (with colored)
 */
fun String.sendFormattedTabooComponent(player: Player, variables: Map<String, Any> = emptyMap()) =
    KetherHandler
        .parseInline(this, player, variables)
        .replacePlaceholder(player)
        .colored()
        .component()
        .build()
        .sendTo(adaptPlayer(player))

fun String.colored() =
    HexColor
        .translate(this)
        .let {
            if (patchDragonCore) {
                it.replace("&#", "§#")
            } else {
                it
            }
        }

private val patchDragonCore: Boolean = MinecraftVersion.majorLegacy in 11200 until 11300