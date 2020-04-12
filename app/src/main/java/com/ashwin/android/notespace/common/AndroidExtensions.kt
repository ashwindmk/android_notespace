package com.ashwin.android.notespace.common

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Fragment.makeToast(value: String) {
    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()
}

internal fun AnimationDrawable.startWithFade() {
    this.setEnterFadeDuration(1000)
    this.setExitFadeDuration(1000)
    this.start()
}

internal fun Fragment.hideKeyboard() {
    val parentActivity = requireActivity()
    val inputMethodManager = parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView = parentActivity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
