package com.example.latihan_camerax.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.latihan_camerax.R


class LoginSession(context: Context) {
    companion object {
        private const val TOKEN_KEY = "authToken"
    }

    private val loginSession =
        context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = loginSession.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun passToken(): String? {
        return loginSession.getString(TOKEN_KEY, null)
    }

    fun logoutSession() {
        loginSession.edit().clear().apply()
    }
}