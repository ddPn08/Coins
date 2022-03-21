package run.dn5.sasa.coins.item

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import run.dn5.sasa.coins.Coin

class CoinItem(
    id: Int,
    displayName: Component,
    material: Material,
    amount: Int
) : ItemStack(material, amount) {
    constructor(coin: Coin, amount: Int) : this(
        coin.customModelData,
        MiniMessage.miniMessage().deserialize(coin.displayName),
        coin.material,
        amount
    )

    init {
        val meta = itemMeta
        meta.setCustomModelData(id)
        meta.displayName(displayName)
        meta.addEnchant(Enchantment.OXYGEN, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemMeta = meta
    }
}