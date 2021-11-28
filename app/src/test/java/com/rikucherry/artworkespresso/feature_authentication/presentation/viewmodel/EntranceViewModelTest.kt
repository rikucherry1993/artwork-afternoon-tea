package com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel

import com.rikucherry.artworkespresso.FakeSecrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class EntranceViewModelTest {

    private val secrets = FakeSecrets()
    private val repository = FakeAuthenticationRepositoryImpl()

    private lateinit var userLoginUseCase: UserLoginUseCase

    @Before
    fun setup() {
        userLoginUseCase = UserLoginUseCase(repository, secrets)
    }

    @Test
    fun `FormAuthorizeUri_verifyResponseType`() {
        //Given
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", true)
        val expectedResponseType = Constants.AUTH_RESPONSE_TYPE
        val actualResponseType = uri.getQueryParameter("response_type")
        //Then
        assertEquals(expectedResponseType,actualResponseType)
    }

    @Test
    fun `FormAuthorizeUri_verifyClientId`() {
        //Given
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", true)
        val expectedClientId = "1"
        val actualClientId = uri.getQueryParameter("client_id")
        //Then
        assertEquals(expectedClientId, actualClientId)
    }


    @Test
    fun `FormAuthorizeUri_verifyScope`() {
        //Given
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", true)
        val expectedScope = Constants.FULL_SCOPE
        val actualScope = uri.getQueryParameter("scope")
        //Then
        assertEquals(expectedScope, actualScope)
    }


    @Test
    fun `FormAuthorizeUri_verifyState`() {
        //Given
        val state = "123"
        //When
        val uri = userLoginUseCase.formAuthorizeUri(state, true)
        val actualState = uri.getQueryParameter("state")
        //Then
        assertEquals(state, actualState)
    }


    @Test
    fun `FormAuthorizeUri_verifyView`() {
        //Given
        val view = Constants.AUTH_VIEW
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", true)
        val actualView = uri.getQueryParameter("view")
        //Then
        assertEquals(view, actualView)
    }

    @Test
    fun `FormAuthorizeUri_topicIsEmpty_redirectToTopicSelect`() {
        //Given
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", true)
        val actualRedirectUri = uri.getQueryParameter("redirect_uri")
        val expectedRedirectUri = Constants.REDIRECT_URI_SCHEME + Constants.REDIRECT_HOST_TOPIC

        //Then
        assertEquals(expectedRedirectUri, actualRedirectUri)
    }

    @Test
    fun `FormAuthorizeUri_topicExists_redirectToTopicSelect`() {
        //Given
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", false)
        val actualRedirectUri = uri.getQueryParameter("redirect_uri")
        val expectedRedirectUri = Constants.REDIRECT_URI_SCHEME + Constants.REDIRECT_HOST_DAILY

        //Then
        assertEquals(expectedRedirectUri, actualRedirectUri)
    }

}