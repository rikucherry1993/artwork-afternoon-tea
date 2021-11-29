package com.rikucherry.artworkespresso.feature_authentication.presentation.viewmodel

import android.content.Intent
import android.os.Bundle
import com.rikucherry.artworkespresso.FakeSecrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryErrorImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryExceptionImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.ClientLoginUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.UserLoginUseCase
import com.skydoves.sandwich.StatusCode
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class LoginViewModelTest {

    private val secrets = FakeSecrets()
    private lateinit var repository: AuthenticationRepository
    private lateinit var userLoginUseCase: UserLoginUseCase
    private lateinit var clientLoginUseCase: ClientLoginUseCase
    private val prefHelper = mockk<SharedPreferenceHelper>(relaxed = true)
    private var args: Bundle? = null

    private lateinit var SUT: LoginViewModel

    @Test
    fun `GetClientAccessToken_APISucceeded_stateChangedToSuccess`() {
        //Given
        repository = FakeAuthenticationRepositoryImpl()
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,true)
            this.putParcelable(Constants.AUTH_INTENT, Intent())
            this.putString(Constants.AUTH_STATE,"")
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        //When
        SUT = LoginViewModel(userLoginUseCase, clientLoginUseCase, prefHelper,args)

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = "fake access token",
            statusCode = 200,
            status = StatusCode.OK,
            message = "fake access token"
        )
        val state = SUT.state.value

        assertEquals(expectedState, state)
    }


    @Test
    fun `GetClientAccessToken_APIFailed_stateChangedToFailed`() {
        //Given
        repository = FakeAuthenticationRepositoryErrorImpl()
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,true)
            this.putParcelable(Constants.AUTH_INTENT, Intent())
            this.putString(Constants.AUTH_STATE,"")
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        //When
        SUT = LoginViewModel(userLoginUseCase, clientLoginUseCase, prefHelper,args)

        //Then
        val expectedState = ViewModelState<String>(
            isLoading = false,
            statusCode = 400,
            status = StatusCode.BadRequest
        )
        val state = SUT.state.value

        assertTrue(
            expectedState.isLoading == state.isLoading
                    && expectedState.statusCode == state.statusCode
                    && expectedState.status == state.status
                    && state.message != null //Might change the message
        )
    }

    @Test
    fun `GetClientAccessToken_APIExceptionThrown_stateChangedToException`() {
        //Given
        repository = FakeAuthenticationRepositoryExceptionImpl()
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,true)
            this.putParcelable(Constants.AUTH_INTENT, Intent())
            this.putString(Constants.AUTH_STATE,"")
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        //When
        SUT = LoginViewModel(userLoginUseCase, clientLoginUseCase, prefHelper,args)

        //Then
        val expectedState = ViewModelState<String>(
            isLoading = false,
            message = "Throw new IO exception"
        )
        val state = SUT.state.value

        assertEquals(expectedState, state)
    }


    @Test
    fun `GetUserAccessToken_APISucceeded_stateChangedToSuccess`() {
        //Given
        repository = FakeAuthenticationRepositoryImpl()
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,false)
            this.putParcelable(Constants.AUTH_INTENT, Intent())
            this.putString(Constants.AUTH_STATE,"")
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        //When
        SUT = LoginViewModel(userLoginUseCase, clientLoginUseCase, prefHelper,args)

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = "fake access token",
            statusCode = 200,
            status = StatusCode.OK,
            message = "fake access token"
        )
        val state = SUT.state.value

        assertEquals(expectedState, state)
    }


    @Test
    fun `GetUserAccessToken_APIFailed_stateChangedToFailed`() {
        //Given
        repository = FakeAuthenticationRepositoryErrorImpl()
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,false)
            this.putParcelable(Constants.AUTH_INTENT, Intent())
            this.putString(Constants.AUTH_STATE,"")
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        //When
        SUT = LoginViewModel(userLoginUseCase, clientLoginUseCase, prefHelper,args)

        //Then
        val expectedState = ViewModelState<String>(
            isLoading = false,
            statusCode = 400,
            status = StatusCode.BadRequest
        )
        val state = SUT.state.value

        assertTrue(
            expectedState.isLoading == state.isLoading
                    && expectedState.statusCode == state.statusCode
                    && expectedState.status == state.status
                    && state.message != null //Might change the message
        )
    }


    @Test
    fun `GetUserAccessToken_APIExceptionThrown_stateChangedToException`() {
        //Given
        repository = FakeAuthenticationRepositoryExceptionImpl()
        userLoginUseCase = UserLoginUseCase(repository, secrets)
        clientLoginUseCase = ClientLoginUseCase(repository, secrets)
        args = Bundle().apply {
            this.putBoolean(Constants.IS_FREE_TRAIL,false)
            this.putParcelable(Constants.AUTH_INTENT, Intent())
            this.putString(Constants.AUTH_STATE,"")
            this.putBoolean(Constants.IS_TOPIC_EMPTY, true)
        }

        //When
        SUT = LoginViewModel(userLoginUseCase, clientLoginUseCase, prefHelper,args)

        //Then
        val expectedState = ViewModelState<String>(
            isLoading = false,
            message = "Throw new IO exception"
        )
        val state = SUT.state.value

        assertEquals(expectedState, state)
    }

}