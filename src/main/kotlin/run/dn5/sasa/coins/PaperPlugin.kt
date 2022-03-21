package run.dn5.sasa.coins

import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.java.JavaPlugin
import run.dn5.sasa.coins.command.Coins


class PaperPlugin : JavaPlugin() {

    lateinit var economy: Economy
    lateinit var coinManager: CoinManager

    override fun onEnable() {
        setupFiles()
        if (!setupEconomy()) {
            logger.warning("Vault not found.")
            server.pluginManager.disablePlugin(this)
            return
        }

        coinManager = CoinManager(this)

        this.setupCommands()
    }

    private fun setupFiles() {
        if (!dataFolder.exists()) dataFolder.mkdir()
    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        economy = rsp.provider
        return true
    }

    private fun setupCommands() {
        getCommand("coins")?.setExecutor(Coins(this))
    }
}