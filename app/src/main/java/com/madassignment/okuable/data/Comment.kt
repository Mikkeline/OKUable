package com.madassignment.okuable.data


import java.util.Date


data class Comment(
    var comment: String = "",
    var timeStamp: Date? = null,
    var username: String = "",

    var dlUrl: String = ""
)
