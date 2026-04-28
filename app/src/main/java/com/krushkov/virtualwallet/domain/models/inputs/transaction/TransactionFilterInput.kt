package com.krushkov.virtualwallet.domain.models.inputs.transaction

import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionStatus
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionFilterInput(
    val label: String? = null,

    val senderId: Long? = null,
    val recipientId: Long? = null,

    val senderWalletId: Long? = null,
    val recipientWalletId: Long? = null,

    val type: TransactionType? = null,
    val status: TransactionStatus? = null,

    val senderCurrencyCode: String? = null,
    val recipientCurrencyCode: String? = null,

    val minSenderAmount: BigDecimal? = null,
    val maxSenderAmount: BigDecimal? = null,

    val minRecipientAmount: BigDecimal? = null,
    val maxRecipientAmount: BigDecimal? = null,

    val externalReference: String? = null,

    val createdFrom: LocalDateTime? = null,
    val createdTo: LocalDateTime? = null,
)