package com.krushkov.virtualwallet.ui.features.pos

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krushkov.virtualwallet.R
import com.krushkov.virtualwallet.ui.core.Button
import com.krushkov.virtualwallet.ui.core.CircleButton
import com.krushkov.virtualwallet.ui.core.DropdownField
import com.krushkov.virtualwallet.ui.core.TextField
import com.krushkov.virtualwallet.ui.nav.Routes
import com.krushkov.virtualwallet.ui.theme.CloudWhite
import com.krushkov.virtualwallet.ui.theme.CyanNeon
import com.krushkov.virtualwallet.ui.theme.Green
import com.krushkov.virtualwallet.ui.theme.Red
import com.krushkov.virtualwallet.ui.theme.Yellow
import com.krushkov.virtualwallet.viewmodel.PosTerminalViewModel
import com.krushkov.virtualwallet.viewmodel.states.NfcStatus

private val SELECT_AID = byteArrayOf(
    0x00, 0xA4.toByte(), 0x04, 0x00, 0x07,
    0xF0.toByte(), 0x57, 0x41, 0x4C, 0x4C, 0x54, 0x59,
    0x00
)
private val RESPONSE_OK = byteArrayOf(0x90.toByte(), 0x00)

@Composable
fun PosTerminalScreen(
    navController: NavController,
    viewModel: PosTerminalViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }

    DisposableEffect(state.nfcStatus) {
        val activity = context as? Activity
        if (nfcAdapter == null || activity == null) {
            viewModel.setNfcAvailable(false)
        } else if (state.nfcStatus == NfcStatus.WAITING) {
            val callback = NfcAdapter.ReaderCallback { tag: Tag ->
                val payload = viewModel.buildPayload() ?: return@ReaderCallback
                sendPaymentViaNfc(tag, payload, viewModel)
            }
            nfcAdapter.enableReaderMode(
                activity,
                callback,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null
            )
        } else {
            nfcAdapter.disableReaderMode(activity)
        }

        onDispose {
            activity?.let { nfcAdapter?.disableReaderMode(it) }
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        CircleButton(
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back),
                    tint = CloudWhite,
                    modifier = Modifier.size(20.dp)
                )
            },
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start),
            containerColor = CyanNeon.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.title_pos_terminal),
            color = CloudWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.subtitle_pos_terminal),
            color = CloudWhite.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = state.amount,
            onValueChange = { viewModel.onAmountChange(it) },
            label = stringResource(R.string.label_amount),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        DropdownField(
            value = state.currencyOptions
                .firstOrNull { it.code == state.currencyCode }
                ?.displayName
                .orEmpty(),
            placeholder = stringResource(R.string.label_select_currency),
            expanded = state.isCurrencyMenuExpanded,
            onExpandedChange = { viewModel.toggleCurrencyMenu(it) },
            modifier = Modifier.fillMaxWidth(),
            items = {
                state.currencyOptions.forEach { currency ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = currency.displayName,
                                color = CloudWhite
                            )
                        },
                        onClick = {
                            viewModel.onCurrencyChange(currency.code)
                            viewModel.toggleCurrencyMenu(false)
                        }
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        NfcStatusIndicator(status = state.nfcStatus, isNfcAvailable = state.isNfcAvailable)

        Spacer(modifier = Modifier.height(16.dp))

        if (state.nfcStatus == NfcStatus.SENT) {
            Button(
                text = stringResource(R.string.action_new_request),
                onClick = { viewModel.setNfcStatus(NfcStatus.IDLE) },
                modifier = Modifier.fillMaxWidth(),
                containerColor = CyanNeon.copy(alpha = 0.5f)
            )
        } else {
            Button(
                text = stringResource(R.string.action_start_payment),
                onClick = {
                    if (viewModel.canStartPaymentRequest()) {
                        navController.popBackStack(Routes.HOME, false)
                        viewModel.startPaymentRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Green.copy(alpha = 0.5f),
                enabled = viewModel.canStartPaymentRequest()
            )
        }
    }
}

@Composable
private fun NfcStatusIndicator(status: NfcStatus, isNfcAvailable: Boolean) {
    val (text, color) = when {
        !isNfcAvailable -> Pair(
            stringResource(R.string.msg_nfc_unavailable),
            CloudWhite.copy(alpha = 0.4f)
        )
        status == NfcStatus.IDLE -> Pair(
            stringResource(R.string.msg_pos_enter_amount),
            CloudWhite.copy(alpha = 0.6f)
        )
        status == NfcStatus.WAITING -> Pair(
            stringResource(R.string.msg_pos_waiting),
            Yellow
        )
        status == NfcStatus.SENT -> Pair(
            stringResource(R.string.msg_pos_sent),
            Green
        )
        else -> Pair(
            stringResource(R.string.msg_pos_error),
            Red
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun sendPaymentViaNfc(tag: Tag, payload: String, viewModel: PosTerminalViewModel) {
    val isoDep = IsoDep.get(tag) ?: run {
        viewModel.setNfcStatus(NfcStatus.ERROR)
        return
    }
    try {
        isoDep.connect()
        isoDep.timeout = 5000

        val selectResponse = isoDep.transceive(SELECT_AID)
        if (!selectResponse.contentEquals(RESPONSE_OK)) {
            viewModel.setNfcStatus(NfcStatus.ERROR)
            return
        }

        val payloadBytes = payload.toByteArray(Charsets.UTF_8)
        val paymentCmd = ByteArray(5 + payloadBytes.size + 1)
        paymentCmd[0] = 0x80.toByte()
        paymentCmd[1] = 0x01
        paymentCmd[2] = 0x00
        paymentCmd[3] = 0x00
        paymentCmd[4] = payloadBytes.size.toByte()
        payloadBytes.copyInto(paymentCmd, 5)
        paymentCmd[paymentCmd.size - 1] = 0x00

        val payResponse = isoDep.transceive(paymentCmd)
        if (payResponse.contentEquals(RESPONSE_OK)) {
            viewModel.setNfcStatus(NfcStatus.SENT)
        } else {
            viewModel.setNfcStatus(NfcStatus.ERROR)
        }
    } catch (e: Exception) {
        viewModel.setNfcStatus(NfcStatus.ERROR)
    } finally {
        runCatching { isoDep.close() }
    }
}
