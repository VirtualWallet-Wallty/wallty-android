package com.krushkov.virtualwallet.ui.features.transactions.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionDirection
import com.krushkov.virtualwallet.domain.models.inputs.transaction.TransactionSortOrder
import com.krushkov.virtualwallet.domain.models.outputs.card.Card
import com.krushkov.virtualwallet.domain.models.outputs.transaction.TransactionType
import com.krushkov.virtualwallet.domain.models.outputs.wallet.Wallet
import com.krushkov.virtualwallet.ui.core.DropdownField
import com.krushkov.virtualwallet.ui.theme.AppBorderStroke
import com.krushkov.virtualwallet.ui.theme.AppButtonShape
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow
import com.krushkov.virtualwallet.viewmodel.states.TransactionsState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val typeValues = listOf(null, TransactionType.TRANSFER, TransactionType.TOP_UP, TransactionType.PAYMENT)
private val directionValues = TransactionDirection.entries
private val sortValues = TransactionSortOrder.entries

@Composable
fun TransactionsFilterPanel(
    state: TransactionsState,
    activeFilterCount: Int,
    selectedWallet: Wallet?,
    selectedCard: Card?,
    onToggleExpanded: () -> Unit,
    onClearFilters: () -> Unit,
    onWalletSelected: (Long?) -> Unit,
    onCardSelected: (Long?) -> Unit,
    onDirectionSelected: (TransactionDirection) -> Unit,
    onTypeSelected: (TransactionType?) -> Unit,
    onSortSelected: (TransactionSortOrder) -> Unit,
    onDateFromSelected: (LocalDate?) -> Unit,
    onDateToSelected: (LocalDate?) -> Unit
) {
    var walletDropdownExpanded by remember { mutableStateOf(false) }
    var cardDropdownExpanded by remember { mutableStateOf(false) }
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    TransactionDatePicker(
        isVisible = showFromPicker,
        initialDate = state.filterDateFrom,
        onDismiss = { showFromPicker = false },
        onDateSelected = onDateFromSelected
    )
    TransactionDatePicker(
        isVisible = showToPicker,
        initialDate = state.filterDateTo,
        onDismiss = { showToPicker = false },
        onDateSelected = onDateToSelected
    )

    GlassFilterContainer {
        FilterHeader(
            isExpanded = state.isFilterExpanded,
            activeFilterCount = activeFilterCount,
            onToggleExpanded = onToggleExpanded,
            onClearFilters = onClearFilters
        )

        if (state.isFilterExpanded) {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = CloudWhite.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                WalletAndCardFilters(
                    state = state,
                    selectedWallet = selectedWallet,
                    selectedCard = selectedCard,
                    walletDropdownExpanded = walletDropdownExpanded,
                    cardDropdownExpanded = cardDropdownExpanded,
                    onWalletExpandedChange = { walletDropdownExpanded = it },
                    onCardExpandedChange = { cardDropdownExpanded = it },
                    onWalletSelected = {
                        walletDropdownExpanded = false
                        onWalletSelected(it)
                    },
                    onCardSelected = {
                        cardDropdownExpanded = false
                        onCardSelected(it)
                    }
                )

                FilterSection(label = stringResource(R.string.label_direction)) {
                    SegmentedPill(
                        options = directionLabels(),
                        selectedIndex = directionValues.indexOf(state.filterDirection),
                        onSelect = { onDirectionSelected(directionValues[it]) }
                    )
                }
                FilterSection(label = stringResource(R.string.label_type)) {
                    SegmentedPill(
                        options = typeLabels(),
                        selectedIndex = typeValues.indexOf(state.filterType).coerceAtLeast(0),
                        onSelect = { onTypeSelected(typeValues[it]) }
                    )
                }
                FilterSection(label = stringResource(R.string.label_sort)) {
                    SegmentedPill(
                        options = sortLabels(),
                        selectedIndex = sortValues.indexOf(state.sortOrder),
                        onSelect = { onSortSelected(sortValues[it]) }
                    )
                }

                DateFilterRow(
                    from = state.filterDateFrom,
                    to = state.filterDateTo,
                    onFromPickerOpen = { showFromPicker = true },
                    onToPickerOpen = { showToPicker = true },
                    onDateFromSelected = onDateFromSelected,
                    onDateToSelected = onDateToSelected
                )
            }
        }
    }
}

