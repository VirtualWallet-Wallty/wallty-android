package com.krushkov.virtualwallet.ui.features.transfer

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import androidx.compose.ui.res.stringResource
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.theme.AppCardShape
import com.krushkov.virtualwallet.ui.theme.Black
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.viewmodel.TransferViewModel
import kotlin.math.min

private enum class TransferTab { RECEIVE, SEND }

@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: TransferViewModel
) {
    val state = viewModel.state
    var selectedTab by remember { mutableStateOf(TransferTab.RECEIVE) }

    LaunchedEffect(state.navigateToConfirm) {
        if (state.navigateToConfirm) {
            viewModel.onNavigateToConfirmHandled()
            navController.navigate(Routes.SEND_CONFIRM)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircleButton(
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = CloudWhite
                    )
                },
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    text = stringResource(R.string.action_receive),
                    onClick = { selectedTab = TransferTab.RECEIVE },
                    modifier = Modifier.width(100.dp),
                    containerColor = if (selectedTab == TransferTab.RECEIVE)
                        Green.copy(alpha = 0.5f) else Black.copy(alpha = 0.4f)
                )
                Button(
                    text = stringResource(R.string.action_send),
                    onClick = { selectedTab = TransferTab.SEND },
                    modifier = Modifier.width(100.dp),
                    containerColor = if (selectedTab == TransferTab.SEND)
                        CyanNeon.copy(alpha = 0.5f) else Black.copy(alpha = 0.4f)
                )
            }
        }

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) +
                    fadeIn(tween(300)) togetherWith
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) +
                    fadeOut(tween(300))
                } else {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) +
                    fadeIn(tween(300)) togetherWith
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) +
                    fadeOut(tween(300))
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = "TransferTabContent"
        ) { tab ->
            when (tab) {
                TransferTab.RECEIVE -> ReceiveContent(viewModel)
                TransferTab.SEND -> SendContent(viewModel)
            }
        }
    }
}

@Composable
private fun ReceiveContent(viewModel: TransferViewModel) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.title_receive_money),
            color = CloudWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.subtitle_receive_money),
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
                    .background(Color.White)
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
                text = stringResource(R.string.msg_no_wallets),
                color = CloudWhite.copy(alpha = 0.5f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        } else if (state.selectedWallet?.ownerId == null && state.selectedWallet?.owner == null && !state.isLoading) {
            Text(
                text = stringResource(R.string.msg_qr_unavailable),
                color = CloudWhite.copy(alpha = 0.5f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun SendContent(viewModel: TransferViewModel) {
    val state = viewModel.state
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    val cameraHolder = remember { object { var provider: ProcessCameraProvider? = null } }

    DisposableEffect(lifecycleOwner) {
        onDispose { cameraHolder.provider?.unbindAll() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)
                    val future = ProcessCameraProvider.getInstance(ctx)
                    future.addListener({
                        val provider = future.get()
                        cameraHolder.provider = provider
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val barcodeScanner = BarcodeScanning.getClient()
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(executor) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                barcodeScanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        barcodes.firstOrNull()?.rawValue?.let { raw ->
                                            viewModel.onQrScanned(raw)
                                        }
                                    }
                                    .addOnCompleteListener { imageProxy.close() }
                            } else {
                                imageProxy.close()
                            }
                        }
                        try {
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                        } catch (_: Exception) {}
                    }, executor)
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val squareSize = min(size.width, size.height) * 0.65f
                val cornerLen = squareSize * 0.11f
                val strokeW = 3.dp.toPx()
                val left = (size.width - squareSize) / 2f
                val top = (size.height - squareSize) / 2f
                val right = left + squareSize
                val bottom = top + squareSize
                val dim = Color.Black.copy(alpha = 0.5f)

                drawRect(dim, size = Size(size.width, top))
                drawRect(dim, topLeft = Offset(0f, bottom), size = Size(size.width, size.height - bottom))
                drawRect(dim, topLeft = Offset(0f, top), size = Size(left, squareSize))
                drawRect(dim, topLeft = Offset(right, top), size = Size(size.width - right, squareSize))

                val c = Color.White
                val cap = StrokeCap.Round
                drawLine(c, Offset(left, top), Offset(left + cornerLen, top), strokeW, cap)
                drawLine(c, Offset(left, top), Offset(left, top + cornerLen), strokeW, cap)
                drawLine(c, Offset(right - cornerLen, top), Offset(right, top), strokeW, cap)
                drawLine(c, Offset(right, top), Offset(right, top + cornerLen), strokeW, cap)
                drawLine(c, Offset(left, bottom - cornerLen), Offset(left, bottom), strokeW, cap)
                drawLine(c, Offset(left, bottom), Offset(left + cornerLen, bottom), strokeW, cap)
                drawLine(c, Offset(right, bottom - cornerLen), Offset(right, bottom), strokeW, cap)
                drawLine(c, Offset(right - cornerLen, bottom), Offset(right, bottom), strokeW, cap)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.msg_camera_permission),
                    color = CloudWhite,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (state.isLoadingRecipient) {
            CircularProgressIndicator(
                color = CyanNeon,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (hasCameraPermission) {
            Text(
                text = stringResource(R.string.msg_point_camera),
                color = CloudWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
            )
        }
    }
}

private fun generateQrBitmap(content: String, size: Int): Bitmap {
    val hints = mapOf(EncodeHintType.MARGIN to 1)
    val bitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
    val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap[x, y] = if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bitmap
}
