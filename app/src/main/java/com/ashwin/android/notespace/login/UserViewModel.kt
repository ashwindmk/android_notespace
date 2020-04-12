package com.ashwin.android.notespace.login

import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import com.ashwin.android.notespace.BuildConfig
import com.ashwin.android.notespace.common.*
import com.ashwin.android.notespace.common.LOADING
import com.ashwin.android.notespace.model.LoginResult
import com.ashwin.android.notespace.model.User
import com.ashwin.android.notespace.model.repository.IUserRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(val repo: IUserRepository, uiContext: CoroutineContext) : BaseViewModel<LoginEvent<LoginResult>>(uiContext) {
    private val userState = MutableLiveData<User>()

    internal val authAttempt = MutableLiveData<Unit>()
    internal val startAnimation = MutableLiveData<Unit>()

    internal val signInStatusText = MutableLiveData<String>()
    internal val authButtonText = MutableLiveData<String>()
    internal val loginDrawable = MutableLiveData<String>()

    override fun handleEvent(event: LoginEvent<LoginResult>) {
        showLoadingState()
        when (event) {
            is LoginEvent.OnStart -> getUser()
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClick()
            is LoginEvent.OnGoogleSignInResult -> onSignInResult(event.result)
        }
    }

    private fun getUser() = launch {
        val result = repo.getCurrentUser()
        when (result) {
            is Result.Value -> {
                userState.value = result.value
                if (result.value == null) {
                    showSignedOutState()
                } else {
                    showSignedInState(result.value)
                }
            }

            is Result.Error -> showErrorState()
        }
    }

    private fun onAuthButtonClick() {
        if (userState.value == null) {
            authAttempt.value = Unit
        } else {
            signOutUser()
        }
    }

    private fun onSignInResult(result: LoginResult) = launch {
        if (result.requestCode == RC_SIGN_IN && result.userToken != null) {

            val createGoogleUserResult = repo.signInGoogleUser(result.userToken)

            // Result.Value means it was successful
            if (createGoogleUserResult is Result.Value) {
                getUser()
            } else {
                showErrorState()
            }
        } else {
            showErrorState()
        }
    }

    private fun signOutUser() = launch {
        val result = repo.signOutCurrentUser()

        when (result) {
            is Result.Value -> {
                userState.value = null
                showSignedOutState()
            }

            is Result.Error -> showErrorState()
        }
    }

    private fun showLoadingState() {
        signInStatusText.value = LOADING
        //loginDrawable.value = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/drawable/" + ANTENNA_LOOP
        startAnimation.value = Unit
    }

    private fun showSignedInState(user: User) {
        signInStatusText.value = SIGNED_IN
        authButtonText.value = SIGN_OUT
        loginDrawable.value = user.imageUrl
    }

    private fun showSignedOutState() {
        signInStatusText.value = SIGNED_OUT
        authButtonText.value = SIGN_IN
        loginDrawable.value = getUri(ANTENNA_EMPTY)
    }

    private fun showErrorState() {
        signInStatusText.value = LOGIN_ERROR
        authButtonText.value = SIGN_IN
        loginDrawable.value = getUri(ANTENNA_EMPTY)
    }

    private fun getUri(resName: String): String {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/drawable/" + resName
    }
}
