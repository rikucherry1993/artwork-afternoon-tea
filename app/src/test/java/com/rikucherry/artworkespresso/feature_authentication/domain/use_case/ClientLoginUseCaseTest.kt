package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.FakeSecrets
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_authentication.domain.model.ClientTokenResponse
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
class ClientLoginUseCaseTest {

    private val secrets = FakeSecrets()

    @Test
    fun `Invoke_getClientAccessTokenSucceeded_emitSuccess`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        //When
        val flow = clientLoginUseCase()
        //Then
        flow.onEach {
            result -> assertTrue(result is Resource.Success<ClientTokenResponse>)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getClientAccessTokenSucceeded_emitCorrectAccessToken`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        //When
        val flow = clientLoginUseCase()
        //Then
        val expectedAccessToken = "fake access token"
        flow.onEach { result ->
                val actualAccessToken =
                    (result as Resource.Success<ClientTokenResponse>).data.accessToken
                assertEquals(expectedAccessToken, actualAccessToken)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getClientAccessTokenFailed_emitError`() {
        //Given
        val repository = FakeAuthenticationRepositoryErrorImpl()
        val clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        //When
        val flow = clientLoginUseCase()
        //Then
        flow.onEach {
                result -> assertTrue(result is Resource.Error<ClientTokenResponse>)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getClientAccessTokenFailed_emitCorrectErrorCode`() {
        //Given
        val repository = FakeAuthenticationRepositoryErrorImpl()
        val clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        //When
        val flow = clientLoginUseCase()
        //Then
        val expectedErrorCode = 400
        flow.onEach { result ->
            val actualErrorCode =
                (result as Resource.Error<ClientTokenResponse>).statusCode.code
            assertEquals(expectedErrorCode, actualErrorCode)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getClientAccessTokenThrowsException_emitException`() {
        //Given
        val repository = FakeAuthenticationRepositoryExceptionImpl()
        val clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        //When
        val flow = clientLoginUseCase()
        //Then
        flow.onEach {
                result -> assertTrue(result is Resource.Exception<ClientTokenResponse>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getClientAccessTokenThrowsException_emitCorrectExceptionMessage`() {
        //Given
        val repository = FakeAuthenticationRepositoryExceptionImpl()
        val clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        //When
        val flow = clientLoginUseCase()
        //Then
        val expectedMessage = "Throw new IO exception"
        flow.onEach { result ->
            val actualMessage =
                (result as Resource.Exception<ClientTokenResponse>).message
            assertEquals(expectedMessage, actualMessage)
        }.launchIn(TestScope())
    }

}