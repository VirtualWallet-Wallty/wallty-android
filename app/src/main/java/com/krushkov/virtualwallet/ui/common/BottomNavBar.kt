package com.krushkov.virtualwallet.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow
@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        NavItem.Home,
        NavItem.Cards,
        NavItem.Transactions,
        NavItem.Settings
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(64.dp)
    ) {
        // Glass Background layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .outerShadow(AppBottomNavShape)
                .clip(AppBottomNavShape)
                .innerShadow(AppBottomNavShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CyanNeon.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(AppBorderStroke, AppBottomNavShape)
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(26.dp),
                        tint = if (isSelected) CyanNeon else CloudWhite
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        color = if (isSelected) CyanNeon else CloudWhite
                    )
                }
            }
        }
    }
}
