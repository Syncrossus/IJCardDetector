package tools;

import ij.process.ImageProcessor;

public class ImageProcessorTools {
	public static double mean(ImageProcessor image){
		double mean=0;
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++)
				mean+=image.getPixel(i, j);
		mean/=image.getHeight()*image.getWidth();
		return mean;		
	}
}
