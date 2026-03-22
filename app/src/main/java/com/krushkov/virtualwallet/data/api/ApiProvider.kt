package com.krushkov.virtualwallet.data.api

object ApiProvider {

    val authApi: AuthApi by lazy {
        RetrofitClient.create(AuthApi::class.java)
    }

    val walletApi: WalletApi by lazy {
        RetrofitClient.create(WalletApi::class.java)
    }


}