package it.progmob.cinelog

data class Review(
    val id: String = "",
    val contentId: String = "",
    val userId: String = "",
    val userDisplayName: String = "",
    val kind: String = "u",             // u = user, c = critica
    val rating: Int = 0,                // voto da 0-10
    val text: String = "",
    val isHidden: Boolean = false,      // true = segnalata/cancellata
    val createdAt: Long = 0L
)