package extraction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import tools.Line;

public class Hough_ implements PlugInFilter{

	public static final int NB_THETA = 180;
	public static final int THETA_RADIUS = 40;
	public static final int RHO_QUOTIENT = 10;
	
	private ImageProcessor image;
	private int[][] accumulator;
	private int diagonale;
	
	public Hough_(){
		super();
	}
	
	public Hough_(ImageProcessor ip){
		super();
		this.init(ip);
	}
	
	/**
	 * Inits the object's attributes when a image is given
	 * @param ip the given image
	 */
	private void init(ImageProcessor ip){
		this.image = new ByteProcessor(ip, true);
		this.diagonale = (int)Math.sqrt((image.getWidth() * image.getWidth()) + (image.getHeight() * image.getHeight()));
		accumulator = new int[diagonale * 2][Hough_.NB_THETA];
	}

	/** 
	 * @return  the result of the Hough transform on the image param.
	 */
	public List<Line> apply(){
		this.initAccumulator();
		int threshold = this.getThreshold();
		this.applyThreshold(threshold);
		this.removeNonLocalMaximums(diagonale/Hough_.RHO_QUOTIENT, Hough_.THETA_RADIUS);
		
		List<Line> lines = this.getLines();
		
		return lines;
	}
	
	/**
	 * Displays all the given line on the image attribute
	 * @param lines the lines to display
	 */
	public static void displayLine(ImageProcessor image, List<Line> lines) {
		System.out.println("Droites");
		image.setColor(Color.WHITE);
		for(Line line:lines){
			System.out.println(line);
			int x1=0, x2=0, y1=0, y2=0;
			
			// Ligne verticale
			if(line.isVertical()){
				x1 = line.getXIntercept();
				x2 = line.getXIntercept();
				y1 = 0;
				y2 = image.getHeight()-1;
			}
			
			// Ligne horizontale
			else{
				boolean found = false;
				for(x1=0; x1<image.getWidth() && !found; x1++){
					y1 = line.getY(x1);
					if(y1>=0 && y1<image.getHeight()){
						found = true;
					}
				}
				
				found = false;
				for(x2=image.getWidth()-1; x2>=0 && !found; x2--){
					y2 = line.getY(x2);
					if(y2>=0 && y2<image.getHeight()){
						found = true;
					}
				}
			}
			
			image.drawLine(x1, y1, x2, y2);
		}
		
	}
	

	/**
	 * Returns all the detected line in the accumulator
	 * @param accumulator the analyzed accumulator
	 * @return an ArrayList containing each Lines with a positive vote
	 */
	public List<Line> getLines() {
		List<Line> lines = new ArrayList<Line>();

		for(int i=0; i<accumulator.length; i++){
			for(int j=0; j<accumulator[i].length; j++){
				if(accumulator[i][j]>0){
					Line line = new Line(i-this.getDiagonale(), Math.toRadians(j-Hough_.NB_THETA/2));
					lines.add(line);
				}
			}
		}
		
		return lines;
	}
	
	
	/**
	 * Returns the threshold used for the given accumulator
	 * @param accumulator the analyzed accumulator
	 * @return the found threshold
	 */
	public int getThreshold() {
		int max = 0;
		double size = 0.4;
		
		for(int i=0; i<accumulator.length; i++){
			for(int j=0; j<accumulator[i].length; j++){
				max = (accumulator[i][j] > max)? accumulator[i][j]:max;
			}
		}
		
		return (int) (max * size);
	}

	/**
	 * Returns the accumulator of the given image, in the Hough polar space
	 * @param image the given image
	 * @return resulting accumulator
	 */
	public void initAccumulator(){
		//initialisation de la taille de l'accumulateur
		int diagonale = (int)Math.sqrt((image.getWidth() * image.getWidth()) + (image.getHeight() * image.getHeight()));	

		//initialisation
		for(int i=0;i<2*diagonale;i++){
			for(int j=0;j<Hough_.NB_THETA;j++){
				accumulator[i][j]=0;
			}
		}

		//incrémentation des valeurs de l'accumulateur correspondant à la formule
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				if(image.getPixel(i, j) == 255){
					// Droites potentielles
					for(int theta = 0; theta<Hough_.NB_THETA; theta++){
						double angle = (theta-Hough_.NB_THETA/2) * Math.PI/180;
						int r = (int) (i * Math.cos(angle) + j*Math.sin(angle));
						accumulator[r + diagonale][theta]++;
					}
				}
			}	
		}
	}

	/**
	 * Remove all value which is not a local maxima.
	 * @param accumulator the accumulator of the Hough Transform
	 * @return a new accumulator which all non local maxima has been removed
	 */
	public void removeNonLocalMaximums(int rayonLargeur, int rayonLongueur){
		for(int i=0; i<accumulator.length; i++){
			for(int j=0; j<accumulator[i].length; j++){
				if (accumulator[i][j]>0)	
					accumulator[i][j] = this.getValueFromNeighbors(rayonLargeur, rayonLongueur, i, j);
			}
		}
		
	}

	/**
	 * Returns the new value of the (col,line) point in the accumulator array.
	 * If the point is not a local maxima, it value is put to 0. Otherwise, it value is keept.
	 * @param accumulator result of the Hough Transform
	 * @param col x-coordinate of the point
	 * @param line y-coordinate of the point
	 * @return the new value
	 */
	private int getValueFromNeighbors(int rayonLargeur, int rayonLongueur, int col, int line){
		int localMax = 0;
		
		for(int i=-rayonLargeur; i<=rayonLargeur; i++){
			for(int j=-rayonLongueur; j<=rayonLongueur; j++){
				
				int x = col+i,  y = line+j;

				if(x<0){
					x=0;
				}
				else if(x>accumulator.length-1){
					x = accumulator.length-1;
				}

				if(y<0){
					y=0;
				}
				else if(y>accumulator[x].length-1){
					y = accumulator[x].length-1;
				}

				if(x != col || y != line)
					localMax = (localMax<accumulator[x][y])? accumulator[x][y]:localMax;
				
			}
		}
		
		if (localMax<accumulator[col][line])
			return accumulator[col][line];
		else
			return 0;
		
	}

	/**
	 * Returns the list of maximums lines, identified using a threshold value
	 * @param accumulator the Hough accumulator
	 * @param threshold the threshold
	 * @return list of maximums line
	 */
	public int[][] applyThreshold(int threshold){

		for(int i=0; i<accumulator.length; i++){
			for(int j=0; j<accumulator[i].length; j++){
				if(accumulator[i][j]<=threshold){
					accumulator[i][j] = 0;
				}
			}
		}

		return accumulator;
	}
	
	/**
	 * @return the image processor
	 */
	public ImageProcessor getImage() {
		return image;
	}
	
	/**
	 * @return the accumulator 
	 */
	public int[][] getAccumulator() {
		return accumulator;
	}
	
	/**
	 * @return the diagonale of the image
	 */
	public int getDiagonale() {
		return diagonale;
	}

	@Override
	public void run(ImageProcessor ip) {
		ip = new ByteProcessor(ip, true);
		this.init(ip);
		List<Line> lines = this.apply();
		Hough_.displayLine(image, lines);
		new ImageWindow(new ImagePlus("Hough Transform", image));
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
	
}


