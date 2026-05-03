package com.krushkov.virtualwallet.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.Scaffold
import com.krushkov.virtualwallet.ui.theme.*
import com.krushkov.virtualwallet.ui.utils.LanguageManager
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow
import com.krushkov.virtualwallet.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var currentLang by remember { mutableStateOf(LanguageManager.getCurrentLanguage()) }

    val langCodes = listOf(LanguageManager.LANGUAGE_EN, LanguageManager.LANGUAGE_BG)
    val langLabels = listOf(
        stringResource(R.string.label_english),
        stringResource(R.string.label_bulgarian)
    )
    val selectedIndex = langCodes.indexOf(currentLang).coerceAtLeast(0)

    Scaffold(
        cardTitle = stringResource(R.string.title_settings),
        cardContent = {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.label_language),
                        color = CloudWhite.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    LanguagePill(
                        options = langLabels,
                        selectedIndex = selectedIndex,
                        onSelect = { index ->
                            val code = langCodes[index]
                            currentLang = code
                            LanguageManager.setLanguage(code)
                        }
                    )
                }

                Button(
                    text = stringResource(R.string.action_logout),
                    onClick = { authViewModel.logout() },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Red.copy(alpha = 0.5f)
                )
            }
        }
    )
}

@Composable
private fun LanguagePill(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .outerShadow(AppButtonShape)
            .clip(AppButtonShape)
    ) {
        Box(modifier = Modifier.matchParentSize().innerShadow(AppButtonShape)) {
            Box(modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.4f)))
        }

        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, AppButtonShape))

        Row(modifier = Modifier.fillMaxSize()) {
            options.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(3.dp)
                                .outerShadow(AppButtonShape)
                                .clip(AppButtonShape)
                                .innerShadow(AppButtonShape)
                        ) {
                            Box(modifier = Modifier.matchParentSize().background(CyanNeon.copy(alpha = 0.24f)))
                            Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, AppButtonShape))
                        }
                    }
                    Text(
                        text = label,
                        color = if (isSelected) CyanNeon else CloudWhite.copy(alpha = 0.55f),
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
