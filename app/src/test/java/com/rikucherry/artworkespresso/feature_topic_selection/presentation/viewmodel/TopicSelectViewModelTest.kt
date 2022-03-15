package com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel

import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.*
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.GetUserUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.InsertLoginInfoUseCase
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.rikucherry.artworkespresso.feature_topic_selection.domain.repository.*
import com.rikucherry.artworkespresso.feature_topic_selection.domain.use_case.TopicSelectUseCase
import com.skydoves.sandwich.StatusCode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class TopicSelectViewModelTest {

    private lateinit var topicSelectRepository: TopicSelectRepository
    private lateinit var userRepository: UserRepository
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var topicSelectUseCase: TopicSelectUseCase
    private lateinit var insertLoginInfoUseCase: InsertLoginInfoUseCase
    private val prefHelper = mockk<SharedPreferenceHelper>(relaxed = true)

    private lateinit var SUT: TopicSelectViewModel

    @Test
    fun `GetTopTopics_APISucceeded_topicStateChangedToSuccess`() {
        //Given
        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = listOf(topicNoExample, topicHasExamples),
            statusCode = 200,
            status = StatusCode.OK
        )
        val state = SUT.topicState.value

        assertEquals(expectedState, state)
    }

    @Test
    fun `GetTopTopics_APIFailed_topicStateChangedToError`() {
        //Given
        topicSelectRepository = FakeTopicSelectRepositoryErrorImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState<List<TopTopicsDto>>(
            isLoading = false,
            statusCode = 400,
            status = StatusCode.BadRequest
        )
        val state = SUT.topicState.value

        assertTrue(
            expectedState.isLoading == state.isLoading
                    && expectedState.statusCode == state.statusCode
                    && expectedState.status == state.status
                    && state.error != null
        )
    }

    @Test
    fun `GetTopTopics_APIException_topicStateChangedToException`() {
        //Given
        topicSelectRepository = FakeTopicSelectRepositoryExceptionImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState<List<TopTopicsDto>>(
            isLoading = false,
            error = "Throw new IO exception"
        )
        val state = SUT.topicState.value

        assertEquals(expectedState, state)
    }

    @Test
    fun `setLoginInfoByUser_clientLogin_userStateChangedToSuccess`() {
        //Given
        // let isClientLogin() returns true
        every {prefHelper.isClientLogin()} returns true

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = LoginInfoItem(1, "", "", "", 3),
            statusCode = StatusCode.OK.code,
            status = StatusCode.OK
        )
        val state = SUT.userState.value

        assertEquals(expectedState, state)
    }

    @Test
    fun `setLoginInfoByUser_userLogin_APISucceeded_userStateChangedToSuccess`() {
        //Given
        // let isClientLogin() returns false
        every {prefHelper.isClientLogin()} returns false

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = LoginInfoItem(
                id = 1,
                userId = "id",
                userName = "name",
                userIconUrl = "url",
                status = 2
            ),
            statusCode = StatusCode.OK.code,
            status = StatusCode.OK
        )
        val state = SUT.userState.value

        assertEquals(expectedState, state)
    }

    @Test
    fun `setLoginInfoByUser_userLogin_APIFailed_userStateChangedToError`() {
        //Given
        // let isClientLogin() returns false
        every {prefHelper.isClientLogin()} returns false

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryErrorImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState<LoginInfoItem>(
            isLoading = false,
            statusCode = 400,
            status = StatusCode.BadRequest
        )
        val state = SUT.userState.value

        assertTrue(
            expectedState.isLoading == state.isLoading
                    && expectedState.statusCode == state.statusCode
                    && expectedState.status == state.status
                    && state.error != null
        )
    }

    @Test
    fun `setLoginInfoByUser_userLogin_APIException_userStateChangedToException`() {
        //Given
        // let isClientLogin() returns false
        every {prefHelper.getUserAccessToken()} returns "valid user token"

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryExceptionImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        //Then
        val expectedState = ViewModelState<LoginInfoItem>(
            isLoading = false,
            error = "Throw new IO exception"
        )
        val state = SUT.userState.value

       assertEquals(expectedState,state)
    }

    @Test
    fun `saveFavouriteTopic_clientLogin_saveClientFavoriteTopicsIsCalled`() {
        //Given
        // let isClientLogin() returns true
        every {prefHelper.isClientLogin()} returns true

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )
        val topicName = "topicname"
        SUT.saveFavouriteTopic(topicName)

        //Then
        //Check if the matching call is made
        verify { prefHelper.saveClientFavoriteTopics(mutableSetOf(topicName)) }
    }

    @Test
    fun `saveFavouriteTopic_userLogin_saveUserFavoriteTopicsIsCalled`() {
        //Given
        // let isClientLogin() returns false
        every {prefHelper.isClientLogin()} returns false

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )
        val topicName = "topicname"
        SUT.saveFavouriteTopic(topicName)

        //Then
        //Check if the matching call is made
        verify { prefHelper.saveUserFavoriteTopics(mutableSetOf(topicName)) }
    }

    @Test
    fun `InsertLoginInfo_InsertSucceeded_clientLogin_loginInfoStateChangedToSuccess`() {
        //Given
        // let isClientLogin() returns true
        every {prefHelper.isClientLogin()} returns true

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        SUT.insertLoginInfo()

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = LoginInfoItem(
                id = 1,
                userId = "",
                userName = "",
                userIconUrl = "",
                status = 3
            ),
            status = StatusCode.OK,
        )
        val state = SUT.loginInfoState.value

        assertEquals(expectedState, state)
    }

    @Test
    fun `InsertLoginInfo_InsertSucceeded_userLogin_loginInfoStateChangedToSuccess`() {
        //Given
        // let isClientLogin() returns false
        every {prefHelper.isClientLogin()} returns false

        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        SUT.insertLoginInfo()

        //Then
        val expectedState = ViewModelState(
            isLoading = false,
            data = LoginInfoItem(
                id = 1,
                userId = "id",
                userName = "name",
                userIconUrl = "url",
                status = 2
            ),
            status = StatusCode.OK,
        )
        val state = SUT.loginInfoState.value

        assertEquals(expectedState, state)
    }

    @Test
    fun `InsertLoginInfo_InsertExit_loginInfoStateChangedToException`() {
        //Given
        topicSelectRepository = FakeTopicSelectRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        authRepository = FakeAuthenticationRepositoryExceptionImpl()
        topicSelectUseCase = TopicSelectUseCase(topicSelectRepository)
        getUserUseCase = GetUserUseCase(userRepository)
        insertLoginInfoUseCase = InsertLoginInfoUseCase(authRepository)

        //When
        SUT = TopicSelectViewModel(
            topicSelectUseCase,
            getUserUseCase,
            insertLoginInfoUseCase,
            prefHelper
        )

        SUT.insertLoginInfo()

        //Then
        val expectedState = ViewModelState<LoginInfoItem>(
            isLoading = false,
            error = "Throw new SQL exception"
        )
        val state = SUT.loginInfoState.value

        assertEquals(expectedState, state)
    }
}