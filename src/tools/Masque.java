package tools;

import ij.process.ImageProcessor;

/**
 * Masque de convolution (carre).
 * Les coordonnees des elements du masque vont de -rayon a +rayon.
 */
public class Masque {

    private double[] contenu;
    private int rayon;
    private int largeur;
    
    public Masque(ImageProcessor image){
    	this(Math.max(image.getWidth(), image.getHeight()));
		this.remplirAvec(0); // le masque devant etre carre, il y aura des vides dans le masque si le template est rectangulaire. on remplit donc es vides avec des 0.
		for(int i=0; i<image.getWidth(); i++){
			for(int j=0; j<image.getHeight(); j++){
				this.put(i, j, image.getPixel(image.getWidth()-i, image.getHeight()-j));
			}
		}
    }

    /**
    * Cree un nouveau masque de convolution.
    * C'est un carre de cote (2 * rayon + 1).
    * Tous les elements sont a zero.
    * @param rayon     Rayon du masque de convolution.
    */
    public Masque(int rayon) {
        this(rayon, 0);
    }

    /**
    * Cree un nouveau masque de convolution.
    * C'est un carre de cote (2 * rayon + 1).
    * Tous les elements sont a 'valeurParDefaut'.
    * @param rayon               Rayon du masque de convolution.
    * @param valeurParDefaut     Valeur a mettre dans chaque element.
    */
    public Masque(int rayon, double valeurParDefaut) {
        if (rayon < 1) {
            throw new IllegalArgumentException("Le rayon doit etre >= 1");
        }

        this.rayon = rayon;
        largeur = 2 * rayon + 1;
        contenu = new double[largeur * largeur];

        for (int i = 0; i < largeur * largeur; i++) {
            contenu[i] = valeurParDefaut;
        }
    }

    /**
    * Renvoie le rayon (demie-largeur) du masque.
    * @return     Le rayon.
    */
    public int getRayon() {
        return rayon;
    }

    /**
    * Renvoie la largeur du masque (cote du carre).
    * @return     La largeur.
    */
    public int getLargeur() {
        return largeur;
    }

    /**
    * Remplit le masque avec la valeur passee en argument.
    * @param valeur     Valeur a stocker dans chaque element.
    */
    public void remplirAvec(double valeur) {
        for (int i = 0; i < largeur * largeur; i++) {
          contenu[i] = valeur;
        }
    }

    /**
    * Renvoie un element du masque.
    * @param x     Abscisse de l'element.
    * @param y     Ordonnee de l'element.
    * @return      La valeur contenue dans l'element de coordonnees (x,y).
    */
    public double get(int x, int y) {
        return contenu[ (y + rayon) * largeur + x + rayon];
    }

    /**
    * Modifie un element du masque.
    * @param x          Abscisse de l'element.
    * @param y          Ordonnee de l'element.
    * @param valeur     Valeur a inscrire dans l'element de coordonnees (x,y).
    */
    public void put(int x, int y, double valeur) {
        contenu[(y + rayon) * largeur + x + rayon] = valeur;
    }

}
