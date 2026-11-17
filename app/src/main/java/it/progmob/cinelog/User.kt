package it.progmob.cinelog.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val role: String = "user", // user, critica, admin
    val createdAt: Long = 0L
)