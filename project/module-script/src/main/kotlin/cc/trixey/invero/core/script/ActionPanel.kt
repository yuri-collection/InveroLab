package cc.trixey.invero.core.script

import cc.trixey.invero.core.script.loader.InveroKetherParser
import cc.trixey.invero.ui.common.Panel
import taboolib.module.kether.*

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionPanel
 *
 * @author Arasple
 * @since 2023/1/19 21:20
 */
object ActionPanel {

    @InveroKetherParser(["panel"])
    fun parser() = scriptParser {
        val indexs = mutableListOf<Int>()

        while (it.hasNext()) {
            when (it.expects("at", "switch", "select")) {
                "at" -> indexs += it.nextInt()
                "switch", "select" -> {
                    return@scriptParser actionNow { variables().set("@panel", locatePanel(indexs)) }
                }
            }
        }

        actionNow { "INVALID_ACTION" }
    }

    /*
    panel select
     */
//    @InveroKetherParser(["panel"])
//    fun parser() = scriptParser {
//        val indexs = mutableListOf<Int>()
//
//
//        while (it.hasNext()) {
//            when (it.expects("at", "page", "icon")) {
//                "at" -> indexs += it.nextInt()
//                "page" -> {
//                    return@scriptParser actionNow { ActionPage.parserPage(locatePanel(indexs)).reader.invoke(it) }
//                }

//                "scroll" -> {
//                    return@scriptParser actionNow { ActionScroll.parserScroll(locatePanel(indexs)) }
//                }
//
//                "icon" -> {
//                    val parser = Kether.scriptRegistry.getParser("icon", "invero").get()
//                    parser.resolve<Any>(it)
//
//                    return@scriptParser actionNow {
//                        ActionIcon.parser(locatePanel(indexs)).reader.invoke(it)
//                    }
//                }
//            }
//        }
//        actionNow { "INVALID_ACTION" }
//    }

    private fun <T : Panel> ScriptFrame.locatePanel(indexs: List<Int>): T? {
        return if (indexs.isEmpty()) null else findPanelAt(indexs)
    }

}