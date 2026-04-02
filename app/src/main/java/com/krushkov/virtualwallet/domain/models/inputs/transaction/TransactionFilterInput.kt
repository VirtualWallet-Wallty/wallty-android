package com.krushkov.virtualwallet.domain.models.inputs.transaction

import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionStatus
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionFilterInput(
    val senderId: Long? = null,
    val recipientId: Long? = null,

    val senderWalletId: Long? = null,
    val recipientWalletId: Long? = null,

    val type: TransactionType? = null,
    val status: TransactionStatus? = null,

    val createdFrom: LocalDateTime? = null,
    val createdTo: LocalDateTime? = null,

    val minAmount: BigDecimal? = null,
    val maxAmount: BigDecimal? = null
)