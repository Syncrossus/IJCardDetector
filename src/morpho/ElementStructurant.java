package morpho;

import ij.gui.*;
import java.util.*;

/**
 * Classe permettant de représenter un élément structurant qui pourra être
 * utilisé dans le cadre d'une opération morphologique.
 *
 * Il s'agit d'une matrice d'éléments indexés en abscisse entre xmin et xmax,
 * et en ordonnée entre ymin et ymax.
 */
public class ElementStructurant {

    private int[] contenu;

    private int xmin; // coordonnée horizontale mini
    private int xmax; // coordonnée horizontale maxi
    private int ymin; // coordonnée verticale mini
    private int ymax; // coordonnée verticale maxi

    private int largeur; // largeur de la matrice en pixels
    private int hauteur; // hauteur de la matrice en pixels

    public static final int AVANT_PLAN = 255;
    public static final int ARRIERE_PLAN = 0;
    public static final int INDEFINI = -1;

    /**
    * Crée un nouvel élément structurant dont le domaine de définition
    * est donné en argument.
    * @param xmin  Coordonnée horizontale mini
    * @param xmax  Coordonnée horizontale maxi
    * @param ymin  Coordonnée verticale mini
    * @param ymax  Coordonnée verticale maxi
    */
    public ElementStructurant(int xmin, int xmax, int ymin, int ymax) {
        if (xmax < xmin)
            throw new IllegalArgumentException("xmax doit être >= à xmin");

        if (ymax < ymin)
            throw new IllegalArgumentException("ymax doit être >= à ymin");

        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;

        largeur = xmax - xmin + 1;
        hauteur = ymax - ymin + 1;

        contenu = new int[largeur * hauteur];
        Arrays.fill(contenu, INDEFINI);
    }

    /**
    * Crée un nouvel élément structurant rectangulaire centré (à condition que
    * les dimensions soient impaires...)
    * (Tous les points appartiennent à l'avant-plan.)
    * @param largeur  Largeur du rectangle
    * @param hauteur  Hauteur du rectangle
    * @return         L'élément structurant ainsi créé
    */
    public static ElementStructurant creerRectangleCentre(int largeur, int hauteur) {
        int xmax = largeur / 2;
        int xmin = xmax - largeur + 1;
        int ymax = hauteur / 2;
        int ymin = ymax - hauteur + 1;

        ElementStructurant es = new ElementStructurant(xmin, xmax, ymin, ymax);
        es.remplirAvec(AVANT_PLAN);

        return es;
    }

    /**
    * Crée un nouvel élément structurant circulaire centré (à condition que le
    * diamètre soit impair...)
    * @param diametre  Diamètre du cercle
    * @return          L'élément structurant ainsi créé
    */
    public static ElementStructurant creerCercleCentre(int diametre) {
        int xymax = diametre / 2;
        int xymin = xymax - diametre + 1;
        ElementStructurant es = new ElementStructurant(xymin, xymax, xymin, xymax);

        OvalRoi roi = new OvalRoi(xymin, xymin, diametre, diametre);
        for (int y = xymin; y <= xymax; y++) {
            for (int x = xymin; x <= xymax; x++) {
                if (roi.contains(x, y))
                    es.put(x, y, AVANT_PLAN);
            }
        }

        return es;
    }

    /**
    * Renvoie la coordonnée horizontale mini
    * @return xmin
    */
    public int getXmin() {
        return xmin;
    }

    /**
    * Renvoie la coordonnée horizontale maxi
    * @return xmax
    */
    public int getXmax() {
        return xmax;
    }

    /**
    * Renvoie la coordonnée verticale mini
    * @return ymin
    */
    public int getYmin() {
        return ymin;
    }

    /**
    * Renvoie la coordonnée verticale maxi
    * @return ymax
    */
    public int getYmax() {
        return ymax;
    }

    /**
    * Renvoie la largeur en pixels de l'élément structurant
    * @return largeur
    */
    public int getLargeur() {
        return largeur;
    }

    /**
    * Renvoie la hauteur en pixels de l'élément structurant
    * @return hauteur
    */
    public int getHauteur() {
        return hauteur;
    }

