package com.krushkov.virtualwallet.ui.features.transfer

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.theme.AppCardShape
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.viewmodel.TransferViewModel
import androidx.core.graphics.set
import androidx.core.graphics.createBitmap

@Composable
fun ReceiveScreen(
    navController: NavController,
    viewModel: TransferViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            CircleButton(
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = CloudWhite
                    )
                },
                onClick = { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = "Receive Money",
            color = CloudWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Show this QR code to the sender",
            color = CloudWhite.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        val qrContent = state.selectedWallet?.let { wallet ->
            val ownerId = wallet.ownerId ?: wallet.owner?.id
            if (ownerId != null) "WALLTY_TRANSFER|$ownerId" else null
        }

        if (qrContent != null) {
            val qrBitmap = remember(qrContent) { generateQrBitmap(qrContent, 512) }
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(AppCardShape)
                    .background(androidx.compose.ui.graphics.Color.White)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    filterQuality = FilterQuality.None
                )
            }
        } else if (state.wallets.isEmpty() && !state.isLoading) {
            Text(
                text = "No wallets available",
                color = CloudWhite.copy(alpha = 0.5f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        } else if (state.selectedWallet?.ownerId == null && state.selectedWallet?.owner == null) {
            Text(
                text = "Unable to generate QR — wallet owner info unavailable",
                color = CloudWhite.copy(alpha = 0.5f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

private fun generateQrBitmap(content: String, size: Int): Bitmap {
    val hints = mapOf(EncodeHintType.MARGIN to 1)

    val bitMatrix = MultiFormatWriter().encode(
        content, BarcodeFormat.QR_CODE, size, size, hints
    )

    val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap[x, y] =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bitmap
}
