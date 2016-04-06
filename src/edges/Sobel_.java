package edges;

import ij.*;
import ij.plugin.filter.*;
import ij.process.*;
import tools.Convolution;

public class Sobel_ implements PlugInFilter {

    public void run(ImageProcessor ip) {
        /**
         * A faire: remplir la matrice mat avec la norme
         * du gradient de Sobel de l'image 'ip'.
         * Entree: ip (ImageProcessor)
         * Sortie: mat (double[][])
         */

        double[][] mat = Sobel_.convolve(ip);
       
        /**
         * Fin de la partie  completer
         */
        // Affichage de la matrice
        if (mat != null) {
            Convolution.afficheMatrice(mat, "Sobel", true);
        }
    }

    public int setup(String args, ImagePlus imp) {
        return NO_CHANGES + DOES_8G;
    }
    
    /**
     * Convolves the Sobel filter on the given Image
     * @param ip input image
     * @return matrix containing result of edge extraction
     */
    public static double[][] convolve(ImageProcessor ip){
    	double[][] mat = new double[ip.getWidth()][ip.getHeight()];

        double[][] sobelX = Convolution.convoluer(ip, SobelX_.getMasque());
        double[][] sobelY = Convolution.convoluer(ip, SobelY_.getMasque());		
        
        
        for(int i=0; i<mat.length; i++){
        	for(int j=0; j<mat[i].length; j++){
        		mat[i][j] = Math.sqrt(sobelX[i][j]*sobelX[i][j] + sobelY[i][j]*sobelY[i][j]);
        	}
        }
        
        return mat;
    }

    /**
     * Return a new image with the Sobel Filter applied.
     * @param ip the input image
     * @return new image with edges extracted
     */
	public static ImageProcessor apply(ImageProcessor ip) {
		return Convolution.toImageProcessor(Sobel_.convolve(ip), false);
	}	
	
	/**
	 * Returns a matrix containing directions of each pixel on the extracted edges
	 * @param ip input image
	 * @return 2D array containing direction on the edge of each pixel
	 */
	public static double[][] getDirection(ImageProcessor ip){
		double[][] mat = new double[ip.getWidth()][ip.getHeight()];

        double[][] sobelX = Convolution.convoluer(ip, SobelX_.getMasque());
        double[][] sobelY = Convolution.convoluer(ip, SobelY_.getMasque());		
        
        
        for(int i=0; i<mat.length; i++){
        	for(int j=0; j<mat[i].length; j++){
        		mat[i][j] = Math.atan2(sobelX[i][j],sobelY[i][j]);
        	}
        }
        
        return mat;
	}
	
}
