package com.krushkov.virtualwallet.viewmodel

import androidx.lifecycle.*
import com.krushkov.virtualwallet.data.api.ApiProvider
import com.krushkov.virtualwallet.data.model.wallet.WalletLongResponse
import com.krushkov.virtualwallet.data.model.wallet.WalletShortResponse
import kotlinx.coroutines.launch

class WalletViewModel : ViewModel() {

    private val _wallets = MutableLiveData<List<WalletShortResponse>>()
    val wallets: LiveData<List<WalletShortResponse>> = _wallets

    private val _selectedWallet = MutableLiveData<WalletLongResponse>()
    val selectedWallet: LiveData<WalletLongResponse> = _selectedWallet

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadWallets() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = ApiProvider.walletApi.getMyWallets()

                if (response.isSuccessful) {
                    _wallets.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to load wallets"
                }

            } catch (e: Exception) {
                _error.value = e.message
            }

            _isLoading.value = false
        }
    }

    fun loadWalletDetails(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = ApiProvider.walletApi.getWalletById(id)

                if (response.isSuccessful) {
                    val data = response.body()?.data

                    if (data != null) {
                        _selectedWallet.value = data
                    } else {
                        _error.value = "Wallet data is empty"
                    }

                } else {
                    _error.value = "Failed to load wallet"
                }

            } catch (e: Exception) {
                _error.value = e.message
            }

            _isLoading.value = false
        }
    }
}