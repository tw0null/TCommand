package io.github.zeettn.wjplugin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import removeDataFromYAML

class RmTpCommandHandler(private val plugin: JavaPlugin) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("삭제할 위치 이름을 입력하세요. 예: /rmtp <이름>")
            return true
        }

        val key = args[0] // 첫 번째 인자를 삭제할 키로 사용
        removeDataFromYAML(plugin, key)
        sender.sendMessage("텔레포트 위치 '$key'가 삭제되었습니다.")

        return true
    }
}