package io.github.zeettn.tcommand

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class TCommand : JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        getCommand("t")?.setExecutor(TtCommandHandler(this))
        getCommand("addtp")?.setExecutor(AddTpCommandHandler(this))
        getCommand("rmtp")?.setExecutor(RmTpCommandHandler(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }



}

class TtCommandHandler(private val plugin: JavaPlugin): CommandExecutor {
    private var inventoryTool = InventoryTool(plugin)
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) {
            p0.sendMessage("플레이어만 사용할수 있습니다.")
            return true
        }
        inventoryTool.generateInventory(p0)
        return true
    }
}
               