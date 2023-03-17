package cc.trixey.invero.plugin.demo

import cc.trixey.invero.ui.bukkit.api.dsl.*
import cc.trixey.invero.ui.bukkit.util.randomMaterial
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.plugin.demo.PagedNetesed
 *
 * @author Arasple
 * @since 2023/3/17 20:11
 */
fun showPagedNetesedPaged(player: Player) = chestWindow(player.viewer, 6, "Paged_Netesed_Paged") {

    pagedNetesed(9 to 6) {
        repeat(3) {
            standard {
                buildItem(randomMaterial()).fillup()
            }
        }

        pagedNetesed {
            val pagedPanel = this
            repeat(3) {
                standard {
                    pageController(pagedPanel, -1, 0, Material.CYAN_STAINED_GLASS_PANE) { modify { name = "Preivous page" } }
                    pageController(pagedPanel, +1, 8, Material.LIME_STAINED_GLASS_PANE) { modify { name = "Next page" } }
                    buildItem(randomMaterial()).fillup()
                }
            }
        }
    }

    standard(9 to 1) {
        pageController(firstPagedPanel(), -1, 0, Material.ARROW) { modify { name = "Preivous page" } }
        pageController(firstPagedPanel(), +1, 8, Material.ARROW) { modify { name = "Next page" } }
    }

    open()

}