package run.dn5.sasa.coins.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import run.dn5.sasa.coins.Coin
import run.dn5.sasa.coins.Constants
import run.dn5.sasa.coins.PaperPlugin
import run.dn5.sasa.coins.item.CoinItem

class Exchange(
    private val plugin: PaperPlugin
) : BaseCommand("exchange") {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        when (args.size) {
            1 -> return mutableListOf("toCoin", "fromCoin")
            2 -> return plugin.coinManager.coins.map { it.name }.toMutableList()
            3 -> return mutableListOf("[<amount>]")
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_ERR_NOT_PLAYER))
            return false
        }
        if (args.size < 3) {
            sendUsage(sender)
            return false
        }


        val coinName = args[1]
        val amount = args[2].toIntOrNull()

        if (amount == null || amount <= 0) {
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_EXCHANGE_ERR_AMOUNT_NOT_INT))
            return true
        }

        val coin = plugin.coinManager.getCoin(coinName)

        if (coin == null) {
            sender.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_ERR_COIN_NOT_FOUND))
            return false
        }

        if (args[0] == "toCoin") return toCoin(sender, coin, amount)
        else if (args[0] == "fromCoin") return fromCoin(sender, coin, amount)

        sendUsage(sender)
        return false
    }

    private fun toCoin(player: Player, coin: Coin, amount: Int): Boolean {
        val balance = plugin.economy.getBalance(player)
        val price = coin.price(amount)
        if (price > balance) {
            player.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_EXCHANGE_ERR_NOT_ENOUGH_MONEY))
            return false
        }
        if (amount > 1000){
            player.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_EXCHANGE_ERR_TO_MANY_COINS))
            return false
        }

        val stack = amount / 64
        val remain = amount - stack * 64

        for (i in 1..stack) {
            player.world.dropItem(player.location, CoinItem(coin, 64))
        }
        if (remain != 0) player.world.dropItem(player.location, CoinItem(coin, remain))

        plugin.economy.withdrawPlayer(player, price)
        plugin.coinManager.issue(coin.name, amount)

        player.sendMessage(
            ms.deserialize(
                Constants.MESSAGES.COMMAND_EXCHANGE_SUCCESS_TOCOIN(
                    amount,
                    price,
                    plugin.economy.currencyNamePlural()
                )
            )
        )
        return true
    }

    private fun fromCoin(player: Player, coin: Coin, amount: Int): Boolean {
        val price = coin.price(amount)
        val inv = player.inventory
        val coinStacks =
            inv.filter { it != null && it.hasItemMeta() && it.itemMeta.hasCustomModelData() && it.itemMeta.customModelData == coin.customModelData }
        var coinItemSize = 0
        coinStacks.forEach {
            coinItemSize += it.amount
        }

        if (coinItemSize < amount) {
            player.sendMessage(ms.deserialize(Constants.MESSAGES.COMMAND_EXCHANGE_ERR_NOT_ENOUGH_COIN))
            return false
        }

        var remain = amount


        coinStacks.forEach {
            if (remain >= it.amount) {
                remain -= it.amount
                inv.remove(it)
            } else {
                inv.setItem(inv.indexOf(it), CoinItem(coin, it.amount - remain))
                remain = 0
            }
        }

        plugin.economy.depositPlayer(player, price)
        plugin.coinManager.erase(coin.name, amount)

        player.sendMessage(
            ms.deserialize(
                Constants.MESSAGES.COMMAND_EXCHANGE_SUCCESS_FROMCOIN(
                    amount,
                    price,
                    plugin.economy.currencyNamePlural()
                )
            )
        )
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage(Constants.MESSAGES.COMMAND_EXCHANGE_USAGE)
    }
}