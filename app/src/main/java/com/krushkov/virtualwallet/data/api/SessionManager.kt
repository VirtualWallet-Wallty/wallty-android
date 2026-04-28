package com.krushkov.virtualwallet.data.api

object SessionManager {

    private var onLogout: (() -> Unit)? = null

    fun setOnLogout(callback: () -> Unit) {
        onLogout = callback
    }

    fun notifySessionExpired() {
        onLogout?.invoke()
    }
}