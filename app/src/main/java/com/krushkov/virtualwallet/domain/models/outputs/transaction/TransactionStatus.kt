package com.krushkov.virtualwallet.domain.models.outputs.transaction

enum class TransactionStatus {
    PENDING,
    CONFIRMED,
    FAILED,
    REJECTED,
    UNKNOWN
}