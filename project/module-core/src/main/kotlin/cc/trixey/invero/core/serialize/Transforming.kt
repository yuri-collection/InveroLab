package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.BaseMenu
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.icon.IconHandler
import cc.trixey.invero.core.item.Frame
import cc.trixey.invero.core.menu.NodeRunnable
import cc.trixey.invero.ui.common.event.ClickType
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

/**
 * Invero
 * cc.trixey.invero.core.serialize.Transforming
 *
 * @author Arasple
 * @since 2023/1/29 13:43
 */
object NodeSerializer : JsonTransformingSerializer<NodeRunnable>(serializer()) {

    override fun transformDeserialize(element: JsonElement) = when (element) {
        is JsonPrimitive -> buildJsonObject {
            put("type", "CONST")
            put("value", element.jsonPrimitive.content)
        }

        is JsonObject -> element
        is JsonArray -> error("NodeRunnable can not be JsonArray")
    }

}


object BaseMenuSerializer : JsonTransformingSerializer<BaseMenu>(serializer()) {

    private val menuKeys = arrayOf(
        "title",
        "rows",
        "virtual",
        "type",
        "override-player-inventory",
        "hide-player-inventory"
    )

    private val panelKeys = arrayOf(
        "layout",
        "locate",
        "scale",
        "pages",
        "generator",
        "scroll",
        "crafting",
        "items", "icons", "item", "icon"
    )

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()

        if ("menu" !in struc.keys) {
            struc["menu"] = buildJsonObject {
                menuKeys.forEach { key ->
                    struc[key]?.let { value -> put(key, value) }
                }
            }
        }

        if (
            arrayOf("panel", "panels").none { it in struc.keys }
            &&
            arrayOf("layout", "scale", "rows").any { it in struc.keys }
        ) {
            struc["panel"] = buildJsonObject {
                panelKeys.forEach { key ->
                    struc[key]?.let { value -> put(key, value) }
                }
            }
        }

        return JsonObject(struc)
    }

}

internal object FrameSerializer : JsonTransformingSerializer<Frame>(serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()

        if ("texture" !in struc.keys) {
            val texture = buildJsonObject {
                textureKeysHead.forEach { key ->
                    struc[key]?.let {
                        put("head", it)
                        return@buildJsonObject
                    }
                }
                textureSourcedKeys.forEach { key ->
                    struc[key]?.let {
                        put("source", key)
                        put("value", it)
                        return@buildJsonObject
                    }
                }
            }
            if (!texture.isEmpty()) struc["texture"] = texture
        }

        return JsonObject(struc)
    }

}

internal object IconSerializer : JsonTransformingSerializer<Icon>(serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()

        if ("display" !in struc.keys) {
            var textureSpecified = false
            val texture = buildJsonObject {
                // head
                val head = textureKeysHead.any { key ->
                    val value = struc[key]
                    value?.let { put("head", value) }
                    value != null
                }
                // sourced
                val source = textureSourcedKeys.any { key ->
                    val value = struc[key]
                    value?.let {
                        put("source", key)
                        put("value", value)
                    }
                    value != null
                }
                textureSpecified = head || source
            }
            struc["display"] = buildJsonObject {
                displayKeys.forEach { key -> struc[key]?.let { value -> put(key, value) } }
                if (textureSpecified) put("texture", texture)
            }
        }
        return JsonObject(struc)
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        val struc = element.jsonObject.toMutableMap()
        struc["display"]!!.jsonObject.forEach { key, value ->
            struc[key] = value
        }
        struc.remove("display")
        return JsonObject(struc)
    }

}

internal object IconHandlerSerializer : JsonTransformingSerializer<IconHandler>(serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonPrimitive, is JsonArray -> {
                buildJsonObject { put("def", element) }
            }

            is JsonObject -> {
                val jsonObject = element.jsonObject
                // 结构动作关键词检测
                if (SelectorAction.structuredKeys.any { it in jsonObject }) {
                    return buildJsonObject { put("def", jsonObject) }
                }
                // 非结构动作
                buildJsonObject {
                    // typed - def
                    jsonObject["def"]?.let { put("def", it) }
                    jsonObject["default"]?.let { put("def", it) }
                    // typed - others
                    buildJsonObject {
                        jsonObject
                            .keys
                            .filterNot { it == "def" || it == "default" }
                            .forEach { key ->
                                val action = jsonObject[key]
                                if (action != null)
                                    key
                                        .split(",")
                                        .mapNotNull { ClickType.find(it) }
                                        .forEach {
                                            put(it.name, action)
                                        }
                            }
                    }.let { put("typed", it) }
                }
            }
        }
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        val all = element.jsonObject["default"]
        val specificed = element.jsonObject["typed"]

        return if ((all is JsonPrimitive || all is JsonArray) && specificed?.jsonObject.isNullOrEmpty()) all else element
    }

}