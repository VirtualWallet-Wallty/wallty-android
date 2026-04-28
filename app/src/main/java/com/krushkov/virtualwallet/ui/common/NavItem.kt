package com.krushkov.virtualwallet.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.ui.graphics.vector.ImageVector
import com.krushkov.virtualwallet.ui.nav.Routes

object NavItem {

    val Home = Item(
        route = Routes.HOME,
        icon = Icons.Default.Home,
        label = "Home"
    )

    val Cards = Item(
        route = Routes.CARDS,
        icon = Icons.Default.CreditCard,
        label = "Cards"
    )

    val Transactions = Item(
        route = Routes.TRANSACTIONS,
        icon = Icons.Default.SwapVert,
        label = "Transactions"
    )

    val Settings = Item(
        route = Routes.SETTINGS,
        icon = Icons.Default.Settings,
        label = "Settings"
    )
}
data class Item(
    val route: String,
    val icon: ImageVector,
    val label: String
)