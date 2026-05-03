package com.krushkov.virtualwallet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.krushkov.virtualwallet.ui.nav.AppNavGraph
import com.krushkov.virtualwallet.ui.theme.VirtualWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VirtualWalletTheme {
                AppNavGraph()
            }
        }
    }
}