package run.dn5.sasa.coins.command

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import run.dn5.sasa.coins.Constants
import run.dn5.sasa.coins.PaperPlugin
import java.util.*

class Create(
    private val plugin: PaperPlugin
) : BaseCommand("create") {


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        when (args.size) {
            1 -> return mutableListOf("[<name>]")
            2 -> return mutableListOf("[<value>]")
            3 -> return Constants.ALLOWED_MATERIAL_LIST.map { it.name }.toMutableList()
            4 -> return mutableListOf("[<displayName>]")
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_ERR_NOT_PLAYER))
            return false
        }
        if (args.size < 4) {
            sendUsage(sender)
            return false
        }

        val ownerCoins = plugin.coinManager.coins.filter { UUID.fromString(it.owner) == sender.uniqueId }

        if(ownerCoins.size > 5){
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_CREATE_ERR_REACHED))
            return false
        }

        val name = args[0]
        val value = args[1].toDoubleOrNull()
        if ((value == null) || (value <= 0)) {
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_CREATE_ERR_NOT_DOUBLE))
            return false
        }
        val material =
            if (Constants.ALLOWED_MATERIAL_LIST.none { it.name == args[2] }) {
                sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_CREATE_ERR_MATERIAL_NOT_ALLOWED))
                return false
            } else Material.valueOf(args[2])

        val displayName = if (args.size == 4) args[3] else "<aqua>$name"


        plugin.coinManager.createCoin(name, value, displayName, material, sender)

        sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_CREATE_SUCCESS))
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_CREATE_USAGE))
    }

}