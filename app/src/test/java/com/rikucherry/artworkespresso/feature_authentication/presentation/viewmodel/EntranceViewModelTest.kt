package com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel

import com.rikucherry.artworkespresso.FakeSecrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginStatus
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.GetLoginInfoUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    private lateinit var getLoginInfoUseCase: GetLoginInfoUseCase
    private lateinit var viewModel: EntranceViewModel
    private val prefHelper = mockk<SharedPreferenceHelper>(relaxed = true)

    @Before
    fun setup() {
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        getLoginInfoUseCase = GetLoginInfoUseCase(repository)
        viewModel = EntranceViewModel(userLoginUseCase,getLoginInfoUseCase, prefHelper)
    }

    @Test
    fun `FormAuthorizeUri_verifyResponseType`() {
        //Given
        //When
        val uri = viewModel.formAuthorizeUri("", true)
        val expectedResponseType = Constants.AUTH_RESPONSE_TYPE
        val actualResponseType = uri.getQueryParameter("response_type")
        //Then
        assertEquals(expectedResponseType,actualResponseType)
    }

    @Test
    fun `FormAuthorizeUri_verifyClientId`() {
        //Given
        //When
        val uri = viewModel.formAuthorizeUri("", true)
        val expectedClientId = "1"
        val actualClientId = uri.getQueryParameter("client_id")
        //Then
        assertEquals(expectedClientId, actualClientId)
    }


    @Test
    fun `FormAuthorizeUri_verifyScope`() {
        //Given
        //When
        val uri = viewModel.formAuthorizeUri("", true)
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
        val uri = viewModel.formAuthorizeUri(state, true)
        val actualState = uri.getQueryParameter("state")
        //Then
        assertEquals(state, actualState)
    }


    @Test
    fun `FormAuthorizeUri_verifyView`() {
        //Given
        val view = Constants.AUTH_VIEW
        //When
        val uri = viewModel.formAuthorizeUri("", true)
        val actualView = uri.getQueryParameter("view")
        //Then
        assertEquals(view, actualView)
    }

    @Test
    fun `FormAuthorizeUri_topicIsEmpty_redirectToTopicSelect`() {
        //Given
        //When
        val uri = viewModel.formAuthorizeUri("", true)
        val actualRedirectUri = uri.getQueryParameter("redirect_uri")
        val expectedRedirectUri = Constants.REDIRECT_URI_SCHEME + Constants.REDIRECT_HOST_TOPIC

        //Then
        assertEquals(expectedRedirectUri, actualRedirectUri)
    }

    @Test
    fun `FormAuthorizeUri_topicExists_redirectToTopicSelect`() {
        //Given
        //When
        val uri = viewModel.formAuthorizeUri("", false)
        val actualRedirectUri = uri.getQueryParameter("redirect_uri")
        val expectedRedirectUri = Constants.REDIRECT_URI_SCHEME + Constants.REDIRECT_HOST_DAILY

        //Then
        assertEquals(expectedRedirectUri, actualRedirectUri)
    }

    @Test
    fun `GetUserTopics_resultIsMutableSet`() {
        //Given
        //When
        val result = viewModel.getUserTopics()
        //Then
        assertTrue(result is MutableSet<String>)
    }


    @Test
    fun `GetClientTopics_resultIsMutableSet`() {
        //Given
        //When
        val result = viewModel.getClientTopics()
        //Then
        assertTrue(result is MutableSet<String>)
    }

    @Test
    fun `GetLoginStatus_userLoggedIn_stateChangesToUserLoggedIn`() {
        //Given
        //When
        viewModel.getLoginStatus()
        val result = viewModel.state.value.data
        //Then
        assertEquals(LoginStatus.USER_LOGGED_IN, result)
    }

}