package cc.trixey.invero.core.script

/**
 * Invero
 * cc.trixey.invero.expansion.script.menu.PageOperat
 *
 * @author Arasple
 * @since 2023/1/19 20:34
 */
enum class PageOperator(val aliases: Set<String> = setOf()) {

    SET(setOf("set", "to", "switch")),

    GET(setOf("get", "current")),

    GET_MAX(setOf("max")),

    IS_FIRST_PAGE(setOf("isFirst")),

    IS_LAST_PAGE(setOf("isLast")),

    NEXT(setOf("next", "add", "increase", "+")),

    PREVIOUS(setOf("previous", "sub", "decrease", "-"));

    companion object {

        fun of(name: String): PageOperator {
            return values().find { it.aliases.any { ali -> ali.equals(name, true) } } ?: GET
        }

    }

}