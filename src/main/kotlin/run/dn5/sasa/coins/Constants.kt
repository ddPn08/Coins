package run.dn5.sasa.coins

import org.bukkit.Material

object Constants {
    const val COIN_CUSTOM_MODEL_DATA = "41150"
    val ALLOWED_MATERIAL_LIST = listOf(
        Material.DIAMOND,
        Material.EMERALD,
        Material.GOLD_INGOT,
        Material.IRON_INGOT,
    )
    const val PREFIX = "<green>[Coins]</green>"

    object MESSAGES {
        const val COMMAND_ERR_NOT_PLAYER = "$PREFIX <red>このコマンドはプレイヤーのみ実行可能です。"
        const val COMMAND_ERR_COIN_NOT_FOUND = "$PREFIX <red>コインが見つかりませんでした。"


        const val COMMAND_CREATE_USAGE = "$PREFIX <red>/coins create <name> <value> <material> <displayName>"
        const val COMMAND_CREATE_SUCCESS = "$PREFIX <aqua>物理コインの作成に成功しました。"
        const val COMMAND_CREATE_ERR_NOT_DOUBLE = "$PREFIX <red>価格には数値を入力してください。"
        const val COMMAND_CREATE_ERR_MATERIAL_NOT_ALLOWED = "$PREFIX <red>この物品は許可されていません。"
        const val COMMAND_CREATE_ERR_REACHED = "$PREFIX <red>物理コインの作成上限に達しました。"

        const val COMMAND_DELETE_USAGE = "$PREFIX <red>/coins delete <name>"
        const val COMMAND_DELETE_SUCCESS = "$PREFIX <aqua>物理コインの削除に成功しました。"
        const val COMMAND_DELETE_ERR_PERMISSION_DENIED = "$PREFIX <red>コインの削除は許可されていません。"
        const val COMMAND_DELETE_ERR_TOO_MANY_ISSUED = "$PREFIX <red>すでに1000枚以上のコインが発行されています。"

        const val COMMAND_EXCHANGE_USAGE = "$PREFIX <red>/coins exchange <toCoin, fromCoin> <name> <value>"
        const val COMMAND_EXCHANGE_ERR_AMOUNT_NOT_INT = "$PREFIX <red>数量には整数を入力してください。"
        const val COMMAND_EXCHANGE_ERR_NOT_ENOUGH_MONEY = "$PREFIX <red>所持金が足りません。"
        const val COMMAND_EXCHANGE_ERR_NOT_ENOUGH_COIN = "$PREFIX <red>コインが足りません。"
        const val COMMAND_EXCHANGE_ERR_TO_MANY_COINS = "$PREFIX <red>コインが多すぎます。"
        fun COMMAND_EXCHANGE_SUCCESS_TOCOIN(amount: Int, price: Double, currencyName: String) =
            "$PREFIX <aqua>${amount}個のコインを${price}${currencyName}で交換しました。"

        fun COMMAND_EXCHANGE_SUCCESS_FROMCOIN(amount: Int, price: Double, currencyName: String) =
            "$PREFIX <aqua>${amount}個のコインを${price}${currencyName}に交換しました。"

    }
}