package com.rikucherry.artworkespresso.feature_authentication.domain.util

import android.content.Intent
import android.net.Uri
import com.rikucherry.artworkespresso.common.Constants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
class AuthenticationUtilTest {

    @Test
    @Config(manifest=Config.NONE)
    fun `FormAuthorizeUri_FullParameters_GetExpectedUri`() {
        //Given
        val redirectUri = Uri.encode("http://myapp.example/cb")
        val parameters = "response_type=code&client_id=0" +
                "&redirect_uri=$redirectUri" +
                "&scope=basic" +
                "&state=mysessionid&view=login"
        val expect = Constants.BASE_URL + Constants.BASE_AUTH_PATH + "?" + parameters

        //When
        val actual = AuthenticationUtil.formAuthorizeUri(
            responseType = "code",
            clientId = "0",
            redirectUri = "http://myapp.example/cb",
            scope = "basic",
            state = "mysessionid",
            view = "login"
        ).toString()

        //Then
        assertEquals(expect, actual)
    }

    @Test
    @Config(manifest=Config.NONE)
    fun `RetrieveAuthorizeCode_Normal_GetAuthorizeCode`() {
        //Given
        val intent = Intent(Intent.ACTION_VIEW)
        val code = UUID.randomUUID().toString()
        val state = UUID.randomUUID().toString()
        intent.data = Uri.parse("http://test.com?state=$state&code=$code")

        //When
        val resultCode = AuthenticationUtil.retrieveAuthorizeCode(intent, state)

        //Then
        assertEquals(resultCode, code)
    }


    @Test
    @Config(manifest=Config.NONE)
    fun `RetrieveAuthorizeCode_IntentIsNull_ReturnNull`() {
        //Given
        val state = UUID.randomUUID().toString()

        //When
        val resultCode = AuthenticationUtil.retrieveAuthorizeCode(null, state)

        //Then
        assertNull(resultCode)
    }


    @Test
    @Config(manifest=Config.NONE)
    fun `RetrieveAuthorizeCode_WrongIntentAction_ReturnNull`() {
        //Given
        val intent = Intent()
        val code = UUID.randomUUID().toString()
        val state = UUID.randomUUID().toString()
        intent.data = Uri.parse("http://test.com?state=$state&code=$code")

        //When
        val resultCode = AuthenticationUtil.retrieveAuthorizeCode(intent, state)

        //Then
        assertNull(resultCode)
    }


    @Test
    @Config(manifest=Config.NONE)
    fun `RetrieveAuthorizeCode_StateInconsistent_ReturnNull`() {
        //Given
        val intent = Intent(Intent.ACTION_VIEW)
        val code = UUID.randomUUID().toString()
        val state = UUID.randomUUID().toString()
        intent.data = Uri.parse("http://test.com?state=$state&code=$code")

        //When
        val resultCode = AuthenticationUtil.retrieveAuthorizeCode(intent, "1234")

        //Then
        assertNull(resultCode)
    }

}