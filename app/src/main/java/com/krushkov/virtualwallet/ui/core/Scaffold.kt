package com.krushkov.virtualwallet.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.krushkov.virtualwallet.ui.theme.AppCardShape
import com.krushkov.virtualwallet.ui.theme.CloudWhite

@Composable
fun Scaffold(
    topContent: @Composable ColumnScope.() -> Unit = {},
    cardTitle: String? = null,
    showCardBackground: Boolean = false,
    cardContentScrollable: Boolean = true,
    cardContent: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        topContent()

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (showCardBackground) {
                GlassSurface(
                    modifier = Modifier.fillMaxSize(),
                    shape = AppCardShape
                ) {}
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (cardTitle != null) {
                    Text(
                        text = cardTitle,
                        color = CloudWhite,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .then(
                            if (cardContentScrollable) Modifier.verticalScroll(rememberScrollState())
                            else Modifier
                        )
                        .padding(horizontal = if (showCardBackground) 16.dp else 0.dp)
                ) {
                    cardContent()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
