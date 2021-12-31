package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryExceptionImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
class InsertLoginInfoUseCaseTest {

    private val loginInfo = mockkClass(LoginInfoItem::class)

    @Test
    fun `Invoke_insertLoginInfoSucceeded_emitSuccess`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val insertLoginInfoUseCase = InsertLoginInfoUseCase(repository)
        //When
        val flow = insertLoginInfoUseCase(loginInfo)
        //Then
        flow.onEach { result ->
            assertTrue(result is LocalResource.Success<LoginInfoItem>)
        }.launchIn(TestScope())
    }

    @Test
    fun `Invoke_insertLoginInfoExit_emitException`() {
        //Given
        val repository = FakeAuthenticationRepositoryExceptionImpl()
        val insertLoginInfoUseCase = InsertLoginInfoUseCase(repository)
        //When
        val flow = insertLoginInfoUseCase(loginInfo)
        //Then
        flow.onEach { result ->
            assertTrue(result is LocalResource.Exception<LoginInfoItem>)
        }.launchIn(TestScope())
    }

}