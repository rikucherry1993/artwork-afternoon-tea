package com.rikucherry.artworkespresso.feature_topic_selection.presentation.viewmodel

import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.common.tool.ViewModelState
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeUserRepositoryImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.GetUserUseCase
import com.rikucherry.artworkespresso.feature_authentication.domain.use_case.InsertLoginInfoUseCase
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.rikucherry.artworkespresso.feature_topic_selection.domain.repository.FakeTopicSelectRepositoryImpl
import com.rikucherry.artworkespresso.feature_topic_selection.domain.repository.topicHasExamples
import com.rikucherry.artworkespresso.feature_topic_selection.domain.repository.topicNoExample
import com.rikucherry.artworkespresso.feature_topic_selection.domain.use_case.TopicSelectUseCase
import com.skydoves.sandwich.StatusCode
import io.mockk.mockk
import org.junit.Assert.assertEquals
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

}