package com.ashwin.android.notespace.model

data class FirebaseNote(
    val creationDate: String? = "",
    val contents: String? = "",
    val upVotes: Int? = 0,
    val imageurl: String? = "",
    val creator: String? = ""
)
