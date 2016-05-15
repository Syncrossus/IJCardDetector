package morpho;

import ij.*;
import ij.gui.*;
import ij.process.*;
import java.util.*;

/**
 * Quelques opérations ensemblistes et morphologiques.
 *
 * IMPORTANT:
 * Dans toutes ces méthodes, on considère qu'un pixel appartient à l'objet s'il
 * est blanc (valeur du pixel à 255).
 *
 * Ces méthodes ne doivent être utilisées qu'avec des images binaires
 * (les valeurs des pixels autorisées sont 0 et 255).
 *
 * Le code des méthodes dilatation(...), erosion(...), ouverture(...) et
 * fermeture(...) est à compléter.
 */
public class Morpho {

    /**
    * Stocke dans 'sortie' l'union de 'entree1' et 'entree2'.
    * 'sortie' peut éventuellement pointer sur l'une des deux entrées.
    * @param entree1  Première image d'entrée.
    * @param entree2  Seconde image d'entrée.
    * @param sortie   Image de sortie.
    */
    public static void union(ImageProcessor entree1, ImageProcessor entree2,
                             ImageProcessor sortie) {

        if (entree1.getWidth() != entree2.getWidth() ||
                entree1.getWidth() != sortie.getWidth() ||
                entree1.getHeight() != entree2.getHeight() ||
                entree1.getHeight() != sortie.getHeight()) {
            throw new IllegalArgumentException(
                "Les tailles des images de correspondent pas.");
        }

        for (int y = 0; y < entree1.getHeight(); y++) {
            for (int x = 0; x < entree1.getWidth(); x++) {
                if (entree1.getPixel(x, y) == 255 || entree2.getPixel(x, y) == 255)
                    sortie.putPixel(x, y, 255);
                else sortie.putPixel(x, y, 0);
            }
        }

    }

    /**
    * Stocke dans 'sortie' l'intersection de 'entree1' et 'entree2'.
    * 'sortie' peut éventuellement pointer sur l'une des deux entrées.
    * @param entree1  Première image d'entrée.
    * @param entree2  Seconde image d'entrée.
    * @param sortie   Image de sortie.
    */
    public static void intersection(ImageProcessor entree1, ImageProcessor entree2,
                                    ImageProcessor sortie) {

        if (entree1.getWidth() != entree2.getWidth() ||
                entree1.getWidth() != sortie.getWidth() ||
                entree1.getHeight() != entree2.getHeight() ||
                entree1.getHeight() != sortie.getHeight()) {
            throw new IllegalArgumentException(
                "Les tailles des images de correspondent pas.");
        }

        for (int y = 0; y < entree1.getHeight(); y++) {
            for (int x = 0; x < entree1.getWidth(); x++) {
                if (entree1.getPixel(x, y) == 255 && entree2.getPixel(x, y) == 255)
                    sortie.putPixel(x, y, 255);
                else sortie.putPixel(x, y, 0);
            }
        }

    }

    /**
    * Stocke dans 'sortie' le complémentaire de 'entree'.
    * 'sortie' peut éventuellement pointer sur l'entrée.
    * @param entree  Image d'entrée
    * @param sortie  Image de sortie
    */
    public static void complementaire(ImageProcessor entree,
                                      ImageProcessor sortie) {

        if (entree.getWidth() != sortie.getWidth() ||
                entree.getHeight() != sortie.getHeight()) {
            throw new IllegalArgumentException(
                "Les tailles des images de correspondent pas.");
        }

        for (int y = 0; y < entree.getHeight(); y++) {
            for (int x = 0; x < entree.getWidth(); x++) {
                sortie.putPixel(x, y, 255 - entree.getPixel(x, y));
            }
        }

    }

    /**
    * Stocke dans 'sortie' la différence entre 'entree1' et 'entree2'.
    * 'sortie' peut éventuellement pointer sur l'une des deux entrées.
    * @param entree1  Première image d'entrée.
    * @param entree2  Seconde image d'entrée.
    * @param sortie   Image de sortie.
    */
    public static void difference(ImageProcessor entree1, ImageProcessor entree2,
                                  ImageProcessor sortie) {

        if (entree1.getWidth() != entree2.getWidth() ||
                entree1.getWidth() != sortie.getWidth() ||
                entree1.getHeight() != entree2.getHeight() ||
                entree1.getHeight() != sortie.getHeight()) {
            throw new IllegalArgumentException(
                "Les tailles des images de correspondent pas.");
        }

        for (int y = 0; y < entree1.getHeight(); y++) {
            for (int x = 0; x < entree1.getWidth(); x++) {
                if (entree1.getPixel(x, y) == 255 && entree2.getPixel(x, y) != 255)
                    sortie.putPixel(x, y, 255);
                else sortie.putPixel(x, y, 0);
            }
        }
    }
    
