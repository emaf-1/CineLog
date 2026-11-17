package it.progmob.cinelog.model

data class Content(
    val id: String = "",
    val title: String = "",
    val kind: String = "f",         // f = film; s = serie
    val release: String = "",       // data "YYYY-MM-DD"
    val synopsis: String = "",
    val genre: String = "",
    val director: String = "",      // nome regista
    val posterUrl: String = "",
    val status: String = "approved", // approved o pending, default approved
    val proposedBy: String = "",
    val avgUserRating: Double = 0.0,
    val avgCriticRating: Double = 0.0,
    val createdAt: Long = 0L
)