# Compte Rendu - TP Android Quiz en images

**Étudiant** : Maram Hsoumi  
**Thème choisi** : Formes Géométriques  

---

## 1. Présentation de l'interface et widgets utilisés
L'interface a été conçue sans fragment ni Jetpack Compose, dans une activité unique `MainActivity` avec :
- `TextView` : titre (`txt_titre`), score courant en haut à droite (`txt_score`), numéro d'étape (`txt_numero`), libellé de question (`txt_question`), retour de réponse (`txt_feedback`) et score final (`txt_score_final`).
- `ProgressBar` horizontal : pour la progression globale (0 à 5).
- `ImageView` : pour l'affichage sans déformation des formes (`adjustViewBounds="true"`, `scaleType="fitCenter"`).
- `Button` : `btn_choix1`, `btn_choix2`, `btn_choix3` pour les alternatives, `btn_navigation` pour la suite, et `btn_rejouer` pour redémarrer.
- `ScrollView` englobant le `ConstraintLayout` pour s'adapter à toutes les résolutions d'écran.

---

## 2. Structure des données et ressources
Les 5 questions statiques sont définies via la data class `Question` et stockées dans une liste Kotlin immuable. Chaque question pointe vers sa ressource vectorielle dédiée dans `res/drawable/` (`triangle.xml`, `carre.xml`, `cercle.xml`, `etoile.xml`, `rectangle.xml`).

---

## 3. Logique événementielle et calculs
- Validation événementielle au clic sur chaque bouton avec transmission de son index.
- Distinction stricte entre la progression (qui incrémente systématiquement) et le score (qui n'incrémente qu'en cas de choix juste).
- Les boutons sont immédiatement désactivés pour interdire les points en double.
- Passage fluide à l'écran de fin de partie dans le même layout.
