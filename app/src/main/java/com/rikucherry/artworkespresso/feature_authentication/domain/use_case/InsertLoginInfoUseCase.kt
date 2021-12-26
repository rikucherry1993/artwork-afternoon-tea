package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertLoginInfoUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository
) {

    operator fun invoke(loginInfo: LoginInfoItem): Flow<LocalResource<LoginInfoItem>> = flow {
        emit(LocalResource.Loading("Inserting login information..."))
        try {
            authRepository.insertLoginInfo(loginInfo)
            emit(LocalResource.Success(loginInfo))
        } catch(e: Exception) {
            e.printStackTrace()
            emit(LocalResource.Exception(e.localizedMessage ?: ""))
        }
    }
}