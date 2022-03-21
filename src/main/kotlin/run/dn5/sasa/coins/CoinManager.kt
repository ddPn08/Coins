package run.dn5.sasa.coins

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.Player

class CoinManager(
    private val plugin: PaperPlugin
) {

    @Serializable
    data class YamlModel(val coins: List<Coin>)

    val coins = mutableListOf<Coin>()

    init {
        setupFile()
        setupCoins()
    }

    private fun setupFile() {
        val file = plugin.dataFolder.resolve("coins.yml")
        if (!file.exists()) {
            plugin.saveResource("coins.yml", false)
        }
    }

    private fun setupCoins(){
        val file = plugin.dataFolder.resolve("coins.yml")
        val yaml = Yaml.default.decodeFromStream(YamlModel.serializer(), file.inputStream())
        coins.addAll(yaml.coins)
    }

    private fun saveFile() {
        val file = plugin.dataFolder.resolve("coins.yml")
        Yaml.default.encodeToStream(YamlModel.serializer(), YamlModel(coins), file.outputStream())
    }

    fun getCoin(name: String): Coin? {
        return coins.find { it.name == name }
    }

    fun issue(name: String, amount: Int){
        val coin = getCoin(name)
        if(coin != null){
            coin.issued += amount
            saveFile()
        }
    }

    fun erase(name: String, amount: Int){
        val coin = getCoin(name)
        if(coin != null){
            coin.issued -= amount
            saveFile()
        }
    }

    fun createCoin(name: String, value: Double, miniName: String, material: Material, owner: Player) {
        val id = coins.size + 1
        val coin = Coin(id, name, miniName, value, material, owner.uniqueId.toString(), 0)
        coins.add(coin)
        saveFile()
    }

    fun deleteCoin(name: String){
        val coin = getCoin(name)
        if(coin != null){
            coins.remove(coin)
            saveFile()
        }
    }
}