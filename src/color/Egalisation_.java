package color;
import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;

public class Egalisation_ implements PlugInFilter {

	public static final int NB_LEVEL_GRAY = 256;
	
    public void run(ImageProcessor ip) {

        ImagePlus impEgal = NewImage.createByteImage(
                "Egalisation de l'histogramme",
                ip.getWidth(), ip.getHeight(), 1, NewImage.FILL_BLACK);
        ImageProcessor ipEgal = impEgal.getProcessor();

        /**
         * À faire :
         * Égaliser l'histogramme de l'image 'ip' et mettre le résultat
         * dans l'image 'ipEgal'.
         *
         * Pour lire le niveau de gris d'un pixel, utiliser la méthode
         * int ImageProcessor.getPixel(int x, int y)
         *
         * Pour modifier le niveau de gris d'un pixel, utiliser la méthode
         * void ImageProcessor.putPixel(int x, int y, int value)
         */

        //Création de l'histo cumulé
       double nbPixel = ip.getWidth() * ip.getHeight();
        
        //Calcul des frequences
        double[]frequencies = new double[NB_LEVEL_GRAY];
        
        for(int i=0; i<ip.getWidth(); i++){
        	for(int j=0; j<ip.getHeight(); j++){
        		frequencies[ip.getPixel(i,j)] += 1./nbPixel;
        	}
        }
        
        double []cumulated = frequencies.clone();
        for(int i=1; i<NB_LEVEL_GRAY; i++){
        	cumulated[i] += cumulated[i-1];
        }
        
        for(int i=0; i<ipEgal.getWidth(); i++){
        	for(int j=0; j<ipEgal.getHeight(); j++){
        		int valeur = (int) (255* cumulated[ip.getPixel(i, j)]);
        		ipEgal.putPixel(i, j, valeur);
        	}
        }
        
        
        
        /**
         * Fin de la partie a completer.
         */
        new ImageWindow(impEgal);
        
        /**
         * Création de la Lut
         */
        ImagePlus impLut = NewImage.createByteImage(
                "LUT de l'égalisation",
                256, 256, 1, NewImage.FILL_WHITE);
        ImageProcessor ipLut = impLut.getProcessor();
        
        ipLut.drawLine(0, ipLut.getHeight(), ipLut.getWidth(), ipLut.getHeight());
        ipLut.drawLine(0, ipLut.getHeight(), 0, 0);
        
        int point = ip.getHeight() - (int) (255 * cumulated[0]);
        for(int i=0; i<NB_LEVEL_GRAY; i++){
        	int valeur = ip.getHeight() - (int) (255 * cumulated[i]);
        	ipLut.putPixel(i, valeur, 0);
        	ipLut.drawLine(i, point, i, valeur);
        	point = valeur;
        }
        
        new ImageWindow(impLut);

    }

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }

}