@Composable
private fun GlassFilterContainer(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .outerShadow(AppButtonShape)
            .clip(AppButtonShape)
            .animateContentSize(animationSpec = tween(300))
    ) {
        Box(modifier = Modifier.matchParentSize().innerShadow(AppButtonShape)) {
            Box(modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.45f)))
        }
        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, AppButtonShape))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            content = content
        )
    }
}

@Composable
private fun FilterHeader(
    isExpanded: Boolean,
    activeFilterCount: Int,
    onToggleExpanded: () -> Unit,
    onClearFilters: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onToggleExpanded() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = CloudWhite,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(R.string.label_filters),
                color = CloudWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (activeFilterCount > 0) FilterCountBadge(activeFilterCount)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = CloudWhite.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
        if (activeFilterCount > 0) ClearFiltersButton(onClick = onClearFilters)
    }
}

@Composable
private fun FilterCountBadge(count: Int) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .outerShadow(CircleShape)
            .clip(CircleShape)
            .innerShadow(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize().background(CyanNeon.copy(alpha = 0.24f)))
        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, CircleShape))
        Text(text = "$count", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ClearFiltersButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .outerShadow(CircleShape)
            .clip(CircleShape)
            .innerShadow(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize().background(Red.copy(alpha = 0.16f)))
        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, CircleShape))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.action_clear_filters),
            tint = Red,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun WalletAndCardFilters(
    state: TransactionsState,
    selectedWallet: Wallet?,
    selectedCard: Card?,
    walletDropdownExpanded: Boolean,
    cardDropdownExpanded: Boolean,
    onWalletExpandedChange: (Boolean) -> Unit,
    onCardExpandedChange: (Boolean) -> Unit,
    onWalletSelected: (Long?) -> Unit,
    onCardSelected: (Long?) -> Unit
) {
    val showWalletDropdown = state.wallets.isNotEmpty()
    val showCardDropdown = state.cards.size > 1
    if (!showWalletDropdown && !showCardDropdown) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showWalletDropdown) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterLabel(stringResource(R.string.label_wallet))
                DropdownField(
                    value = selectedWallet?.name ?: "",
                    placeholder = stringResource(R.string.action_all_wallets),
                    expanded = walletDropdownExpanded,
                    onExpandedChange = onWalletExpandedChange
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(R.string.action_all_wallets),
                                color = if (state.currentWalletId == null) CyanNeon else CloudWhite
                            )
                        },
                        onClick = { onWalletSelected(null) }
                    )
                    state.wallets.forEach { wallet ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    wallet.name,
                                    color = if (wallet.id == state.currentWalletId) CyanNeon else CloudWhite
                                )
                            },
                            onClick = { onWalletSelected(wallet.id) }
                        )
                    }
                }
            }
        }

        if (showCardDropdown) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterLabel(stringResource(R.string.label_card))
                DropdownField(
                    value = selectedCard?.let { "**** ${it.cardSuffix}" } ?: "",
                    placeholder = stringResource(R.string.label_all_cards),
                    expanded = cardDropdownExpanded,
                    onExpandedChange = onCardExpandedChange
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(R.string.label_all_cards),
                                color = if (state.filterCardId == null) CyanNeon else CloudWhite
                            )
                        },
                        onClick = { onCardSelected(null) }
                    )
                    state.cards.forEach { card ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "**** ${card.cardSuffix}",
                                    color = if (card.id == state.filterCardId) CyanNeon else CloudWhite
                                )
                            },
                            onClick = { onCardSelected(card.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun typeLabels() = listOf(
    stringResource(R.string.label_type_all),
    stringResource(R.string.label_type_transfer),
    stringResource(R.string.label_type_topup),
    stringResource(R.string.label_type_payment)
)

@Composable
private fun directionLabels() = listOf(
    stringResource(R.string.label_direction_all),
    stringResource(R.string.label_direction_sent),
    stringResource(R.string.label_direction_received)
)

@Composable
private fun sortLabels() = listOf(
    stringResource(R.string.label_sort_newest),
    stringResource(R.string.label_sort_oldest),
    stringResource(R.string.label_sort_highest),
    stringResource(R.string.label_sort_lowest)
)
