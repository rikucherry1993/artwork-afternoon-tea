package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryErrorImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryExceptionImpl
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.FakeAuthenticationRepositoryImpl
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
@Config(manifest= Config.NONE)
class GetLoginInfoUseCaseTest {

    @Test
    fun `Invoke_getLoginInfoSucceeded_emitSuccess`() {
        //Given
        val repository = FakeAuthenticationRepositoryImpl()
        val getLoginInfoUseCase = GetLoginInfoUseCase(repository)
        //When
        val flow = getLoginInfoUseCase()
        //Then
        flow.onEach {
                result -> assertTrue(result is LocalResource.Success<LoginInfoItem>)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getLoginInfoFailed_emitFail`() {
        //Given
        val repository = FakeAuthenticationRepositoryErrorImpl()
        val getLoginInfoUseCase = GetLoginInfoUseCase(repository)
        //When
        val flow = getLoginInfoUseCase()
        //Then
        flow.onEach {
                result -> assertTrue(result is LocalResource.Fail<LoginInfoItem>)
        }.launchIn(TestScope())
    }


    @Test
    fun `Invoke_getLoginInfoExit_emitException`() {
        //Given
        val repository = FakeAuthenticationRepositoryExceptionImpl()
        val getLoginInfoUseCase = GetLoginInfoUseCase(repository)
        //When
        val flow = getLoginInfoUseCase()
        //Then
        flow.onEach {
                result -> assertTrue(result is LocalResource.Exception<LoginInfoItem>)
        }.launchIn(TestScope())
    }


}