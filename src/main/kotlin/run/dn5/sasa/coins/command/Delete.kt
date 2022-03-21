package run.dn5.sasa.coins.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import run.dn5.sasa.coins.Constants
import run.dn5.sasa.coins.PaperPlugin
import java.util.*

class Delete(
    private val plugin: PaperPlugin
): BaseCommand("delete") {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>? {
        if(args.size == 1){
            return plugin.coinManager.coins.map { it.name }
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.size != 1){
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_DELETE_USAGE))
            return false
        }
        if(sender !is Player){
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_ERR_NOT_PLAYER))
            return false
        }

        val coin = plugin.coinManager.getCoin(args[0])
        if(coin == null){
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_ERR_COIN_NOT_FOUND))
            return false
        }
        if(UUID.fromString(coin.owner) != sender.uniqueId) {
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_DELETE_ERR_PERMISSION_DENIED))
            return false
        }

        if(coin.issued > 1000){
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_DELETE_ERR_TOO_MANY_ISSUED))
            return false
        }

        plugin.coinManager.deleteCoin(coin.name)
        sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_DELETE_SUCCESS))
        return true
    }

}