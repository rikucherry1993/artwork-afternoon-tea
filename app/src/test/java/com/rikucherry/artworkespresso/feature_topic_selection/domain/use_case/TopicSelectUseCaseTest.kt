package com.rikucherry.artworkespresso.feature_topic_selection.domain.use_case

import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.data_source.TopTopicsDto
import com.rikucherry.artworkespresso.feature_topic_selection.domain.repository.FakeTopicSelectRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(manifest= Config.NONE)
class TopicSelectUseCaseTest {

    @Test
    fun `Invoke_getTopTopicsSucceeded_emitSuccess`() {
        //Given
        val repository = FakeTopicSelectRepositoryImpl()
        val topicSelectUseCase = TopicSelectUseCase(repository)
        //When
        val flow = topicSelectUseCase(token = "fake token")
        //Then
        flow.onEach {
                result ->
            Assert.assertTrue(result is Resource.Success<List<TopTopicsDto>>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getTopTopicsSucceeded_emitCorrectContents`() {
        //Given
        val repository = FakeTopicSelectRepositoryImpl()
        val topicSelectUseCase = TopicSelectUseCase(repository)
        //When
        val flow = topicSelectUseCase(token = "fake token")
        //Then
        val expectedTopic1 = repository.topicNoExample
        val expectedTopic2 = repository.topicHasExamples

        flow.onEach {
                response ->
                val results = (response as Resource.Success<List<TopTopicsDto>>).data

            assertEquals(results[0], expectedTopic1)
            assertEquals(results[1], expectedTopic2)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getTopTopicsFailed_emitError`() {
        //Given
        val repository = FakeTopicSelectRepositoryImpl()
        val topicSelectUseCase = TopicSelectUseCase(repository)
        //When
        val flow = topicSelectUseCase(token = "fake token")
        //Then
        flow.onEach {
                result ->
            Assert.assertTrue(result is Resource.Error<List<TopTopicsDto>>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getTopTopicsFailed_emitCorrectErrorCode`() {
        //Given
        val repository = FakeTopicSelectRepositoryImpl()
        val topicSelectUseCase = TopicSelectUseCase(repository)
        //When
        val flow = topicSelectUseCase(token = "fake token")
        //Then
        val expectedErrorCode = 400
        flow.onEach { result ->
            val actualErrorCode =
                (result as Resource.Error<List<TopTopicsDto>>).statusCode.code
            assertEquals(expectedErrorCode, actualErrorCode)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getTopTopicsThrowsException_emitException`() {
        //Given
        val repository = FakeTopicSelectRepositoryImpl()
        val topicSelectUseCase = TopicSelectUseCase(repository)
        //When
        val flow = topicSelectUseCase(token = "fake token")
        //Then
        flow.onEach {
                result ->
            Assert.assertTrue(result is Resource.Exception<List<TopTopicsDto>>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getTopTopicsThrowsException_emitCorrectExceptionMessage`() {
        //Given
        val repository = FakeTopicSelectRepositoryImpl()
        val topicSelectUseCase = TopicSelectUseCase(repository)
        //When
        val flow = topicSelectUseCase(token = "fake token")
        //Then
        val expectedMessage = "Throw new IO exception"
        flow.onEach { result ->
            val actualMessage =
                (result as Resource.Exception<List<TopTopicsDto>>).message
            assertEquals(expectedMessage, actualMessage)
        }.launchIn(TestScope())
    }
}