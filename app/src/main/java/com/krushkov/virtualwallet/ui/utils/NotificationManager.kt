package com.krushkov.virtualwallet.ui.utils

import com.krushkov.virtualwallet.ui.common.NotificationData
import com.krushkov.virtualwallet.ui.common.NotificationType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor() {
    private val _notifications = MutableSharedFlow<NotificationData>()
    val notifications = _notifications.asSharedFlow()

    suspend fun showSuccess(message: String) {
        _notifications.emit(NotificationData(message, isSuccess = true))
    }

    suspend fun showError(message: String) {
        _notifications.emit(NotificationData(message, isSuccess = false))
    }

    suspend fun showWarning(message: String) {
        _notifications.emit(
            NotificationData(
                message = message,
                isSuccess = false,
                type = NotificationType.Warning
            )
        )
    }
}
