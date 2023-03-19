package cc.trixey.invero.common.util

import kotlinx.serialization.json.*

/**
 * Invero
 * cc.trixey.invero.common.util.Json
 *
 * @author Arasple
 * @since 2023/2/18 13:55
 */
val prettyJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

val standardJson = Json

fun JsonElement.reduceEmpty(): JsonElement? {
    return when (this) {
        is JsonArray -> {
            JsonArray(toMutableList().mapNotNull { it.reduceEmpty() })
        }

        is JsonObject -> {
            JsonObject(toMutableMap().filterNot { it.value.isEmpty() }.toMap())
        }

        else -> if (isEmpty()) return null else this
    }
}

fun JsonElement.isEmpty(): Boolean {
    return when (this) {
        is JsonArray -> all { it.isEmpty() }
        is JsonObject -> values.all { it.isEmpty() }
        is JsonPrimitive -> false
        JsonNull -> true
    }
}