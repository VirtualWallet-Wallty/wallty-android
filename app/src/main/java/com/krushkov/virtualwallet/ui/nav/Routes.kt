package com.krushkov.virtualwallet.ui.nav

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CARDS = "cards"
    const val TRANSACTIONS = "transactions"
    const val TRANSACTIONS_FULL = "transactions?walletId={walletId}&type={type}&cardId={cardId}&label={label}"
    const val TRANSACTION_DETAILS = "transaction_details/{transactionId}"
    const val SETTINGS = "settings"
    const val CREATE_WALLET = "create_wallet"
    const val ADD_CARD = "add_card"
    const val TRANSFER = "transfer"
    const val RECEIVE = "receive"
    const val SEND_FLOW = "send_flow"
    const val SEND = "send"
    const val SEND_CONFIRM = "send_confirm"
    const val TOP_UP = "top_up/{mode}/{id}"
    const val MOVE = "move/{walletId}"
    const val PAYMENT_CONFIRM = "payment_confirm"
    const val POS_TERMINAL = "pos_terminal"

    fun transactionDetails(transactionId: Long) = "transaction_details/$transactionId"
}
