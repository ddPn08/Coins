package run.dn5.sasa.coins.command

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.TabExecutor

abstract class BaseCommand(
    val name: String
): TabExecutor {
    val ms = MiniMessage.miniMessage()
}