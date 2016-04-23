package extraction;

import java.util.List;

import tools.Line;

import java.util.ArrayList;

public class CardFinder {
	public static final int ANGLE_DIFFERENCE_MAX = 20;

	/**
	 * Returns the card detected among the given lines
	 * @param lines the analyzed lines
	 * @return the detected card
	 */
	public static List<Line> findCard(List<Line> lines){
		/*
		 *  Version naïve ne prenant pas en compte les cartes multiples ni les erreurs de détections
		 */
		boolean cardFound = false;
		List<Line> result = new ArrayList<Line>();
		
		while (!cardFound && !lines.isEmpty()){
			Line line = lines.remove(0);
			result.add(line);
			
			// Permet de mémoriser le nombre de droite identifiée
			boolean parallele = false;
			int cptPerp = 0;
			
			for(Line other:lines){
				int differenceTheta = (int) Math.abs(line.getThetaDegree() - other.getThetaDegree());
				
				// les droites sont perpendiculaires
				if(Math.abs(90-differenceTheta) < ANGLE_DIFFERENCE_MAX && cptPerp < 2){
					cptPerp++;
					result.add(other);
				}
				
				// les droites sont parallèles
				else if(differenceTheta < ANGLE_DIFFERENCE_MAX){
					double differenceRho =  Math.abs(line.getRho() - other.getRho());
					if(differenceRho > 100 && !parallele){
						parallele = true;
						result.add(other);
					}
				}
			}
			
			// 2 perpendiculaires et 1 parallèles identifiées
			cardFound = (parallele && cptPerp == 2);
			if(!cardFound){
				result.clear();
			}
		}
		
		// on retourne provisoirement une liste de carte -> amené à être une carte
		return result;
	}
}
