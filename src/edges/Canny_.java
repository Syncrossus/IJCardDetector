package edges;

import binarisation.Hysteresis_;
import binarisation.Otsu_;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import mean.FiltreGaussien_;

public class Canny_ implements PlugInFilter {
	
	private ImageProcessor ip;
	
	public Canny_(){
		super();
	}
	
	public Canny_(ImageProcessor ip){
		super();
		this.ip = new ByteProcessor(ip, true);
	}
	
	/**
	 * Returns the result of a CannyFilter on the input image
	 * @param ip the input image
	 * @param rayon Gaussian blur's radius
	 * @return Canny filter's result on the input image
	 */
	public ImageProcessor apply(int rayon){
		ip = FiltreGaussien_.apply(ip, rayon);
				
		ip = Sobel_.apply(ip);
		double[][] directions = Sobel_.getDirection(ip);
		
		int max = Otsu_.getOtsuThreshHold(ip);
		int min = max/2;
		
		ip = this.suppressNonMaximum(directions);
		ip = Hysteresis_.apply(ip, min, max);
		return ip;
	}

	
	/**
	 * Returns a new image, within all non local maximum value has been suppressed.
	 * @param ip the input image
	 * @param directions direction of each pixel
	 * @return image within only local maximum
	 */
	public ImageProcessor suppressNonMaximum(double[][] directions) {
		ImageProcessor result = new ByteProcessor(ip, true);
		
		for(int i=0; i<ip.getWidth(); i++){
			for(int j=0; j<ip.getHeight(); j++){
				result.putPixel(i, j, this.getValueFromDirections(directions[i][j], i, j));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the value of the (x,y) pixel, considering value and neighbors value (in it direction).
	 * @param ip the input image
	 * @param direction direction of the pixel
	 * @param x x-coordinate of the pixel
	 * @param y y-coordinate of the pixel
	 * @return the new value of (x,y) piel
	 */
	private int getValueFromDirections(double direction, int x, int y){
		int next, pred; 
		
		//East West
		if(direction == 0){
			next = ip.getPixel(x+1, y);
			pred = ip.getPixel(x-1, y);
		}
		
		else if(direction > 0 && direction < 45){
			next = (ip.getPixel(x+1, y) + ip.getPixel(x+1, y-1))/2;
			pred = (ip.getPixel(x-1, y) + ip.getPixel(x-1, y+1))/2;
		}
		
		// North-east South-west
		else if(direction == 45){
			next = ip.getPixel(x+1, y-1);
			pred = ip.getPixel(x-1, y+1);
		}
		else if(direction>45 && direction<90){
			next = (ip.getPixel(x, y-1) + ip.getPixel(x+1, y-1))/2;
			pred = (ip.getPixel(x, y+1) + ip.getPixel(x-1, y+1))/2;
		}
		
		// North South
		else if(direction == 90){
			next = ip.getPixel(x, y-1);
			pred = ip.getPixel(x, y+1);
		}
		else if(direction>90 && direction<135){
			next = (ip.getPixel(x, y-1) + ip.getPixel(x-1, y-1))/2;
			pred = (ip.getPixel(x, y+1) + ip.getPixel(x+1, y+1))/2;
		}
		
		// North-west South-East
		else if(direction == 135){
			next = ip.getPixel(x+1, y-1);
			pred = ip.getPixel(x-1, y+1);
		}
		else{
			next = ip.getPixel(x+1, y) + ip.getPixel(x-1, y+1)/2;
			pred = ip.getPixel(x-1, y) + ip.getPixel(x+1, y-1)/2;
		}
		
		int value = ip.getPixel(x, y);
		return (value>=next && value>=pred)? value:0;
	}
	
	@Override
	public void run(ImageProcessor ip) {
        GenericDialog gd = new GenericDialog("Options du filtre de Canny");
        gd.addNumericField("Rayon du filtre Gaussien", 2, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;

        // rayon: rayon du voisinage considere pour chaque pixel
        // Autrement dit, le voisinage est un carre de cote (2 * rayon + 1)
        int rayon = (int) gd.getNextNumber();
        
        this.setIp(ip);
        ip = this.apply(rayon);
        
        new ImageWindow(new ImagePlus("Canny Filter", ip));
		
	}

	public void setIp(ImageProcessor ip) {
		this.ip = ip;
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
	
}
