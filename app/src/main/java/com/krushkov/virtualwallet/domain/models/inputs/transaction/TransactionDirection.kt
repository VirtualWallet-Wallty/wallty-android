package com.krushkov.virtualwallet.domain.models.inputs.transaction

enum class TransactionDirection(val label: String) {
    ALL("All"),
    SENT("Sent"),
    RECEIVED("Received")
}
