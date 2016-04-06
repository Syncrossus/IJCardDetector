package binarisation;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

/**
 * Plugin permettant d'effectuer la binarisation d'une image par la méthode
 * d'Otsu.
 *
 * La binarisation d'une image revient à diviser l'image en deux classes à
 * partir d'un seuil fixé. Tous les pixels en dessous du seuil deviennent 0 et
 * tous les pixels au dessus deviennent 255.
 */
public class Otsu_ implements PlugInFilter {
	
	public static final int NB_LEVEL_GRAY = 256;
	
	/**
	 * Returns the ImageProcessor, binarized by the Otsu method.
	 * 
	 * @param ip the source image
	 * @return ip image binarized by the Otsu method
	 */
	public static ImageProcessor apply(ImageProcessor ip){
		ImageProcessor result = new ByteProcessor(ip, true);
		
		int otsu = Otsu_.getOtsuThreshHold(result);
        result = Seuillage.apply(result, otsu);
        
        return result;
	}

	/**
	 * Returns the otsu threshold of the image to binarize.
	 * @param ip the image to binarize
	 * @return otsu threshold
	 */
	public static int getOtsuThreshHold(ImageProcessor ip){
		int otsu = 0;

        /**
         * Calcul du seuil de Otsu
         */
        double nbPixel = ip.getWidth()*ip.getHeight();
        double []probabilities = new double[NB_LEVEL_GRAY];
        
        for(int i=0; i<ip.getHeight(); i++){
        	for(int j=0; j<ip.getWidth(); j++){
        		probabilities[ip.getPixel(j, i)] += 1/nbPixel;
        	}
        }
       
        /**
         * Maximisation de la variance interclasse
         */
        double max = 0;
        for(int i=0; i<NB_LEVEL_GRAY; i++){
        	double w1=0, w2=0, u1=0, u2=0;
        	for(int j=0; j<i; j++){
        		w1 += probabilities[j];
        		u1 += j*probabilities[j];
        	}
        	
        	u1/=w1;
        	w2 = 1-w1;
        	
        	for(int j=i; j<NB_LEVEL_GRAY; j++){
        		u2 += j*probabilities[j];
        	}
        	u2/=w2;
        	
        	double value= w1*w2*(u1-u2)*(u1-u2);
        	if(max<value){
        		otsu = i;
        		max = value;
        	}
        }
        
        return otsu;
	}
	
	/**
	 * Runs the Otsu binarization on the ImageProcessor ip.
	 */
    public void run(ImageProcessor ip) {
        ImageProcessor ipOtsu = Otsu_.apply(ip);
        ImagePlus impOtsu = new ImagePlus("Otsu thresholding", ipOtsu);
        new ImageWindow(impOtsu);
    }

    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL;
    }
}
