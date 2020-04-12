package com.ashwin.android.notespace.login

import android.content.ContentResolver
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ashwin.android.notespace.R
import com.ashwin.android.notespace.common.ANTENNA_LOOP
import com.ashwin.android.notespace.common.DEBUG_TAG
import com.ashwin.android.notespace.common.RC_SIGN_IN
import com.ashwin.android.notespace.common.startWithFade
import com.ashwin.android.notespace.model.LoginResult
import com.ashwin.android.notespace.note.NoteActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*

class LoginView : Fragment() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()
        userViewModel = ViewModelProvider(this, LoginInjector(requireActivity().application).provideUserViewModelFactory()).get(UserViewModel::class.java)

        (root_fragment_login.background as AnimationDrawable).startWithFade()

        login_status_imageview.setImageResource(R.drawable.antenna_half)

        setUpClickListeners()
        setUpObservers()

        userViewModel.handleEvent(LoginEvent.OnStart)
    }

    private fun setUpClickListeners() {
        auth_attempt_button.setOnClickListener {
            userViewModel.handleEvent(LoginEvent.OnAuthButtonClick)
        }

        back_imagebutton.setOnClickListener {
            startListActivity()
        }

        // Handle OS back button click
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startListActivity()
            }
        })
    }

    private fun startListActivity() {
        requireActivity().startActivity(Intent(activity, NoteActivity::class.java))
        //requireActivity().finish()
    }

    private fun setUpObservers() {
        userViewModel.signInStatusText.observe(viewLifecycleOwner, Observer<String> { status ->
            login_status_textview.text = status
        })

        userViewModel.authButtonText.observe(viewLifecycleOwner, Observer {
            auth_attempt_button.text = it
        })

        userViewModel.startAnimation.observe(viewLifecycleOwner, Observer {
            login_status_imageview.setImageResource(resources.getIdentifier(ANTENNA_LOOP, "drawable", activity?.packageName))
            (login_status_imageview.drawable as AnimationDrawable).start()
        })

        userViewModel.authAttempt.observe(viewLifecycleOwner, Observer {
            startSignInFlow() }
        )

        userViewModel.loginDrawable.observe(viewLifecycleOwner, Observer {
            if (isLocalResource(it)) {
                login_status_imageview.setImageURI(Uri.parse(it))
            } else {
                Glide.with(this).load(it).into(login_status_imageview)
            }
        })
    }

    private fun isLocalResource(str: String): Boolean {
        return str.startsWith(ContentResolver.SCHEME_ANDROID_RESOURCE)
    }

    private fun startSignInFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var userToken: String? = null
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null) {
                    userToken = account.idToken
                }
            } catch (e: Exception) {
                Log.e(DEBUG_TAG, "Exception while logging in", e)
            }

            userViewModel.handleEvent( LoginEvent.OnGoogleSignInResult<LoginResult>(LoginResult(requestCode, userToken)) )
        }
    }
}
