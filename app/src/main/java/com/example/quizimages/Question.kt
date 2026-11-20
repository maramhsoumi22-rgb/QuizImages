package com.example.quizimages

/**
 * Structure de données représentant une question du quiz
 */
data class Question(
    val texte: String,
    val imageResId: Int,
    val choix: List<String>,
    val bonneReponseIndex: Int,
    val descriptionImage: String
)
