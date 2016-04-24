package tools;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Card {
	public static final int ANGLE_DIFFERENCE_MAX = 20;
	private List<Line> lines = new ArrayList<Line>();
	private List<Point> corners = new ArrayList<Point>();
	
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
						x = line.getXIntercept();
						y = other.getY(x);
					}
					else if(other.isVertical()){
						x = other.getXIntercept();
						y = line.getY(x);
					}
					else{
						x = -(line.getYIntercept() - other.getYIntercept()) / (line.getSlope() - other.getSlope());
						y = line.getY(x);
					}
					
					line.addCorner(new Point(x,y));
					other.addCorner(new Point(x,y));
				}
			}
		}
	}

	
	public double getRotationRadius(){
		Line result = lines.get(0);
		
		for(int i=1; i<lines.size(); i++){
			Line current = lines.get(i);
			if(result.getLength()<current.getLength()){
				result = current;
			}
		}
		
		return -result.getThetaDegree();
	}
}
