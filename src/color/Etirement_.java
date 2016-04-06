package color;
import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;

public class Etirement_ implements PlugInFilter {
	
	public static final int NB_LEVEL_GRAY = 256;
	
    public void run(ImageProcessor ip) {

        ImagePlus impEtir = NewImage.createByteImage(
                "Etirement de l'histogramme",
                ip.getWidth(), ip.getHeight(), 1, NewImage.FILL_BLACK);
        ImageProcessor ipEtir = impEtir.getProcessor();

        /**
         * À faire:
         * Étirer l'histogramme de l'image 'ip' et mettre le résultat
         * dans l'image 'ipEtir'.
         *
         * Pour lire le niveau de gris d'un pixel, utiliser la méthode
         * int ImageProcessor.getPixel(int x, int y)
         *
         * Pour modifier le niveau de gris d'un pixel, utiliser la méthode
         * void ImageProcessor.putPixel(int x, int y, int value)
         */
        
        int min = NB_LEVEL_GRAY, max=0;
        for(int i=0; i<ip.getWidth(); i++){
        	for(int j=0; j<ip.getHeight(); j++){
        		int valeur = ip.getPixel(i,j);
        		min = (valeur<min)? valeur:min;
        		max = (valeur>max)? valeur:max;
        	}
        }
        
        for(int i=0; i<ipEtir.getWidth(); i++){
        	for(int j=0; j<ipEtir.getHeight(); j++){	
        		int valeur = 255*(ip.getPixel(i, j) - min)/(max-min);
        		ipEtir.putPixel(i,j,valeur);
        	}
        }

        /**
         * Fin de la partie a compléter.
         */
        new ImageWindow(impEtir);
        
        /**
         * Création de la LUT
         */
        
        ImagePlus impLut = NewImage.createByteImage(
                "Lut de l'étirement",
                256, 256, 1, NewImage.FILL_WHITE);
        ImageProcessor ipLut = impLut.getProcessor();
        
        ipLut.drawLine(0, ipLut.getHeight(), ipLut.getWidth(), ipLut.getHeight());
        ipLut.drawLine(0, ipLut.getHeight(), 0, 0);
        
        int point = ipLut.getHeight() - 255 * (-min)/(max-min);
        for(int i=0; i<NB_LEVEL_GRAY; i++){
        	int valeur = ipLut.getHeight() - 255 * (i - min)/(max-min);
        	ipLut.putPixel(i,  valeur, 0);
        	ipLut.drawLine(i, point, i, valeur);
        	point = valeur;
        }
        
        new ImageWindow(impLut);
    }

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }
}
