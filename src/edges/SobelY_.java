package edges;

import ij.*;
import ij.plugin.filter.*;
import ij.process.*;
import tools.Masque;
import tools.Convolution;

/**
 * Rien a completer, juste a tester.
 * (Encore faut-il que la methode Outils.convoluer(...) fonctionne.)
 * Le masque de convolution est le suivant:
 *      |-1 -2 -1 |
 * Sy = | 0  0  0 |
 *      | 1  2  1 |
 */
public class SobelY_ implements PlugInFilter {

    public void run(ImageProcessor ip) {
        double[][] mat = Convolution.convoluer(ip, SobelY_.getMasque());
        Convolution.afficheMatrice(mat, "Sobel Y", true);
    }

    public int setup(String args, ImagePlus imp) {
        return NO_CHANGES + DOES_8G;
    }
    
    public static Masque getMasque(){
        Masque sobelY = new Masque(1);
        
	    sobelY.put( -1, -1, -1);
	    sobelY.put(0, -1, -2);
	    sobelY.put(1, -1, -1);
	    sobelY.put( -1, 1, 1);
	    sobelY.put(0, 1, 2);
	    sobelY.put(1, 1, 1);


        
        return sobelY;
    }

}
