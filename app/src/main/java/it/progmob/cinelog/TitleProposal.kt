package it.progmob.cinelog.model

data class TitleProposal(
    val id: String = "",
    val title: String = "",
    val kind: String = "f",         // f = film; s = serie
    val release: String = "",
    val director: String = "",
    val note: String = "",
    val proposedBy: String = "",
    val status: String = "p",       // p = pending o a = approved, r = rejected
    val createdAt: Long = 0L
)