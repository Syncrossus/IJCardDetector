Projet traitement d'images - jeu de données
-------------------------------------------

Ce jeu de données est composé de 90 images de cartes à jouer, réparties dans
plusieurs dossiers correspondant à différentes difficultés. Toutes les images
sont au format JPG, en couleur (RGB) et codées sur 8 bits. Elles ont été
redimensionnées pour que leur taille ne dépasse pas 1000x1000 pixels.

Pour chaque dossier (à l'exception de 'multiple'), les fichiers respectent la
convention de nommage suivante :
<id>-<dossier>_<valeur>-<couleur>.jpg

La vérité terrain est donc obtenue de la manière suivante :
<valeur> correspond à la valeur de la carte allant de 1 à 13.
<couleur> correspond à 'carreau', 'coeur', 'pique' ou 'trefle'

Pour le dossier 'multiple', les images peuvent contenir plusieurs cartes, ou ne
pas en contenir du tout.

Lorsqu'une image ne contient aucune carte, le nom est le suivant :
<id>-<dossier>-00-00.jpg

Lorsqu'une image contient plusieurs cartes, elles sont listées à la suite avec
le caractère '_' comme séparateur. Par exemple, une image contenant deux cartes
est nommée de la manière suivante :
<id>-<dossier>_<valeur1>-<couleur1>_<valeur2>-<couleur2>.jpg

Bon travail !
