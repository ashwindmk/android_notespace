package com.ashwin.android.notespace.model.repository

import com.ashwin.android.notespace.common.Result
import com.ashwin.android.notespace.model.User

interface IUserRepository {
    suspend fun getCurrentUser(): Result<Exception, User?>

    suspend fun signOutCurrentUser(): Result<Exception, Unit>

    suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit>
}
