package tools;

import ij.*;
import ij.gui.*;
import ij.process.*;

public class Convolution {

	/**
	 * Effectue la convolution de l'image 'ip' avec un masque carre.
	 * Le resultat d'un produit de convolution n'est pas forcement dans le meme
	 * domaine de definition que l'image d'origine. C'est pourquoi le resultat
	 * est stocke dans une matrice de nombres reels.
	 * @param ip        L'image a convoluer.
	 * @param masque    Le masque de convolution.
	 * @return          La matrice resultat.
	 */
	public static double[][] convoluer(ImageProcessor ip, Masque masque) {
		/**
		 * A faire: effectuer la convolution.
		 * Reflechir a la question des bords.
		 */
		// resultat: la matrice dans laquelle sera stocke le resultat de la convolution.
		double[][] resultat = new double[ip.getWidth()][ip.getHeight()];

		for(int i=0; i<ip.getWidth(); i++){
			for(int j=0; j<ip.getHeight(); j++){
				resultat[i][j] = appliquerMasque(ip, masque, i, j);
			}
		}
		/**
		 * Fin de la partie a completer
		 */
		return resultat;
	}

	/**
	 * Retourne la valeur du pixel (col, line) modifié par le masque de convolution .
	 * @param ip l'image d'origine
	 * @param masque le masque à appliquer
	 * @param col la colonne du pixel à modifier
	 * @param line la ligne du pixel à modifier
	 * @return la nouvelle valeur du pixel
	 */
	public static double appliquerMasque(ImageProcessor ip, Masque masque, int col, int line){
		int rayon = masque.getRayon();
		double valeur = 0;

		for(int i=-rayon; i<=rayon; i++){
			for(int j=-rayon; j<=rayon; j++){
				int x = col+i,  y = line+j;

				if(x<0){
					x=0;
				}
				else if(x>ip.getWidth()-1){
					x = ip.getWidth()-1;
				}

				if(y<0){
					y=0;
				}
				else if(y>ip.getHeight()-1){
					y = ip.getHeight()-1;
				}

				valeur += ip.getPixel(x,y)*masque.get(-i, -j);
			}
		}

		return valeur;
	}
	
	/**
	 * @param template : a template image to find in the target image
	 * @param image : the image in which to search for the template
	 * @return a matrix with a match percentage for each position the template can occupy on the image
	 */
	public static double[][] correlationCroisee(ImageProcessor template, ImageProcessor image){
		// La correlation croisee est une convolution sans symétrisation.
		//Nous allons donc utiliser la convolution, et symétriser notre masque avant pour compenser la symétrisation de la convolution.
		Masque m = new Masque(Math.max(template.getWidth(), template.getHeight()));
		m.remplirAvec(0); // le masque devant etre carre, il y aura des vides dans le masque si le template est rectangulaire. on remplit donc es vides avec des 0.
		for(int i=0; i<template.getWidth(); i++){
			for(int j=0; j<template.getHeight(); j++){
				m.put(i, j, template.getPixel(template.getWidth()-i, template.getHeight()-j));
			}
		}
		return Convolution.convoluer(image, m);
	}
	
	/**
     * Affiche une matrice de nombres reels dans une nouvelle fenetre.
     * Comme les elements de cette matrice ne sont pas forcement dans le
     * domaine [0..255], on a le choix entre:
     * 1) normaliser: c'est-a-dire faire une mise a l'echelle de maniere a ce
     * que la valeur la plus faible soit 0 et la valeur la plus haute 255.
     * (voir TP1: etirement d'histogramme).
     * 2) ne pas normaliser: tous les elements dont la valeur est inferieure a 0
     * sont fixes a 0 et tous les elements dont la valeur est superieure a 255
     * sont fixes a 255.
     * @param mat            La matrice a afficher.
     * @param titre          Le titre de la fenetre.
     * @param normaliser     Faut-il normaliser ?
     */
    public static void afficheMatrice(double[][] mat, String titre,
            boolean normaliser) {

        ImagePlus imp = NewImage.createByteImage(
                titre, mat.length, mat[0].length, 1, NewImage.FILL_BLACK);
        ImageProcessor ip = imp.getProcessor();

        if (normaliser) {
            double max = mat[0][0];
            double min = mat[0][0];
            for (int y = 0; y < mat[0].length; y++) {
                for (int x = 0; x < mat.length; x++) {
                    if (mat[x][y] > max) max = mat[x][y];
                    if (mat[x][y] < min) min = mat[x][y];
                }
            }

            if (min != max) {
                for (int y = 0; y < mat[0].length; y++) {
                    for (int x = 0; x < mat.length; x++) {
                        ip.putPixel(x, y,
                            (int) ((255 * (mat[x][y] - min)) / (max - min)));
                    }
                }
            }
        }

        else {
            for (int y = 0; y < mat[0].length; y++) {
                for (int x = 0; x < mat.length; x++) {
                    int p = (int) Math.min(mat[x][y], 255);
                    p = Math.max(p, 0);
                    ip.putPixel(x, y, p);
                }
            }
        }

        new ImageWindow(imp);
    }

	/**
	 * Transforme une matrice en image.
	 * 1) normaliser: c'est-a-dire faire une mise a l'echelle de maniere a ce
	 * que la valeur la plus faible soit 0 et la valeur la plus haute 255.
	 * (voir TP1: etirement d'histogramme).
	 * 2) ne pas normaliser: tous les elements dont la valeur est inferieure a 0
	 * sont fixes a 0 et tous les elements dont la valeur est superieure a 255
	 * sont fixes a 255.
	 * @param mat            La matrice a transformer en image
	 * @param normaliser     Faut-il normaliser ?
	 */
	public static ImageProcessor toImageProcessor(double[][] mat, boolean normalise){
		ImageProcessor result = new ByteProcessor(mat.length, mat[0].length);

		if (normalise){
			double max = mat[0][0];
			double min = mat[0][0];
			for (int y = 0; y < mat[0].length; y++) {
				for (int x = 0; x < mat.length; x++) {
					if (mat[x][y] > max) max = mat[x][y];
					if (mat[x][y] < min) min = mat[x][y];
				}
			}

			if (min != max) {
				for (int y = 0; y < mat[0].length; y++) {
					for (int x = 0; x < mat.length; x++) {
						result.putPixel(x, y, (int) ((255 * (mat[x][y] - min)) / (max - min)));
					}
				}
			}
		}
		else{
			for (int y = 0; y < mat[0].length; y++){
				for (int x = 0; x < mat.length; x++) {
					int p = (int) Math.min(mat[x][y], 255);
					p = Math.max(p, 0);
					result.putPixel(x, y, p);
				}
			}
		}
		
		return result;
	}
}
