package binarisation;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Hysteresis_ implements PlugInFilter{
	
	/**
	 * Returns the ImageProcessor, binarized by the Hysteresis method.
	 * 
	 * @param ip the source image
	 * @return ip image binarized by the Otsu method
	 */
	public static ImageProcessor apply(ImageProcessor ip, int min, int max){
		ip = new ByteProcessor(ip, true);
		ImageProcessor result = new ByteProcessor(ip, true);
		
        result = Seuillage.apply(result, max);
        
        for(int i=0; i<result.getWidth(); i++){
        	for(int j=0; j<result.getHeight(); j++){
        		int valeur = ip.getPixel(i, j);
        		if(valeur>min && valeur<max){
        			result.putPixel(i, j, Hysteresis_.getUncertainValue(result, i, j));
        		}
        	}
        }
		
        return result;
	}
	
	/**
	 * Return 255 if the given point is a true edges, 0 otherwise.
	 * @param ip the given image
	 * @param x x-coordinate of the pixel
	 * @param y y-coordinate of the pixel
	 * @return 0 if the pixel is not an edges, 255 otherwise
	 */
	public static int getUncertainValue(ImageProcessor ip, int x, int y){		
		for(int i = -1; i<=1; i++){
			for(int j = -1; j<=1; j++){
				
				int col = (i+x < 0)? 0:i+x;
				col = (i+x > ip.getWidth()-1)? ip.getWidth()-1:i+x;
				int line = (j+y<0)? 0:j+y;
				line = (j+y>ip.getHeight()-1)? ip.getHeight()-1:j+y;
				
				if(x!= 0 && y!=0 && ip.getPixel(col, line) == 255){
						return 255;
				}
				
			}
		}
		
		return 0;
	}
	
	/**
	 * Runs the Hysteresis binarization on the ImageProcessor ip.
	 */
    public void run(ImageProcessor ip) {
        GenericDialog gd = new GenericDialog("Options du seuillage Hysteresis");
        gd.addNumericField("Minimum", 50, 0);
        gd.addNumericField("Maximum", 60, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;

        // rayon: rayon du voisinage considere pour chaque pixel
        // Autrement dit, le voisinage est un carre de cote (2 * rayon + 1)
        int min = (int) gd.getNextNumber();
        int max = (int) gd.getNextNumber();
        
        ImageProcessor ipOtsu = Hysteresis_.apply(ip, min, max);
        ImagePlus impOtsu = new ImagePlus("Hysteresis thresholding", ipOtsu);
        new ImageWindow(impOtsu);
    }

    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL;
    }
    
}