    /**
    * Réalise la dilatation de l'image 'in' par l'élément structurant 'es'
    * et stocke le résultat dans 'out'.
    * @param in   Image d'entrée
    * @param es   Elément structurant
    * @param out  Image de sortie
    */
    public static void dilatation(ImageProcessor in, ElementStructurant es, ImageProcessor out) {
        verifie(in, out);
        
        for(int i=0; i<in.getWidth(); i++){
        	for(int j=0; j<in.getHeight(); j++){
        		out.putPixel(i, j, Morpho.getValeurDilate(in, es, i, j));
        	}
        }
    }
    
    public static int getValeurDilate(ImageProcessor in, ElementStructurant es, int i, int j){
    	int value = 255;
    	
    	for(int x = es.getXmin(); x<es.getXmax(); x++){
			for(int y = es.getYmin(); y<es.getYmax(); y++){
				int col = i+x, line=j+y;
				if(col < 0){
					col = 0;
				}
				else if(col>in.getWidth()-1){ 
					col = in.getWidth() - 1;
				}

				if(line < 0){
					line = 0;
				}
				else if(line>in.getHeight()-1){
					line = in.getHeight() - 1;
				}
				
				if(in.getPixel(col, line) == 0 && es.get(x, y) == 0){
					return 0;
				}
			}
		}
    	
    	return value;
    }
    
    /**
    * Réalise l'érosion de l'image 'in' par l'élément structurant 'es'
    * et stocke le résultat dans 'out'.
    * @param in   Image d'entrée
    * @param es   Elément structurant
    * @param out  Image de sortie
    */
    public static void erosion(ImageProcessor in, ElementStructurant es,
                               ImageProcessor out) {

        verifie(in, out);

        for(int i=0; i<in.getWidth(); i++){
        	for(int j=0; j<in.getHeight(); j++){
        		out.putPixel(i, j, Morpho.getValeurErrode(in, es, i, j));
        	}
        }
    }

    
    public static int getValeurErrode(ImageProcessor in, ElementStructurant es, int i, int j){
    	int value = 0;
    	
    	for(int x=es.getXmin(); x<es.getXmax(); x++){
			for(int y=es.getYmin(); y<es.getYmax(); y++){
				int col = i+x, line = j+y;
				
				if(col < 0){
					col = 0;
				}
				else if(col>in.getWidth()-1){ 
					col = in.getWidth() - 1;
				}

				if(line < 0){
					line = 0;
				}
				else if(line>in.getHeight()-1){
					line = in.getHeight()- 1;
				}
				
				if(in.getPixel(col, line) == 255 || es.get(x, y) == 255){
					value = 255;
				}
			}
		}
    	
    	return value;
    }
    
    /**
    * Réalise l'ouverture morphologique de l'image 'in' par
    * l'élément structurant 'es' et stocke le résultat dans 'out'.
    * @param in   Image d'entrée
    * @param es   Elément structurant
    * @param out  Image de sortie
    */
    public static void ouverture(ImageProcessor in, ElementStructurant es,ImageProcessor out) {
    	ImageProcessor result = new ByteProcessor(in, true);
    	Morpho.erosion(in, es, result);
    	Morpho.dilatation(result, es.symetrique(), out);
    }

    /**
    * Réalise la fermeture morphologique de l'image 'in' par
    * l'élément structurant 'es' et stocke le résultat dans 'out'.
    * @param in   Image d'entrée
    * @param es   Elément structurant
    * @param out  Image de sortie
    */
    public static void fermeture(ImageProcessor in, ElementStructurant es, ImageProcessor out){
        ImageProcessor result = new ByteProcessor(in, true);
        Morpho.dilatation(in, es, result);
        Morpho.erosion(result, es.symetrique(), out);
    }

    /**
    * Vérifie que l'image 'out' peut bien être utilisée pour y stocker le
    * résultat d'une opération morphologique sur 'in'.
    * Si c'est le cas, l'image de sortie est repeinte en noir.
    * @param in   Image d'entrée
    * @param out  Image de sortie
    */
    private static void verifie(ImageProcessor in, ImageProcessor out) {
        if (in.getWidth() != out.getWidth()
                || in.getHeight() != out.getHeight())
            throw new IllegalArgumentException(
                "in et out doivent avoir les mêmes dimensions");

        if (in == out)
            throw new IllegalArgumentException(
                "in et out ne doivent pas pointer sur la même image");

        byte[] pixels = (byte[]) out.getPixels();
        Arrays.fill(pixels, (byte) 0);
    }

}
