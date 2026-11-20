# TP Android — Créer un quiz en images

**Étudiant** : Maram Hsoumi  
**Matière** : Développement Mobile Android Kotlin  
**Projet** : QuizImages  

---

## 📌 Présentation du projet
Ce projet consiste en un jeu de quiz illustré composé de 5 questions sur le thème des **formes géométriques** (triangle, carré, cercle, étoile, rectangle).
- **Langage** : Kotlin
- **Architecture** : Single Activity (`MainActivity`), sans fragment, sans Jetpack Compose
- **Interface** : Widgets déclarés en XML avec prise en charge du défilement (`ScrollView`) pour les petits écrans

---

## 🚀 Fonctionnalités implémentées
1. **Affichage dynamique** :
   - Image vectorielle haute résolution sans déformation (`scaleType="fitCenter"`).
   - Intitulé de la question et 3 choix de réponses sous forme de boutons.
   - En-tête avec titre, numéro de question et score dynamique en haut à droite.
   - Barre de progression horizontale (`ProgressBar`) mesurant le nombre de questions répondues.
2. **Gestion des interactions** :
   - Évaluation immédiate de la réponse au clic (message de correction textuel et coloré).
   - Désactivation immédiate des 3 boutons de choix pour empêcher les clics multiples.
   - Navigation conditionnelle : bouton « Question suivante » pour les 4 premières questions, et « Voir le résultat » à la 5ème question.
3. **Fin de partie & Rejouabilité** :
   - Masquage des choix et affichage du score final dans la même activité.
   - Bouton « Rejouer » réinitialisant l'index, le score et la barre de progression.
4. **Conservation d'état** : Sauvegarde et restauration via `onSaveInstanceState`.

---

## ❓ Réponses aux Questions du Compte Rendu (Section 3)

### 1. Comment représentez-vous une question et sa bonne réponse ?
Une question est représentée par une `data class Question` contenant :
- `texte: String` : L'intitulé de la question.
- `imageResId: Int` : L'identifiant de la ressource drawable (`R.drawable.triangle`, etc.).
- `choix: List<String>` : Une liste ordonnée de 3 propositions.
- `bonneReponseIndex: Int` : L'indice (0, 1 ou 2) de la proposition correcte.
- `descriptionImage: String` : Une description destinée à l'accessibilité (`contentDescription`).

### 2. Comment le clic sur un bouton permet-il de savoir quel choix a été sélectionné ?
Chaque bouton possède son propre gestionnaire d'événement `setOnClickListener` qui invoque la fonction `validerChoix(choixIndex: Int)` en transmettant directement l'indice correspondant (0 pour `btn_choix1`, 1 pour `btn_choix2`, 2 pour `btn_choix3`). Cet indice est ensuite comparé à `question.bonneReponseIndex`.

### 3. Comment empêchez-vous l’attribution de plusieurs points pour une question ?
1. Un booléen d'état `aRepondu` est immédiatement basculé à `true` dès le premier clic, et un contrôle au début de `validerChoix` interrompt la fonction si `aRepondu` est déjà vrai.
2. Les trois boutons de choix sont désactivés graphiquement (`isEnabled = false`) dès la validation, empêchant tout clic subséquent.

### 4. Quelle différence faites-vous entre le score et la progression ?
- **La progression** quantifie l'avancement dans le questionnaire (nombre de questions auxquelles l'utilisateur a répondu, qu'elles soient justes ou fausses). Elle augmente de 1 à chaque validation et atteint toujours 5/5 à la fin.
- **Le score** représente le nombre exact de **bonnes réponses** obtenues (de 0 à 5). Une réponse fausse fait avancer la progression mais ne modifie pas le score.

### 5. Comment détectez-vous la dernière question et affichez-vous le résultat ?
La détection s'effectue en comparant l'index courant à la taille du tableau : `indexQuestionCourante == questions.size - 1`.
- Lorsque cette condition est vérifiée, le texte du bouton de navigation devient « Voir le résultat ».
- Au clic sur ce bouton, on bascule `layoutQuestionActive.visibility = View.GONE` et `layoutFinPartie.visibility = View.VISIBLE` pour afficher le score final dans la même activité.

---

## 🧪 Cahier de tests validés

| Scénario de test | Résultat attendu | Statut |
| :--- | :--- | :---: |
| Démarrage nouvelle partie | Question 1, 3 choix actifs, score 0/5, progression 0/5 | ✅ Validé |
| Réponse correcte | Score incrémenté (+1), progression (+1), message vert | ✅ Validé |
| Réponse incorrecte | Score inchangé, progression (+1), message rouge | ✅ Validé |
| Clics multiples sur un bouton | Aucun point supplémentaire, boutons désactivés | ✅ Validé |
| Question suivante | Image et textes actualisés, score conservé | ✅ Validé |
| 5 bonnes réponses consécutives | Score final 5/5, barre à 5/5 | ✅ Validé |
| 5 mauvaises réponses consécutives | Score final 0/5, barre à 5/5 | ✅ Validé |
| Clic sur « Rejouer » | Retour à la question 1, score et barre remis à 0 | ✅ Validé |
| Test sur petit écran / rotation | Aucun élément tronqué grâce au ScrollView, état préservé | ✅ Validé |
