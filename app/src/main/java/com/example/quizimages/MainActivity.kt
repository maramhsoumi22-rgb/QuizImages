package com.example.quizimages

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // 5 questions statiques sur le thème des formes géométriques
    private val questions = listOf(
        Question(
            texte = "Combien de côtés possède cette forme géométrique rose ?",
            imageResId = R.drawable.triangle,
            choix = listOf("3 côtés (Triangle)", "4 côtés (Carré)", "5 côtés (Pentagone)"),
            bonneReponseIndex = 0,
            descriptionImage = "Un triangle équilatéral rose à 3 côtés"
        ),
        Question(
            texte = "Quelle est cette forme géométrique bleue aux 4 côtés égaux ?",
            imageResId = R.drawable.carre,
            choix = listOf("Rectangle", "Carré", "Losange"),
            bonneReponseIndex = 1,
            descriptionImage = "Un carré bleu régulier"
        ),
        Question(
            texte = "Quelle figure géométrique verte n'a aucun sommet ?",
            imageResId = R.drawable.cercle,
            choix = listOf("Ovale", "Hexagone", "Cercle"),
            bonneReponseIndex = 2,
            descriptionImage = "Un cercle vert parfait"
        ),
        Question(
            texte = "Combien de branches possède cette étoile jaune ?",
            imageResId = R.drawable.etoile,
            choix = listOf("4 branches", "5 branches", "6 branches"),
            bonneReponseIndex = 1,
            descriptionImage = "Une étoile jaune à 5 branches"
        ),
        Question(
            texte = "Quelle est cette figure violette dont les côtés opposés sont parallèles et de même longueur ?",
            imageResId = R.drawable.rectangle,
            choix = listOf("Rectangle", "Trapèze", "Parallélogramme quelconque"),
            bonneReponseIndex = 0,
            descriptionImage = "Un rectangle violet horizontal"
        )
    )

    // Variables d'état
    private var indexQuestionCourante = 0
    private var score = 0
    private var aRepondu = false
    private var finDePartie = false

    // Vues graphiques
    private lateinit var txtScore: TextView
    private lateinit var progressQuiz: ProgressBar
    private lateinit var txtNumero: TextView
    private lateinit var imgQuestion: ImageView
    private lateinit var txtQuestion: TextView
    private lateinit var btnChoix1: Button
    private lateinit var btnChoix2: Button
    private lateinit var btnChoix3: Button
    private lateinit var txtFeedback: TextView
    private lateinit var btnNavigation: Button
    private lateinit var layoutQuestionActive: LinearLayout
    private lateinit var layoutFinPartie: LinearLayout
    private lateinit var txtScoreFinal: TextView
    private lateinit var btnRejouer: Button

    companion object {
        private const val KEY_INDEX = "key_index"
        private const val KEY_SCORE = "key_score"
        private const val KEY_REPONDU = "key_repondu"
        private const val KEY_FIN = "key_fin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupération des composants par leurs identifiants
        txtScore = findViewById(R.id.txt_score)
        progressQuiz = findViewById(R.id.progress_quiz)
        txtNumero = findViewById(R.id.txt_numero)
        imgQuestion = findViewById(R.id.img_question)
        txtQuestion = findViewById(R.id.txt_question)
        btnChoix1 = findViewById(R.id.btn_choix1)
        btnChoix2 = findViewById(R.id.btn_choix2)
        btnChoix3 = findViewById(R.id.btn_choix3)
        txtFeedback = findViewById(R.id.txt_feedback)
        btnNavigation = findViewById(R.id.btn_navigation)
        layoutQuestionActive = findViewById(R.id.layout_question_active)
        layoutFinPartie = findViewById(R.id.layout_fin_partie)
        txtScoreFinal = findViewById(R.id.txt_score_final)
        btnRejouer = findViewById(R.id.btn_rejouer)

        // Restauration d'état après rotation
        if (savedInstanceState != null) {
            indexQuestionCourante = savedInstanceState.getInt(KEY_INDEX, 0)
            score = savedInstanceState.getInt(KEY_SCORE, 0)
            aRepondu = savedInstanceState.getBoolean(KEY_REPONDU, false)
            finDePartie = savedInstanceState.getBoolean(KEY_FIN, false)
        }

        // Configuration des clics de choix
        btnChoix1.setOnClickListener { validerChoix(0) }
        btnChoix2.setOnClickListener { validerChoix(1) }
        btnChoix3.setOnClickListener { validerChoix(2) }

        // Navigation (Suivant ou Voir le résultat)
        btnNavigation.setOnClickListener {
            if (indexQuestionCourante < questions.size - 1) {
                indexQuestionCourante++
                aRepondu = false
                afficherQuestionCourante()
            } else {
                finDePartie = true
                afficherFinDePartie()
            }
        }

        // Rejouer
        btnRejouer.setOnClickListener {
            recommencerPartie()
        }

        // Initialisation de l'affichage
        if (finDePartie) {
            afficherFinDePartie()
        } else {
            afficherQuestionCourante()
        }
    }

    private fun afficherQuestionCourante() {
        layoutQuestionActive.visibility = View.VISIBLE
        layoutFinPartie.visibility = View.GONE

        val question = questions[indexQuestionCourante]

        // Mise à jour de l'en-tête et progression
        txtScore.text = getString(R.string.format_score, score, questions.size)
        txtNumero.text = getString(R.string.format_question_numero, indexQuestionCourante + 1, questions.size)
        progressQuiz.progress = if (aRepondu) indexQuestionCourante + 1 else indexQuestionCourante

        // Mise à jour de l'image et accessibilité
        imgQuestion.setImageResource(question.imageResId)
        imgQuestion.contentDescription = question.descriptionImage

        // Texte de la question et choix
        txtQuestion.text = question.texte
        btnChoix1.text = question.choix[0]
        btnChoix2.text = question.choix[1]
        btnChoix3.text = question.choix[2]

        if (!aRepondu) {
            // État avant réponse
            btnChoix1.isEnabled = true
            btnChoix2.isEnabled = true
            btnChoix3.isEnabled = true
            txtFeedback.visibility = View.INVISIBLE
            btnNavigation.visibility = View.GONE
        } else {
            // Déjà répondu (ex: après rotation)
            desactiverBoutonsChoix()
            btnNavigation.visibility = View.VISIBLE
            btnNavigation.text = if (indexQuestionCourante == questions.size - 1) {
                getString(R.string.btn_voir_resultat)
            } else {
                getString(R.string.btn_suivant)
            }
        }
    }

    private fun validerChoix(choixIndex: Int) {
        if (aRepondu) return
        aRepondu = true

        val question = questions[indexQuestionCourante]
        val estCorrect = (choixIndex == question.bonneReponseIndex)

        if (estCorrect) {
            score++
            txtFeedback.text = getString(R.string.feedback_correct)
            txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.color_correct))
        } else {
            val bonneReponseTexte = question.choix[question.bonneReponseIndex]
            txtFeedback.text = getString(R.string.feedback_incorrect, bonneReponseTexte)
            txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.color_incorrect))
        }

        txtFeedback.visibility = View.VISIBLE

        // Mise à jour immédiate du score et de la progression
        txtScore.text = getString(R.string.format_score, score, questions.size)
        progressQuiz.progress = indexQuestionCourante + 1

        // Désactivation immédiate des boutons pour empêcher de cliquer plusieurs fois
        desactiverBoutonsChoix()

        // Affichage du bouton de navigation adapté
        btnNavigation.visibility = View.VISIBLE
        btnNavigation.text = if (indexQuestionCourante == questions.size - 1) {
            getString(R.string.btn_voir_resultat)
        } else {
            getString(R.string.btn_suivant)
        }
    }

    private fun desactiverBoutonsChoix() {
        btnChoix1.isEnabled = false
        btnChoix2.isEnabled = false
        btnChoix3.isEnabled = false
    }

    private fun afficherFinDePartie() {
        layoutQuestionActive.visibility = View.GONE
        layoutFinPartie.visibility = View.VISIBLE

        progressQuiz.progress = questions.size
        txtScore.text = getString(R.string.format_score, score, questions.size)
        txtScoreFinal.text = getString(R.string.format_score_final, score, questions.size)
    }

    private fun recommencerPartie() {
        indexQuestionCourante = 0
        score = 0
        aRepondu = false
        finDePartie = false
        afficherQuestionCourante()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, indexQuestionCourante)
        outState.putInt(KEY_SCORE, score)
        outState.putBoolean(KEY_REPONDU, aRepondu)
        outState.putBoolean(KEY_FIN, finDePartie)
    }
}
