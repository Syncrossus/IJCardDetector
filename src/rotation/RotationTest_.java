package rotation;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;


public class RotationTest_ implements PlugInFilter {
	/**
	 * 
	 * @param ip image to rotate
	 * @param theta the angle to rotate the image
	 * This function rotates a card with the equation of a line
	 */
	public static ImageProcessor rotate(ImageProcessor ip, int angle){
		
		ImageProcessor dst = new ByteProcessor(ip.getWidth(), ip.getHeight());
		
		dst = rotate(ip, 45);
		return dst;
	}
	
	/**
	 * Find the angle of the line
	 * @param a in ax+b
	 * @param b in ax+b
	 * @return the angle of the line
	 */
	public int getTheta(int a, int b){
		return (int)Math.atan(b/a);
	}

	@Override
	public void run(ImageProcessor arg0) {
		ImageProcessor ipRot = rotate(arg0.duplicate(), 45);
        ImagePlus impRot = new ImagePlus("kjderhfdfbvjd", ipRot);
        new ImageWindow(impRot);
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
}
