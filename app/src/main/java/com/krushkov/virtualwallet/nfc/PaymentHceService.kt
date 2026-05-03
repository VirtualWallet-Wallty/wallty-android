package com.krushkov.virtualwallet.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class PaymentHceService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        return try {
            when {
                isSelectAid(commandApdu) -> OK
                isPaymentCmd(commandApdu) -> handlePayment(commandApdu)
                else -> UNKNOWN_CMD
            }
        } catch (e: Exception) {
            ERROR
        }
    }

    private fun isSelectAid(apdu: ByteArray): Boolean {
        if (apdu.size < 12) return false
        return apdu[0] == 0x00.toByte() &&
                apdu[1] == 0xA4.toByte() &&
                apdu[2] == 0x04.toByte() &&
                apdu[3] == 0x00.toByte() &&
                apdu[4] == 0x07.toByte() &&
                apdu.sliceArray(5..11).contentEquals(AID)
    }

    private fun isPaymentCmd(apdu: ByteArray): Boolean =
        apdu.size > 4 && apdu[0] == 0x80.toByte() && apdu[1] == 0x01.toByte()

    private fun handlePayment(apdu: ByteArray): ByteArray {
        val lc = apdu[4].toInt() and 0xFF
        if (apdu.size < 5 + lc) return ERROR
        val payload = String(apdu.sliceArray(5 until 5 + lc), Charsets.UTF_8)
        val parts = payload.split("|")
        if (parts.size < 2) return ERROR
        val amount = parts[0].toBigDecimalOrNull() ?: return ERROR
        val currencyCode = parts[1]
        val merchantRef = if (parts.size >= 3) parts[2] else ""
        PaymentEventBus.post(PendingPayment(amount, currencyCode, merchantRef))
        return OK
    }

    override fun onDeactivated(reason: Int) {}

    companion object {
        private val AID = byteArrayOf(0xF0.toByte(), 0x57, 0x41, 0x4C, 0x4C, 0x54, 0x59)
        private val OK = byteArrayOf(0x90.toByte(), 0x00)
        private val ERROR = byteArrayOf(0x6F.toByte(), 0x00)
        private val UNKNOWN_CMD = byteArrayOf(0x6D.toByte(), 0x00)
    }
}
