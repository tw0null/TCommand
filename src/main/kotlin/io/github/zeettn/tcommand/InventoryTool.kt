package io.github.zeettn.tcommand

import io.github.zeettn.invfx.InvFX.frame
import io.github.zeettn.invfx.openFrame
import loadAllDataFromYAML
import loadHomeLocationFromYAML
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import saveDataToYAML


class InventoryTool(private val plugin:JavaPlugin) {
    private var alldata :Map<String, Location> = mutableMapOf()
    fun getNearestPlayer(player: Player): Player? {
        val world = player.world
        val players = world.players.filter { it != player } // 자기 자신 제외

        return players.minByOrNull { it.location.distance(player.location) }
    }
    fun showTpInv(player: Player) {
        val tpframe = frame(3, Component.text("바로가기")) {
            list(0, 0, 8, 2, true, { alldata.toList() }) {
                transform { (name, _) ->
                    ItemStack(Material.ENDER_PEARL).apply {
                        editMeta { meta ->
                            meta.displayName(Component.text(name))
                        }
                    }
                }
                onClickItem { _, _, (data,_),e ->
                    val (name, location) = data
                    val player = e.whoClicked as Player
                    player.teleport(location)
                    player.sendMessage("$name 으로 텔레포트했습니다.")
                }
            }
        }
        player.openFrame(tpframe)
    }
    fun generateInventory(player: Player) {
        val invFrame = frame(3, Component.text("도구")){
            slot(0,1){
                item= ItemStack(Material.SKELETON_SKULL).apply {
                    editMeta { meta ->
                        meta.displayName(
                            Component.text().content("죽기").build()
                        )
                    }
                }
                onClick {clickEvent ->
                    val player  = clickEvent.whoClicked
                    player.health = 0.0
                }
            }
            slot(1,1){
                item= ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA).apply {
                    editMeta { meta ->
                        meta.displayName(
                            Component.text().content("가장 가까운 사람에게 텔레포트하기.").build()
                        )
                    }
                }
                onClick { clickEvent ->
                    val player  = clickEvent.whoClicked
                    val nearplayer = getNearestPlayer(player as Player)
                    if (nearplayer != null) {
                        player.teleport(nearplayer.location)
                    }
                        player.sendMessage("${player.name}으로 텔레포트함")
                }
            }
            slot(2,1){
                item= ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA).apply {
                    editMeta { meta ->
                        meta.displayName(
                            Component.text().content("가장 가까운 사람을 자신한테 텔레포트하기").build()
                        )
                    }
                }
                onClick { clickEvent ->
                    val player  = clickEvent.whoClicked
                    val nearplayer = getNearestPlayer(player as Player)
                    if (nearplayer != null) {
                        nearplayer.teleport(player)
                    }
                    player.sendMessage("${player.name}를 자신에게 텔레포트함")
                }
            }
            slot(3,1) {
                item= ItemStack(Material.RED_BANNER).apply {
                    editMeta { meta ->
                        meta.displayName(Component.text().content("집 설정").build())

                    }
                }
                onClick { e ->
                    val player  = e.whoClicked
                    saveDataToYAML(plugin, "home", player.location)
                }
            }
            slot(4,1) {
                item= ItemStack(Material.WHITE_BANNER).apply {
                    editMeta { meta ->
                        meta.displayName(Component.text().content("집으로 가기").build())
                    }
                }
                onClick { e ->
                    val player  = e.whoClicked
                    loadHomeLocationFromYAML(plugin)?.let { player.teleport(it) }
                }
            }
            slot(5, 1){
                item= ItemStack(Material.NETHER_STAR).apply {
                    editMeta { meta ->
                        meta.displayName(Component.text().content("바로가기들").build())
                    }
                }
                onClick {e->
                    alldata = loadAllDataFromYAML(plugin).filter { (key, _) -> key != "home" }
                    showTpInv(e.whoClicked as Player)
                }
            }


        }

        player.openFrame(invFrame)
    }


    }
