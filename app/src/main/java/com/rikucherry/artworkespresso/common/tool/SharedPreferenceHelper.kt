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

    fun saveClientAccessToken(token: String) {
        prefs?.edit(commit = true) {
            putString(Constants.CLIENT_ACCESS_TOKEN, token)
        }
    }

    fun saveUserFavoriteTopics(topics: MutableSet<String>) {
        prefs?.edit(commit = true) {
            putStringSet(Constants.USER_TOPICS, topics)
        }
    }

    fun saveClientFavoriteTopics(topics: MutableSet<String>) {
        prefs?.edit(commit = true) {
            putStringSet(Constants.CLIENT_TOPICS, topics)
        }
    }

    fun getUserAccessToken() = prefs?.getString(Constants.USER_ACCESS_TOKEN, "")
    fun getUserRefreshToken() = prefs?.getString(Constants.USER_REFRESH_TOKEN, "")
    fun getClientAccessToken() = prefs?.getString(Constants.CLIENT_ACCESS_TOKEN, "")
    fun getUserFavoriteTopics(): MutableSet<String>? = prefs?.getStringSet(Constants.USER_TOPICS, null)
    fun getClientFavoriteTopics(): MutableSet<String>? = prefs?.getStringSet(Constants.CLIENT_TOPICS, null)

    /**
     * Clear all preferences
     */
    fun clearPrefs() {
        prefs?.edit(commit = true) {
            clear()
        }
    }

    /**
     * Clear all access/refresh tokens
     */
    fun clearTokens() {
        prefs?.edit(commit = true) {
            remove(Constants.USER_ACCESS_TOKEN)
            remove(Constants.USER_REFRESH_TOKEN)
            remove(Constants.CLIENT_ACCESS_TOKEN)
        }
    }


    /**
     * remove a designated preference
     */
    fun removePreference(key: String) {
        prefs?.edit(commit = true) {
            remove(key)
        }
    }
}