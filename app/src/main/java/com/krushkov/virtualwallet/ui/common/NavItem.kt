package com.krushkov.virtualwallet.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.ui.graphics.vector.ImageVector
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.nav.Routes

object NavItem {

    val Home = Item(
        route = Routes.HOME,
        icon = Icons.Default.Home,
        labelRes = R.string.nav_home
    )

    val Cards = Item(
        route = Routes.CARDS,
        icon = Icons.Default.CreditCard,
        labelRes = R.string.nav_cards
    )

    val Transactions = Item(
        route = Routes.TRANSACTIONS,
        icon = Icons.Default.SwapVert,
        labelRes = R.string.nav_transactions
    )

    val Settings = Item(
        route = Routes.SETTINGS,
        icon = Icons.Default.Settings,
        labelRes = R.string.nav_settings
    )
}

data class Item(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
)
