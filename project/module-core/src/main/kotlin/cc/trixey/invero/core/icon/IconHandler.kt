package cc.trixey.invero.core.icon

import cc.trixey.invero.common.util.alert
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.action.Action
import cc.trixey.invero.ui.common.event.ClickType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.icon.IconHandler
 *
 * @author Arasple
 * @since 2023/1/18 11:14
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class IconHandler(@JsonNames("def") val default: Action?, private val typed: Map<ClickType, Action> = mapOf()) {

    fun run(context: Context, clickType: ClickType) {
        // 临时暂停动态标题
        context.session?.pauseAnimatedTitle(5L)
        alert {
            typed[clickType]?.run(context)
            if (clickType.isNumberKeyClick) typed[ClickType.NUMBER_KEY]?.run(context)
            default?.run(context)
        }
    }

}