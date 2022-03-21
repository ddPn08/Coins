package run.dn5.sasa.coins.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import run.dn5.sasa.coins.PaperPlugin

class Coins(
    plugin: PaperPlugin
) : BaseCommand("coins") {
    private val subCommands = listOf(
        Create(plugin),
        Exchange(plugin),
        Delete(plugin)
    )
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>? {
        if (args.size == 1) {
            return subCommands.map { it.name }
        }
        val subCommand = subCommands.find { it.name == args[0] } ?: return null
        return subCommand.onTabComplete(sender, command, label, args.drop(1).toTypedArray())
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return false
        }
        val subCommand = subCommands.find { it.name == args[0] }
        if (subCommand == null) {
            sendUsage(sender)
            return false
        }
        subCommand.onCommand(sender, command, label, args.drop(1).toTypedArray())
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage("/coins <${subCommands.joinToString(" ") { it.name }}>")
    }

}