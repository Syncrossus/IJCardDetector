package binarisation;

import ij.process.ImageProcessor;

public class Seuillage {
	
	/**
	 * 
	 * @param ip ImageProcessor to binarize
	 * @param value Integer to compare each pixel of the ImageProcessor.
	 * @return ImageProcesor binarized
	 * If the pixel has a value greater than the given value, the method return 255, else, it return 0 for this pixel.
	 */
	public static final ImageProcessor apply(ImageProcessor ip, int value){
		
		for(int i=0; i<ip.getWidth();i++){
			for(int j=0;j<ip.getHeight();j++){
				if(ip.getPixel(i, j)<value){
					ip.putPixel(i, j, 0);
				}
				else{
					ip.putPixel(i, j, 255);
				}
			}
		}
		
		return ip;
	}
}
