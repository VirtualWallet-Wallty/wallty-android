package com.krushkov.virtualwallet.viewmodel.states

import com.krushkov.virtualwallet.domain.models.outputs.user.UserProfile
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet

data class TransferState(
    val wallets: List<Wallet> = emptyList(),
    val selectedWallet: Wallet? = null,
    val currencySymbol: String = "",
    val isWalletDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false,
    // Send flow
    val scannedRecipientId: Long? = null,
    val recipientProfile: UserProfile? = null,
    val isLoadingRecipient: Boolean = false,
    val sendAmount: String = "",
    val navigateToConfirm: Boolean = false,
    val isSendLoading: Boolean = false,
    val isTransferSuccess: Boolean = false
)
