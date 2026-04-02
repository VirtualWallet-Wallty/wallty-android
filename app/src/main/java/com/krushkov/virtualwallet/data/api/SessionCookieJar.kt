package com.krushkov.virtualwallet.data.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class SessionCookieJar(context: Context) : CookieJar {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("cookie_prefs", Context.MODE_PRIVATE)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        prefs.edit {
            val serialized = cookies.joinToString(";") {
                serialize(it)
            }
            putString(url.host, serialized)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = prefs.getString(url.host, null) ?: return emptyList()

        return cookies.split(";").mapNotNull {
            deserialize(url, it)
        }
    }

    private fun serialize(cookie: Cookie): String {
        return listOf(
            cookie.name,
            cookie.value,
            cookie.domain,
            cookie.path,
            cookie.expiresAt.toString(),
            cookie.secure.toString(),
            cookie.httpOnly.toString()
        ).joinToString("|")
    }

    private fun deserialize(url: HttpUrl, data: String): Cookie? {
        val parts = data.split("|")
        if (parts.size < 7) return null

        return Cookie.Builder()
            .name(parts[0])
            .value(parts[1])
            .domain(parts[2])
            .path(parts[3])
            .expiresAt(parts[4].toLong())
            .apply {
                if (parts[5].toBoolean()) secure()
                if (parts[6].toBoolean()) httpOnly()
            }
            .build()
    }
}