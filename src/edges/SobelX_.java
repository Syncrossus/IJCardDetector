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
 *      |-1  0  1 |
 * Sx = |-2  0  2 |
 *      |-1  0  1 |
 */
public class SobelX_ implements PlugInFilter {

    public void run(ImageProcessor ip) {
        double[][] mat = Convolution.convoluer(ip, SobelX_.getMasque());
        Convolution.afficheMatrice(mat, "Sobel X", true);
    }

    public int setup(String args, ImagePlus imp) {
        return NO_CHANGES + DOES_8G;
    }
    
    public static Masque getMasque(){
        Masque sobelX = new Masque(1);


    	sobelX.put(-1, -1, -1);
        sobelX.put(1, -1, 1);
        sobelX.put(-1, 0, -2);
        sobelX.put(1, 0, 2);
        sobelX.put(-1, 1, -1);
        sobelX.put(1, 1, 1);

        
        return sobelX;
    }
    
}
