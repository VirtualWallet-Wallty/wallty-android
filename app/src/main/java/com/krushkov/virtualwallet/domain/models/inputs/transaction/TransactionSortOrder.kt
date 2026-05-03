package com.krushkov.virtualwallet.domain.models.inputs.transaction

enum class TransactionSortOrder(val label: String, val apiValue: String) {
    NEWEST("Newest", "createdAt,desc"),
    OLDEST("Oldest", "createdAt,asc"),
    HIGHEST("Highest", "senderAmount,desc"),
    LOWEST("Lowest", "senderAmount,asc")
}
