package run.dn5.sasa.coins

import kotlinx.serialization.Serializable
import org.bukkit.Material

@Serializable
data class Coin(
    val id: Int,
    val name: String,
    val displayName: String,
    val value: Double,
    val material: Material,
    val owner: String,
    var issued: Int
) {
    val customModelData = "${Constants.COIN_CUSTOM_MODEL_DATA}$id".toInt()
    fun price(amount: Int): Double = value * amount
}
