package com.rikucherry.artworkespresso.feature_authentication.domain.util

import android.content.Intent
import android.net.Uri
import com.rikucherry.artworkespresso.common.Constants

class AuthenticationUtil {
    companion object {
        /**
         * Form the authorization URL provided by server side
         * @see <a href="https://www.deviantart.com/developers/authentication">documentation</a>
         */
        @JvmStatic
        fun formAuthorizeUri(
            responseType: String,
            clientId: String,
            redirectUri: String,
            scope: String?,
            state: String?,
            view: String?
        ): Uri {
            return Uri.parse(Constants.BASE_URL + Constants.BASE_AUTH_PATH)
                .buildUpon()
                .appendQueryParameter("response_type", responseType)
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("scope", scope ?: Constants.DEFAULT_SCOPE)
                .appendQueryParameter("state", state ?: "")
                .appendQueryParameter("view", view ?: "")
                .build()
        }

        /**
         * Retrieve Authorization code from received Intent.
         */
        @JvmStatic
        fun retrieveAuthorizeCode(intent: Intent? = null, state: String): String? {
            val uri = intent?.data
            return if (intent?.action != Intent.ACTION_VIEW ||
                uri?.getQueryParameter("state") != state
            ) {
                null
            } else {
                uri.getQueryParameter("code")
            }
        }
    }

}