package scaling;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Resizer {

	/**
	 * Scales an image, downsampling or upsampling it depending on the scaling factor.
	 * Scaling factor must be positive.
	 * @param src : a source image that should be resized
	 * @param factor : the scaling factor (>0)
	 * @return the resized image
	 */
	public static ImageProcessor scale(ImageProcessor src, double factor){
		if(factor <= 0)
			throw new IllegalArgumentException("Argument \"factor\" is less than 0."+factor);
		
		ImageProcessor dst = new ByteProcessor((int)(src.getWidth()*factor), (int)(src.getHeight()*factor)); //creating the destination image with the propoer dimensions
		
		for (double i = 0; i < src.getWidth(); i+=1/factor) {
			for (double j = 0; j < src.getHeight(); j+=1/factor) {
				dst.putPixel((int)(i*factor), (int)(j*factor), src.getPixel((int)i, (int)j));
			}
		}
		return dst;
		/*
		if(factor<1)
			return downsample(src, 1/factor);
		else
			return upsample(src, factor);*/
	}
	
	/*
	/**
	 * downsamples an image by the given factor. Raises an IllegalArgumentException if shrink factor is less than 1.
	 * @param src : a source image that should be resized
	 * @param factor : the resize factor (>1)
	 * @return the resized image
	 *
	public static ImageProcessor downsample(ImageProcessor src, double factor){
		if(factor <= 1)
			throw new IllegalArgumentException("Argument \"factor\" is less than 1.");
		
		ImageProcessor dst = new ByteProcessor((int)(src.getWidth()/factor), (int)(src.getHeight()/factor)); //creating the destination image with the propoer dimensions
		
		for (double i = 0; i < src.getHeight(); i+=factor) {
			for (double j = 0; j < src.getWidth(); j+=factor) {
				dst.putPixel((int)(i/factor), (int)(j/factor), src.getPixel((int)i, (int)j));
			}
		}
		return dst;
	}
	
	/**
	 * Upsamples an image by the given factor. Raises an IllegalArgumentException if enlargement factor is less than 1.
	 * @param src : a source image that should be resized
	 * @param factor : the resize factor (>1)
	 * @return the resized image
	 *
	public static ImageProcessor upsample(ImageProcessor src, double factor){
		if(factor <= 1)
			throw new IllegalArgumentException("Argument \"factor\" is less than 1.");
		
		ImageProcessor dst = new ByteProcessor((int)(src.getWidth()*factor), (int)(src.getHeight()*factor)); //creating the destination image with the propoer dimensions
		
		return src;
	}
	*/
}
