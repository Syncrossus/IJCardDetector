package extraction;

import java.awt.Point;
import java.util.ArrayList;
import ij.process.ImageProcessor;

public class CCIdentifier{

	/**
	 * Finds and returns in a list all the connected components in the image described by ip
	 * @param ip : a binarized image
	 * @return a list of connected components containing all of the components of the image
	 */
	public static ArrayList<ConnectedComponent> getComponents(ImageProcessor ip){
		ArrayList<ConnectedComponent> components = new ArrayList<ConnectedComponent>();
		for (int i = 0; i < ip.getHeight(); i++) {
			for (int j = 0; j < ip.getWidth(); j++) {
				if(ip.getPixel(i, j)==0 && !componentArrayContainsPoint(components, new Point(i, j))){	// black pixel => part of a CC && point has not already been found
					components.add(getComponentFromPoint(i, j, ip));
				}
			}
		}
		return components;
	}
	
	/**
	 * builds a connected component from a point (very bad time-wise and memory-wise, needs serious optimization)
	 * @param x : x coordinate of the reference point
	 * @param y : y coordinate of the reference point
	 * @param ip : image in which the CC needs to be found
	 * @return the CC to which the point at (x, y) belongs
	 */
	private static ConnectedComponent getComponentFromPoint(int x, int y, ImageProcessor ip) {
		ConnectedComponent current = new ConnectedComponent(x, y);
		ConnectedComponent previous;
		do{
			for (int i = -1; i < 1; i++) {		// check all the 8-adjacent points to each point of current
				for (int j = -1; j < 1; j++) {
					for (Point p : current.getPoints()) {
						if(i==0 && j==0)	// don't want to waste an iteration on a point we already know is part of the CC
							continue;
						if(ip.getPixel((int)p.getX()+i, (int)p.getY()+j)==0 && !current.contains((int)p.getX()+i, (int)p.getY()+j))
							current.addPoint(i, j);
					}
										
				}
			}
			previous = current.deepClone();
		}while(!previous.isIdenticalTo(current)); // stop when previous and current are identical => no more points are being added
		
		return current;
	}

	private static boolean componentArrayContainsPoint(ArrayList<ConnectedComponent> components, Point p){
		for (ConnectedComponent cc : components) {
			if(cc.contains(p)){
				return true;
			}
		}
		return false;
	}
	
}