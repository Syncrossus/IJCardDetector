package tools;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;

public class Histogram_ implements PlugInFilter {

	public static final int NB_LEVEL_GRAY = 256;
	
	public static double[] getHistogram(ImageProcessor ip){
        //Calcul des frequences
        double []frequencies = new double[NB_LEVEL_GRAY];
        
        for(int i=0; i<ip.getHeight(); i++){
        	for(int j=0; j<ip.getWidth(); j++){
        		frequencies[ip.getPixel(j, i)]++;
        	}
        }
        
        return frequencies;
	}
	
	public static double[] getNormalizedHistogram(ImageProcessor ip){
		int nbPixel = ip.getWidth() * ip.getHeight();
		double[] frequencies = Histogram_.getHistogram(ip);
		
		for(int i=0; i<NB_LEVEL_GRAY; i++){
			frequencies[i]/=nbPixel;
		}
		
		return frequencies;
	}
	
	public static double[] getCumulatedHistogram(ImageProcessor ip){
        double[]frequencies = Histogram_.getCumulatedHistogram(ip);
        
        for(int i=1; i<NB_LEVEL_GRAY; i++){
        	frequencies[i] += frequencies[i-1];
        }
        
        return frequencies;
	}
	
    public void run(ImageProcessor ip) {

        ImagePlus impHisto = NewImage.createByteImage(
                "Histogramme", 256, 256, 1, NewImage.FILL_WHITE);
        ImageProcessor ipHisto = impHisto.getProcessor();
        ipHisto.setColor(0);

        /**
         * À faire :
         * dessiner l'histogramme de l'image 'ip' sur l'image 'ipHisto'.
         *
         * Contraintes:
         * - largeur d'une barre de l'histogramme : 1 pixel
         * - taille de l'histogramme : 256x256 pixels
         *
         * Pour lire le niveau de gris d'un pixel, utiliser la méthode
         * int ImageProcessor.getPixel(int x, int y)
         *
         * Pour tracer une ligne sur une image, utiliser la méthode
         * void ImageProcessor.drawLine(int x1, int y1, int x2, int y2)
         */
        
        
        int nbPixel = ip.getWidth() * ip.getHeight();
        
        //Calcul des frequences
        int []frequencies = new int[NB_LEVEL_GRAY];
        
        for(int i=0; i<ip.getHeight(); i++){
        	for(int j=0; j<ip.getWidth(); j++){
        		frequencies[ip.getPixel(j, i)]++;
        	}
        }
        
        //Normalisation
        double max=0;
        for(int i=0; i<NB_LEVEL_GRAY; i++){
        	max = (max<frequencies[i])? frequencies[i]:max;
        }
        max /= nbPixel;
        
        //Création de l'histogramme
        ipHisto.drawLine(0, ipHisto.getHeight(), ipHisto.getWidth(), ipHisto.getHeight());
        ipHisto.drawLine(0, ipHisto.getHeight(), 0, 0);
        
        for(int i=0; i<NB_LEVEL_GRAY; i++){
        	double percent = (double)frequencies[i]/nbPixel;
        	double value = (percent/max) * NB_LEVEL_GRAY;
        	ipHisto.drawLine(i, ipHisto.getHeight(), i, ipHisto.getHeight()-(int)value);
        }
        
        /**
         * Fin de la partie a compléter.
         */
        new ImageWindow(impHisto);

    }

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }
}
