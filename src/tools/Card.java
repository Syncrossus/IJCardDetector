package tools;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import rotation.Rotation;

public class Card {
	public static final int ANGLE_DIFFERENCE_MAX = 20;
	private List<Line> lines = new ArrayList<Line>();
	
	public Card(List<Line> lines){
		this.initLines(lines);
		this.initCorners();
	}
	
	public List<Line> getLines(){
		return lines;
	}
	
	/**
	 * Inits the lines attribute with the given lines
	 * A card is identified among the given lines
	 * @param givenLines the analyzed lines
	 * @return the detected card
	 * @throws A RuntimeException is thrown is no card is detected
	 */
	private void initLines(List<Line> givenLines){
		/*
		 *  Version naïve ne prenant pas en compte les cartes multiples ni les erreurs de détections
		 */
		boolean cardFound = false;
		
		while (!cardFound && !givenLines.isEmpty()){
			Line line = givenLines.remove(0);
			this.lines.add(line);
			
			// Permet de mémoriser le nombre de droite identifiée
			boolean parallele = false;
			int cptPerp = 0;
			
			for(Line other:givenLines){
				int differenceTheta = (int) Math.abs(line.getThetaDegree() - other.getThetaDegree());
				
				// les droites sont perpendiculaires
				if(Math.abs(90-differenceTheta) < ANGLE_DIFFERENCE_MAX && cptPerp < 2){
					cptPerp++;
					this.lines.add(other);
				}
				
				// les droites sont parallèles
				else if(differenceTheta < ANGLE_DIFFERENCE_MAX){
					double differenceRho =  Math.abs(line.getRho() - other.getRho());
					if(differenceRho > 100 && !parallele){
						parallele = true;
						this.lines.add(other);
					}
				}
			}
			
			// 2 perpendiculaires et 1 parallèles identifiées
			cardFound = (parallele && cptPerp == 2);
			if(!cardFound){
				this.lines.clear();
			}
		}
		
		// on retourne provisoirement une liste de carte -> amené à être une carte
		if(this.lines.isEmpty()){
			throw new RuntimeException("No card detected");
		}
	}
	
	/**
	 * Inits the corners attribute, using the detected lines
	 */
	private void initCorners(){
		for(int i=0; i<lines.size()-1; i++){
			Line line = lines.get(i);
			for(int j=i+1; j<lines.size(); j++){
				Line other = lines.get(j);
				int x, y;
				if(Math.abs(line.getThetaDegree()-other.getThetaDegree()) > ANGLE_DIFFERENCE_MAX){	
					if(line.isVertical()){
						x = (int)line.getXIntercept();
						y = other.getY(x);
					}
					else if(other.isVertical()){
						x = (int) other.getXIntercept();
						y = line.getY(x);
					}
					else{
						x =(int)(-(line.getYIntercept() - other.getYIntercept()) / (line.getSlope() - other.getSlope()));
						y = line.getY(x);
					}
					
		
					line.addCorner(new Point(x,y));
					other.addCorner(new Point(x,y));
				}
			}
		}
	}

	/**
	 * @return the rotation radius of the card
	 */
	public double getRotationRadius(){
		Line result = lines.get(0);
		
		for(int i=1; i<lines.size(); i++){
			Line current = lines.get(i);
			if(result.getLength()<current.getLength()){
				result = current;
			}
		}
		
		return result.getTheta();
	}
	

	/**
	 * Rotate the card's lines using the given radius.
	 * All the contained lines of the card are rotated.
	 * @param radius the radius used to rotate
	 */
	public void rotate(ImageProcessor ip, double radius){
		// Rotation des lignes
		int centerX = ip.getWidth()/2;
		int centerY = ip.getHeight()/2;
		
		for(Line line:lines){
			for(Point corner: line.getCorners()){
				double x2 = (int)((corner.x-centerX)*Math.cos(radius) + (corner.y-centerY)*Math.sin(radius))+centerX;
				double y2 = (int)(-(corner.x-centerX)*Math.sin(radius) + (corner.y-centerY)*Math.cos(radius)) + centerY;
			
				corner.x = (int) x2;
				corner.y = (int) y2;
			}
			
			// Est ce que modifier ça de la sorte a du sens ? En vrai je pense pas.
			// Mais j'aimerais bien rotate les droites en vrai.
			//line.setTheta(line.getTheta() + radius);
		}
		
	}
	
	/**
	 * Returns a new ImageProcessor which contains only the detected card.
	 * @param ip the image processor
	 * @param rotate the image is rotated if true
	 */
	public ImageProcessor extract(ImageProcessor ip){
		double radius = this.getRotationRadius();
		ImageProcessor rotated = Rotation.rotate(ip, -radius);
		this.rotate(ip, radius);

		int x1 = Integer.MAX_VALUE, x2 = 0, y1 = Integer.MAX_VALUE, y2 = 1;
		
		// en vrai c'est barbare comme façon de faire. 
		// Surtout qu'on a théoriquement besoin de considerer deux lignes.
		// je vais retoucher l'architecture du code, histoire de faire ça proprement. 
		// Pour l'instant je veux juste que ça marche.
		for(Line line:lines){
			for(Point corner: line.getCorners()){
				if(corner.getX()>x2){
					x2 = (int)corner.getX();
				}
				if(corner.getX()<x1){
					x1 = (int)corner.getX();
				}
				if(corner.getY()>y2){
					y2 = (int)corner.getY();
				}
				if(corner.getY()<y1){
					y1 = (int)corner.getY();
				}
			}
		}
		
		ImageProcessor result = new ByteProcessor(x2-x1, y2-y1);
		
		for(int i=0; i<result.getWidth(); i++){
			for(int j=0; j<result.getHeight(); j++){
				result.putPixel(i, j, rotated.getPixel(i+x1, j+y1));
			}
		}
		
		return result;
	}

	
	public ImageProcessor extractCorner(ImageProcessor ip){
		ImageProcessor card = this.extract(ip);
		double COEFF_CORNER_WIDTH = 0.15;
		double COEFF_CORNER_HEIGHT = 0.30;
		double COEFF_CORNER_START = 0.20;
		
		ImageProcessor result = new ByteProcessor((int) (card.getWidth()*COEFF_CORNER_WIDTH), (int)(card.getHeight()*COEFF_CORNER_HEIGHT));
		int x = (int) (result.getWidth()*COEFF_CORNER_START);
		int y = (int) (result.getWidth()*COEFF_CORNER_START);
		
		for(int i=x; i<result.getWidth()+x; i++){
			for(int j=y; j<result.getHeight()+y; j++){
				result.putPixel(i-x, j-y, card.getPixel(i,j));
			}
		}
		
		return result;
	}
}


