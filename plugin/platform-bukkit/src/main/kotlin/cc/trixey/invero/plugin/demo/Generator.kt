package cc.trixey.invero.plugin.demo

import cc.trixey.invero.bukkit.api.dsl.*
import cc.trixey.invero.bukkit.nms.updateTitle
import cc.trixey.invero.bukkit.util.randomMaterial
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.demo.Generator
 *
 * @author Arasple
 * @since 2023/1/11 16:16
 */
fun showGeneratorPaged(player: Player, filter: String? = null) = packetChestWindow(6, "Generator_Paged (filtered: $filter)") {

    generatorPaged<Sound>(9 to 6) {

        onPageChanging { _, toPage ->
            updateTitle("Generator_Paged ($toPage / $maxPageIndex)")
            forViewersInstance<Player> {
                it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
            }
        }

        generatorElements {
            Sound
                .values()
                .sortedBy { it.name }
                .filter { filter == null || it.name.contains(filter.uppercase()) }
        }

        onGenerate { sound ->

            buildItem(randomMaterial()) {
                modify {
                    name = sound.name
                }
                onClick {
                    player.sendMessage(sound.name)
                }
            }

        }

        pageController(this, -1, 0, Material.CYAN_STAINED_GLASS_PANE) { modify { name = "Preivous page" } }
        pageController(this, +1, 8, Material.LIME_STAINED_GLASS_PANE) { modify { name = "Next page" } }

    }

    open(player)

}

fun showGeneratorScroll(player: Player, filter: String? = null) = packetChestWindow(6, "Generator_Paged") {

    generatorScroll<Sound>(9 to 6) {

        generatorElements {
            Sound
                .values()
                .sortedBy { it.name }
                .filter { filter == null || it.name.contains(filter.uppercase()) }
        }

        onGenerate { sound ->

            buildItem(randomMaterial()) {
                modify {
                    name = sound.name
                }
                onClick {
                    player.sendMessage(sound.name)
                }
            }

        }

    }

    freeformNavigator()

    open(player)

}