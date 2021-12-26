package com.rikucherry.artworkespresso.feature_authentication.domain.use_case

import com.rikucherry.artworkespresso.common.data.remote.UserDto
import com.rikucherry.artworkespresso.common.tool.Resource
import com.rikucherry.artworkespresso.feature_authentication.data.repository.UserRepository
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    ) {

    operator fun invoke(token: String): Flow<Resource<UserDto>> = flow {
        emit(Resource.Loading("Requesting current user info..."))
        val user = userRepository.getWhoAmI(token)
        user.suspendOnSuccess {
            emit(Resource.Success<UserDto>(data, statusCode))
        }.suspendOnError {
            emit(Resource.Error<UserDto>(statusCode, toString()))
        }.suspendOnException {
            emit(Resource.Exception<UserDto>(message ?: "Undefined exception."))
        }

    }

}