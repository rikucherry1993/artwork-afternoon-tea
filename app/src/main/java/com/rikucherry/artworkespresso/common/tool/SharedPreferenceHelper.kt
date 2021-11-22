package com.rikucherry.artworkespresso.common.tool

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.rikucherry.artworkespresso.common.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceHelper @Inject constructor(
    private val context: Context
){

    private var prefs: SharedPreferences? = null

    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveUserAccessToken(token: String) {
        prefs?.edit(commit = true) {
            putString(Constants.USER_ACCESS_TOKEN, token)
        }
    }

    fun saveUserRefreshToken(token: String) {
        prefs?.edit(commit = true) {
            putString(Constants.USER_REFRESH_TOKEN, token)
        }
    }

    fun getUserAccessToken() = prefs?.getString(Constants.USER_ACCESS_TOKEN, "")
    fun getUserRefreshToken() = prefs?.getString(Constants.USER_REFRESH_TOKEN, "")

    fun clearPrefs() {
        prefs?.edit(commit = true) {
            clear()
        }
    }
}