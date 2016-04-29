package tools;

import java.util.ArrayList;
import java.util.Iterator;

import ij.process.ImageProcessor;

public class MathTools{
	
	// min
	public static double min(double... args){
		double min=args[0];
		for (double d : args) {
			min = Math.min(min, d);
		}
		return min;
	}
	
	public static float min(float... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (float) min(newArray);
	}
	
	public static long min(long... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (long) min(newArray);
	}
	
	public static int min(int... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (int) min(newArray);
	}
	
	public static int min(ArrayList<Integer> list){
		int[] array = new int[list.size()];
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < array.length; i++) {
			array[i]=it.next().intValue();
		}
		return min(array);
	}
	
	//max
	public static double max(double... args){
		double max=args[0];
		for (double d : args) {
			max = Math.max(max, d);
		}
		return max;
	}
	
	public static float max(float... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (float) max(newArray);
	}
	
	public static long max(long... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (long) max(newArray);
	}
	
	public static int max(int... args){
		double[] newArray = new double[args.length];
		for (int i = 0; i < args.length; i++) {
			newArray[i] = (double) args[i];	
		}
		return (int) max(newArray);
	}
	
	public static int max(ArrayList<Integer> list){
		int[] array = new int[list.size()];
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < array.length; i++) {
			array[i]=it.next().intValue();
		}
		return max(array);
	}

	public static int average(ArrayList<Integer> list) {
		int sum = 0;
		for (Integer i : list) {
			sum+=i.intValue();
		}
		return sum/list.size();
	}

	public static double mean(double[] contenu) {
		return sum(contenu)/contenu.length;
	}

	private static double sum(double[] contenu) {
		double sum = 0;
		for (double d : contenu)
			sum+=d;
		return sum;
	}

	public static double standardDeviation(ImageProcessor ip) {
		double[] contenu=new double[ip.getHeight()*ip.getWidth()];
		for (int i = 0; i < ip.getWidth(); i++) {
			for (int j = 0; j < ip.getHeight(); j++) {
				contenu[i*ip.getWidth()+j]=ip.getPixel(i, j);
			}
		}
		return standardDeviation(contenu);
	}

	public static double standardDeviation(Masque masque) {
		return standardDeviation(masque.getContenu());
	}

	public static double standardDeviation(double[] contenu) {
		double meanValue = mean(contenu);
		for (int i = 0; i < contenu.length; i++) {
			contenu[i]=(contenu[i]-meanValue)*(contenu[i]-meanValue);
		}
		return Math.sqrt(mean(contenu));
	}
	

	
}
