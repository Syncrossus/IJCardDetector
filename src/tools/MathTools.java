package tools;

import java.util.ArrayList;
import java.util.Iterator;

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

	

	
}
