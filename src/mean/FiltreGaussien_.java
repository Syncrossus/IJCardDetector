package mean;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;
import tools.Masque;
import tools.Convolution;

public class FiltreGaussien_ implements PlugInFilter {

    public void run(ImageProcessor ip) {

        GenericDialog gd = new GenericDialog("Options du flou gaussien");
        gd.addNumericField("Rayon du masque", 1, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;

        // rayon: rayon du masque gaussien
        int rayon = (int) gd.getNextNumber();

        
        /**
         * Fin de la partie a completer
         */
        ImageProcessor result = FiltreGaussien_.apply(ip, rayon);
        ImagePlus imp = new ImagePlus("Application du filtre Gaussien", result);
        new ImageWindow(imp);

    }

    public int setup(String args, ImagePlus imp) {
        return NO_CHANGES + DOES_8G;
    }
    
    public static double[][] convoluer(ImageProcessor ip, int rayon){
    	// pour avoir une "cloche" a peu pres complete,
        // on choisit un ecart-type egal a un tiers du rayon.
        double sigma = rayon / 3.0;

        Masque masque = new Masque(rayon);
        
        double somme = 0;
        
        for(int i=-rayon; i<=rayon; i++){
        	for(int j=-rayon; j<=rayon; j++){
        		double valeur = 1/(2*sigma*sigma*Math.PI);
        		valeur *= Math.exp(-(i*i+j*j)/(2*sigma*sigma));
        		masque.put(i,j,valeur);
        		somme+=valeur;
        	}
        }
        
        
        for(int i=-rayon; i<=rayon; i++){
        	for(int j=-rayon; j<=rayon; j++){
        		masque.put(i, j,  masque.get(i, j)/somme);
        	}
        }
        
        double[][] mat = null;

        mat = Convolution.convoluer(ip, masque);
        
        return mat;
    }

    public static ImageProcessor apply(ImageProcessor ip, int rayon){
    	ip = new ByteProcessor(ip, true);
    	double [][]result = FiltreGaussien_.convoluer(ip, rayon);
  
    	return Convolution.toImageProcessor(result, false);
    }
}
 