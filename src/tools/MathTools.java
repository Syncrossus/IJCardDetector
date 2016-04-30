package tools;

import java.util.ArrayList;
import java.util.Iterator;

import ij.process.ImageProcessor;

public class MathTools{
	
	/*===============================================================*/
	/*==============================MIN==============================*/
	/*===============================================================*/
	/**
	 * @return the min value of the set of arguments
	 */
	public static double min(double... args){
		double min=args[0];
		for (double d : args) {
			min = Math.min(min, d);
		}
		return min;
	}
	
	/**
	 * @return the min value of the set of arguments
	 */
	public static float min(float... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (float) min(newArray);
	}
	
	/**
	 * @return the min value of the set of arguments
	 */
	public static long min(long... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (long) min(newArray);
	}
	
	/**
	 * @return the min value of the set of arguments
	 */
	public static int min(int... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (int) min(newArray);
	}
	
	/**
	 * @return the min value of the set of arguments
	 */
	public static int min(ArrayList<Integer> list){
		int[] array = new int[list.size()];
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < array.length; i++) {
			array[i]=it.next().intValue();
		}
		return min(array);
	}
	
	/*===============================================================*/
	/*==============================MAX==============================*/
	/*===============================================================*/
	/**
	 * @return the max value of the set of arguments
	 */
	public static double max(double... args){
		double max=args[0];
		for (double d : args) {
			max = Math.max(max, d);
		}
		return max;
	}
	
	/**
	 * @return the max value of the set of arguments
	 */
	public static float max(float... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (float) max(newArray);
	}
	
	/**
	 * @return the max value of the set of arguments
	 */
	public static long max(long... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (long) max(newArray);
	}
	
	/**
	 * @return the max value of the set of arguments
	 */
	public static int max(int... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (int) max(newArray);
	}
	
	/**
	 * @return the max value of the set of arguments
	 */
	public static int max(ArrayList<Integer> list){
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i]=list.get(i);
		}
		return max(array);
	}

	/*================================================================*/
	/*===========================STATISTICS===========================*/
	/*================================================================*/
	
	/**
	 * @param values : a set of real values
	 * @return the sum of the set of values
	 */
	public static double sum(double... values) {
		double sum = 0;
		for (double d : values)
			sum+=d;
		return sum;
	}
	
	/**
	 * @param list : an ArrayList of Integers
	 * @return the average value of the list (may not be contained in the list)
	 */
	public static int average(ArrayList<Integer> list) {
		double[] values = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i]=list.get(i);
		}
		return (int) average(values);
	}

	/**
	 * @param values : a set of real values
	 * @return the average value of the set of values (may not be contained in the argument set)
	 */
	public static double average(double... values) {
		return sum(values)/values.length;
	}

	/**
	 * @param values : a set of values
	 * @return the variance of the set of values
	 */
	public static double variance(double... values) {
		double meanValue = average(values);
		for (int i = 0; i < values.length; i++) {
			values[i]=(values[i]-meanValue)*(values[i]-meanValue);
		}
		return average(values);
	}

	/**
	 * @param image : an image
	 * @return the standard deviation of the pixel values in the image
	 */
	public static double standardDeviation(ImageProcessor image) {
		double[] contenu=new double[image.getHeight()*image.getWidth()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				contenu[i*image.getWidth()+j]=image.getPixel(i, j);
			}
		}
		return standardDeviation(contenu);
	}

	/**
	 * @param mask : a convolution mask
	 * @return the standard deviation of the pixel values in the mask
	 */
	public static double standardDeviation(Masque mask) {
		return standardDeviation(mask.getContenu());
	}

	/**
	 * @param values : a set of values
	 * @return the standard deviation of the set of values
	 */
	public static double standardDeviation(double... values) {
		return Math.sqrt(variance(values));
	}
		
}
