package extraction;

import java.awt.Point;
import java.util.ArrayList;
import ij.process.ImageProcessor;

public class CCIdentifier{

	/**
	 * 
	 * @param pixelValue the value of the connected component
	 * @param ip image in which the CC needs to be found
	 * @return all connected components detected
	 */
	public static ArrayList<ConnectedComponent> getCC(int pixelValue, ImageProcessor ip){

		boolean[][] visited = initTabBool(ip);

		//liste contenant les CC détectées
		ArrayList<ConnectedComponent> connectedComponents = new ArrayList<ConnectedComponent>();

		//on parcourt l'image
		for(int x=0;x<ip.getWidth();x++){
			for(int y=0;y<ip.getHeight();y++){

				//on vérifie que le pixel est de la bonne couleur et qu'il n'a pas été visité
				if(ip.getPixel(x, y) == pixelValue && !visited[x][y]){

					//file utilisée pour trouver la CC
					ArrayList<Point> pointCC = new ArrayList<Point>();

					//liste de points de la composante connexe courante
					ArrayList<Point> cc = new ArrayList<Point>();

					//initialisation avec le pixel que l'on considère
					pointCC.add(new Point(x, y));
					cc.add(new Point(x, y));
					visited[x][y] = true;

					//tant que la liste est non vide
					while(!pointCC.isEmpty()){
						pointCC = dilateCC(pixelValue, ip, pointCC, cc, visited);
					}
					if(cc.size()>20)
						//ajout de la composante connexe à la liste
						connectedComponents.add(new ConnectedComponent(cc));
				}
			}
		}


		return connectedComponents;
	}

	private static ArrayList<Point> dilateCC(int pixelValue, ImageProcessor ip, ArrayList<Point> pilePointCC, ArrayList<Point> cc, boolean[][] visited){

		//on prend le premier élément de la liste
		Point currentPoint = pilePointCC.get(0);

		// check all the 8-adjacent points no visited to each point of current
		for (int i = -1; i <= 1; i++) {		
			for (int j = -1; j <= 1; j++) {

				try{

					//initialisation des coordonnées x et y du point détecté
					int coordX = (int)currentPoint.getX() + i;
					int coordY = (int)currentPoint.getY() + j;

					//vérification que le pixel fait bien partie de la CC
					if(ip.getPixel(coordX, coordY) == pixelValue && !visited[coordX][coordY]){

						//ajout du point aux listes
						pilePointCC.add(new Point(coordX, coordY));
						cc.add(new Point(coordX, coordY));
						visited[coordX][coordY] = true;
						//on enlève le point visité et traité et on change la valeur visitée à "true"

					}
				}catch(Exception e){
					//si on sort de l'image
				}//fin catch	
			}//fin for j
		}//fin for i

		//on enlève le point de la file
		pilePointCC.remove(0);

		return pilePointCC;
	}


	private static boolean[][] initTabBool(ImageProcessor ip){
		boolean[][] visited = new boolean[ip.getWidth()][ip.getHeight()];

		//initialisation du tableau
		for(int x=0;x<ip.getWidth();x++){
			for(int y=0;y<ip.getHeight();y++){
				visited[x][y] = false;
			}
		}

		return visited;
	}
}