    /**
    * Renvoie le point (x,y) de l'élément structurant.
    * La valeur retournée peut être soit:
    * - ElementStructurant.AVANT_PLAN
    * - ElementStructurant.ARRIERE_PLAN
    * - ElementStructurant.INDEFINI
    * @param x  Coordonnée horizontale du point à retourner
    * @param y  Coordonnée verticale du point à retourner
    * @return   La valeur stockée aux coordonnées (x,y)
    */
    public int get(int x, int y) {
        verifie(x, y);
        return contenu[(y - ymin) * largeur + (x - xmin)];
    }

    /**
    * Modifie la valeur stockée aux coordonnées (x,y) dans l'élément structurant.
    * Les trois valeurs possibles sont:
    * - ElementStructurant.AVANT_PLAN
    * - ElementStructurant.ARRIERE_PLAN (pas aujourd'hui)
    * - ElementStructurant.INDEFINI
    * @param x       Coordonnée horizontale du point à modifier
    * @param y       Coordonnée verticale du point à modifier
    * @param valeur  La valeur à stocker en (x,y)
    */
    public void put(int x, int y, int valeur) {

        verifie(x, y);
        if (valeur != AVANT_PLAN && valeur != ARRIERE_PLAN && valeur != INDEFINI)
            throw new IllegalArgumentException(
                "Les valeurs possibles sont ElementStructurant.AVANT_PLAN," +
                " ElementStructurant.ARRIERE_PLAN et ElementStructurant.INDEFINI");

        contenu[(y - ymin) * largeur + x - xmin] = valeur;

    }

    /**
    * Remplit la totalité de l'élément structurant avec la valeur passée
    * en argument. Les valeurs acceptées sont les mêmes que pour la méthode
    * put(int, int, int).
    * @param valeur  La valeur à stocker en tout point de la matrice.
    */
    public void remplirAvec(int valeur) {

        if (valeur != AVANT_PLAN && valeur != ARRIERE_PLAN && valeur != INDEFINI)
            throw new IllegalArgumentException(
                "Les valeurs possibles sont ElementStructurant.AVANT_PLAN," +
                " ElementStructurant.ARRIERE_PLAN et ElementStructurant.INDEFINI");

        Arrays.fill(contenu, valeur);

    }

    /**
    * Remplit la matrice avec les éléments du tableau passé en argument.
    * La taille du tableau doit être strictement égale aux nombres d'éléments
    * de la matrice. Les valeurs acceptées sont les mêmes que pour la méthode
    * pu(int, int, int).
    * La matrice sera remplie de la gauche vers la droite puis de haut en bas
    * (sens de la lecture).
    * @param contenu  Liste des valeurs à stocker dans la matrice
    */
    public void remplirAvec(int[] contenu) {

        if (contenu.length != this.contenu.length)
            throw new IllegalArgumentException("Le tableau doit avoir le même " +
                "d'éléments que l'élément structurant");

        for (int i = 0; i < contenu.length; i++) {
            if (contenu[i] != AVANT_PLAN && contenu[i] != ARRIERE_PLAN &&
                contenu[i] != INDEFINI)
                throw new IllegalArgumentException(
                  "Les valeurs possibles sont ElementStructurant.AVANT_PLAN," +
                  " ElementStructurant.ARRIERE_PLAN et ElementStructurant.INDEFINI");
        }

        for (int i = 0; i < contenu.length; i++) {
            this.contenu[i] = contenu[i];
        }

    }

    /**
    * Renvoie le symétrique de cet élément structurant.
    * @return Le symétrique de 'this'.
    */
    public ElementStructurant symetrique() {
        ElementStructurant sym = new ElementStructurant(-xmax, -xmin, -ymax, -ymin);
        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                sym.put(-x, -y, get(x, y));
            }
        }
        return sym;
    }

    /**
    * Vérifie qu'un point (x,y) appartient au domaine de définition de
    * l'élément structurant.
    * @param x  Coordonnée horizontale à tester.
    * @param y  Coordonnée verticale à tester.
    */
    private void verifie(int x, int y) {
        if (x < xmin)
            throw new IllegalArgumentException(
                "x ne peut pas valoir " + x + " puisque xmin vaut " + xmin);

        if (x > xmax)
            throw new IllegalArgumentException(
                "x ne peut pas valoir " + x + " puisque xmax vaut " + xmax);

        if (y < ymin)
            throw new IllegalArgumentException(
                "y ne peut pas valoir " + y + " puisque ymin vaut " + ymin);

        if (y > ymax)
            throw new IllegalArgumentException(
                "y ne peut pas valoir " + y + " puisque ymax vaut " + ymax);
    }

}
