package com.krushkov.virtualwallet.ui.features.cards.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.krushkov.virtualwallet.ui.theme.AppBorderStroke
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.DigitBoxShape
import com.krushkov.virtualwallet.ui.utils.innerShadow
import com.krushkov.virtualwallet.ui.utils.outerShadow

private val digitBoxShape = DigitBoxShape
private val activeBorderStroke = BorderStroke(1.dp, SolidColor(CyanNeon))

@Composable
fun CardNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusRequester.requestFocus() }
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it.filter { c -> c.isDigit() }.take(16)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester)
                .alpha(0f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(4) { group ->
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(4) { pos ->
                        val index = group * 4 + pos
                        val char = value.getOrNull(index)
                        val isCurrent = index == value.length

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .outerShadow(digitBoxShape)
                                .clip(digitBoxShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .innerShadow(digitBoxShape)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Black.copy(alpha = 0.5f))
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .border(
                                        if (isCurrent) activeBorderStroke else AppBorderStroke,
                                        digitBoxShape
                                    )
                            )
                            Text(
                                text = char?.toString() ?: "",
                                color = CloudWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
