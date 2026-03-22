package com.krushkov.virtualwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.krushkov.virtualwallet.data.api.RetrofitClient
import com.krushkov.virtualwallet.ui.navigation.AppNavGraph
import com.krushkov.virtualwallet.viewmodel.AuthViewModel
import com.krushkov.virtualwallet.viewmodel.WalletViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.init(applicationContext)

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val walletViewModel: WalletViewModel = viewModel()

            AppNavGraph(authViewModel, walletViewModel)
        }
    }
}