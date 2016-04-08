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

	public static ArrayList<ConnectedComponent> getCC(int pixelValue, ImageProcessor ip){

		boolean[][] visited = new boolean[ip.getWidth()][ip.getHeight()];

		//initialisation du tableau
		for(int x=0;x<ip.getWidth();x++){
			for(int y=0;y<ip.getHeight();y++){
				visited[x][y] = false;
			}
		}

		//liste contenant les CC détectées
		ArrayList<ConnectedComponent> connectedComponent = new ArrayList<ConnectedComponent>();

		//on parcourt l'image
		for(int x=0;x<ip.getWidth();x++){
			for(int y=0;y<ip.getHeight();y++){

				//on vérifie que le pixel est de la bonne couleur et qu'il n'a pas été visité
				if(ip.getPixel(x, y) == pixelValue && !visited[x][y]){

					//file utilisée pour trouver la CC
					ArrayList<Point> pointCC = new ArrayList<Point>();
					
					//liste de composante connexe
					ArrayList<Point> cc = new ArrayList<Point>();
					
					//initialisée avec le pixel que l'on considère
					pointCC.add(new Point(x, y));
					
					//tant que la liste est non vide
					while(!pointCC.isEmpty()){
						
						//on prend le premier élément de la liste
						Point currentPoint = pointCC.get(0);
						cc.add(currentPoint);
						
						// check all the 8-adjacent points to each point of current
						for (int i = -1; i < 1; i++) {		
							for (int j = -1; j < 1; j++) {
								
								
								
								//vérification que le pixel fait bien partie de la CC
								if(ip.getPixel(x, y) == pixelValue && !visited[(int)currentPoint.getX()][(int)currentPoint.getY()]){
									
									//initialisation des coordonnées x et y du point détecté
									int coordX = (int)currentPoint.getX() + i;
									int coordY = (int)currentPoint.getY() + j;
									
									//ajout du point aux listes
									pointCC.add(new Point(coordX, coordY));
									cc.add(new Point(coordX, coordY));
									
									//on enlève le point visité et traité et on change la valeur visité à "true"
									visited[(int)currentPoint.getX()][(int)currentPoint.getY()] = true;
									pointCC.remove(0);
								}
							}
						}
						
						//ajout de la composante connexe à la liste
						connectedComponent.add(new ConnectedComponent(cc));
					}
				}
			}
		}

		return null;
	}

}