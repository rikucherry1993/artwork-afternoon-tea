package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeUserRepositoryErrorImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeUserRepositoryExceptionImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeUserRepositoryImpl
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
@Config(manifest = Config.NONE)
class GetUserUseCaseTest {

    @Test
    fun `Invoke_getUserSucceeded_emitSuccess`() {
        //Given
        val repository = FakeUserRepositoryImpl()
        val getUserUseCase = GetUserUseCase(repository)
        //When
        val flow = getUserUseCase("token")
        //Then
        flow.onEach { result ->
            assertTrue(result is Resource.Success<UserDto>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getUserSucceeded_emitCorrectUserName`() {
        //Given
        val repository = FakeUserRepositoryImpl()
        val getUserUseCase = GetUserUseCase(repository)
        //When
        val flow = getUserUseCase("token")
        //Then
        val expected = "name"
        flow.onEach { result ->
            val actual = (result as Resource.Success<UserDto>).data.username
            assertEquals(expected, actual)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getUserFailed_emitFail`() {
        //Given
        val repository = FakeUserRepositoryErrorImpl()
        val getUserUseCase = GetUserUseCase(repository)
        //When
        val flow = getUserUseCase("token")
        //Then
        flow.onEach { result ->
            assertTrue(result is Resource.Error<UserDto>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getUserFailed_emitCorrectErrorCode`() {
        //Given
        val repository = FakeUserRepositoryErrorImpl()
        val getUserUseCase = GetUserUseCase(repository)
        //When
        val flow = getUserUseCase("token")
        //Then
        val expected = 400
        flow.onEach { result ->
            val actual = (result as Resource.Error<UserDto>).statusCode.code
            assertEquals(expected, actual)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getUserExit_emitException`() {
        //Given
        val repository = FakeUserRepositoryExceptionImpl()
        val getUserUseCase = GetUserUseCase(repository)
        //When
        val flow = getUserUseCase("token")
        //Then
        flow.onEach { result ->
            assertTrue(result is Resource.Exception<UserDto>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_getUserExit_emitCorrectExceptionMessage`() {
        //Given
        val repository = FakeUserRepositoryExceptionImpl()
        val getUserUseCase = GetUserUseCase(repository)
        //When
        val flow = getUserUseCase("token")
        //Then
        val expected = "Throw new IO exception"
        flow.onEach { result ->
            val actual = (result as Resource.Exception<UserDto>).message
            assertEquals(expected, actual)
        }.launchIn(TestScope())
    }

}