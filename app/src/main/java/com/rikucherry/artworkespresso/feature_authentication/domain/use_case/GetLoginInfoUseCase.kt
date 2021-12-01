package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.common.tool.LocalResource
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginStatus
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.getStatus
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLoginInfoUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository
) {

    operator fun invoke(): Flow<LocalResource<LoginInfoItem>> = flow {
        emit(LocalResource.Loading("Requesting login information..."))
        try {
            val info = authRepository.getLoginInfo()

            if (info == null || info.getStatus() == LoginStatus.INVALID) {
                emit(LocalResource.Fail(info,"Invalid status"))
            } else {
                emit(LocalResource.Success(info))
            }
        } catch(e: Exception) {
            e.printStackTrace()
            emit(LocalResource.Exception(e.localizedMessage ?: ""))
        }
    }
}