package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.FakeSecrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_authentication.domain.model.UserTokenResponse
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryErrorImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryExceptionImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(manifest= Config.NONE)
class UserLoginUseCaseTest {

    private val secrets = FakeSecrets()

    @Test
    fun `FormAuthorizeUri_topicIsEmpty_redirectToTopicSelect`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", true)
        //Then
        val actualRedirectUri = uri.getQueryParameter("redirect_uri")
        val expectedRedirectUri = Constants.REDIRECT_URI_SCHEME + Constants.REDIRECT_HOST_TOPIC

        assertEquals(expectedRedirectUri, actualRedirectUri)
    }


    @Test
    fun `FormAuthorizeUri_topicExists_redirectToDailyBrief`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val uri = userLoginUseCase.formAuthorizeUri("", false)
        //Then
        val actualRedirectUri = uri.getQueryParameter("redirect_uri")
        val expectedRedirectUri = Constants.REDIRECT_URI_SCHEME + Constants.REDIRECT_HOST_DAILY

        assertEquals(expectedRedirectUri, actualRedirectUri)
    }


    @Test
    fun `Invoke_getUserAccessTokenSucceeded_emitSuccess`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        flow.onEach {
            result -> assertTrue(result is Resource.Success<UserTokenResponse>)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getUserAccessTokenSucceeded_emitCorrectAccessToken`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        val expectedAccessToken = "fake access token"
        flow.onEach { result ->
                val actualAccessToken =
                    (result as Resource.Success<UserTokenResponse>).data.accessToken
                assertEquals(expectedAccessToken, actualAccessToken)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getUserAccessTokenSucceeded_emitCorrectRefreshToken`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        val expectedRefreshToken = "fake refresh token"
        flow.onEach { result ->
            val actualRefreshToken =
                (result as Resource.Success<UserTokenResponse>).data.refreshToken
            assertEquals(expectedRefreshToken, actualRefreshToken)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getUserAccessTokenFailed_emitError`() {
        //Given
        val repository = FakeAuthenticationRepositoryErrorImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        flow.onEach {
                result -> assertTrue(result is Resource.Error<UserTokenResponse>)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getUserAccessTokenFailed_emitCorrectErrorCode`() {
        //Given
        val repository = FakeAuthenticationRepositoryErrorImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        val expectedErrorCode = 400
        flow.onEach { result ->
            val actualErrorCode =
                (result as Resource.Error<UserTokenResponse>).statusCode.code
            assertEquals(expectedErrorCode, actualErrorCode)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getUserAccessTokenThrowsException_emitException`() {
        //Given
        val repository = FakeAuthenticationRepositoryExceptionImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        flow.onEach {
                result -> assertTrue(result is Resource.Exception<UserTokenResponse>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getUserAccessTokenThrowsException_emitCorrectExceptionMessage`() {
        //Given
        val repository = FakeAuthenticationRepositoryExceptionImpl()
        val userLoginUseCase = UserLoginUseCase(repository, secrets)
        //When
        val flow = userLoginUseCase("", true)
        //Then
        val expectedMessage = "Throw new IO exception"
        flow.onEach { result ->
            val actualMessage =
                (result as Resource.Exception<UserTokenResponse>).message
            assertEquals(expectedMessage, actualMessage)
        }.launchIn(TestScope())
    }

}