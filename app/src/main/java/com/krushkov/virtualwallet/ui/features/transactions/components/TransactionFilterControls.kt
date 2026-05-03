package com.krushkov.virtualwallet.ui.features.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.theme.AppBorderStroke
import com.krushkov.virtualwallet.ui.theme.AppButtonShape
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun DateFilterRow(
    from: LocalDate?,
    to: LocalDate?,
    onFromPickerOpen: () -> Unit,
    onToPickerOpen: () -> Unit,
    onDateFromSelected: (LocalDate?) -> Unit,
    onDateToSelected: (LocalDate?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterLabel(stringResource(R.string.label_from))
            DateButton(date = from, onPickerOpen = onFromPickerOpen, onClear = { onDateFromSelected(null) })
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterLabel(stringResource(R.string.label_to))
            DateButton(date = to, onPickerOpen = onToPickerOpen, onClear = { onDateToSelected(null) })
        }
    }
}

@Composable
fun FilterSection(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FilterLabel(label)
        content()
    }
}

@Composable
fun FilterLabel(label: String) {
    Text(text = label, color = CloudWhite.copy(alpha = 0.4f), fontSize = 11.sp)
}

@Composable
fun SegmentedPill(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
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
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun DateButton(
    date: LocalDate?,
    onPickerOpen: () -> Unit,
    onClear: () -> Unit
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }
    val selected = date != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .outerShadow(AppButtonShape)
            .clip(AppButtonShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onPickerOpen
            )
    ) {
        Box(modifier = Modifier.matchParentSize().innerShadow(AppButtonShape)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (selected) CyanNeon.copy(alpha = 0.2f) else Black.copy(alpha = 0.4f))
            )
        }
        Box(modifier = Modifier.matchParentSize().border(AppBorderStroke, AppButtonShape))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date?.format(dateFormatter) ?: stringResource(R.string.label_any_date),
                color = if (selected) CyanNeon else CloudWhite.copy(alpha = 0.55f),
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = CyanNeon.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClear
                        )
                )
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TransactionDatePicker(
    isVisible: Boolean,
    initialDate: LocalDate?,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    if (!isVisible) return

    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            ?.atStartOfDay(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    }
                    onDismiss()
                }
            ) { Text(stringResource(R.string.action_ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
        }
    ) {
        DatePicker(state = pickerState)
    }
}
