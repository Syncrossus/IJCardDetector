package statistic;

import java.io.File;
import java.util.List;

import binarisation.Otsu_;
import edges.Canny_;
import extraction.Hough_;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import mean.FiltreGaussien_;
import templateMatching.TemplateMatching_;
import tools.Card;
import tools.Line;

public class Statistic_ implements PlugInFilter{

	public Statistic_(){
		this.initTemplates("src/image"); //TODO revoir emplacement
	}

	/** 
	 * Inits the templates attributes.
	 * Create an ImagePlus object for each template in the given directory
	 * @params path the relative path of the directory containing the templates
	 */
	private void initTemplates(String path){

		//Convention : nom de carte "numéro_figure_divers.png"

		Opener opener = new Opener();  
		File dir = new File(path);
		File[] files = dir.listFiles();
		
		//résultats des stats
		int nbValide = 0, nbValideAMoitie = 0, nbInvalide = 0;
		int nbCards = 0;

		for(File file:files){

			//**********************************************************************************//
			//OUVERTURE DE LA CARTE DANS LE DOSSIER PREVU A CET EFFET

			ImagePlus implus = opener.openImage(file.getAbsolutePath());

			//**********************************************************************************//





			//**********************************************************************************//
			//TRAITEMENT DE LA CARTE (MEME METHODE QUE LE MAIN)

			//création de l'image processor pour les traitements
			ImageProcessor ip = implus.getProcessor();
			
			ip = Otsu_.apply(ip);

			ImageProcessor result  = FiltreGaussien_.apply(ip, 3);
			result = Otsu_.apply(result);	 
			// Extraction de la carte

			Canny_ cannyFilter = new Canny_(result);
			result = cannyFilter.apply(5);

			Hough_ houghFilter = new Hough_(result);
			//Affichage de la detection simple de Hough
			List<Line> lines = houghFilter.apply();

			try{
				Card card = new Card(lines);	
				ip = card.extractCorner(ip);
			}
			catch(RuntimeException e){

			}

			ip = Otsu_.apply(ip);

			//**********************************************************************************//





			//**********************************************************************************//
			//RECUPERATION DES CHAINES DE CARACTERES

			//récupération de la chaine résultat du template matching
			TemplateMatching_ matcher = new TemplateMatching_();
			String cardResult = matcher.statCC(ip);

			//récupération du nom de l'image
			String imageName = file.getName();
			
			//**********************************************************************************//
			

			
			

			//**********************************************************************************//
			//INITIALISATION DES CHAINES DE CARACTERES

			//dépend du résultat de la CC
			String figureResult = "";
			String numberResult = "";
			StringBuffer sb = new StringBuffer();

			//dépend du nom de l'image analysée
			String figure = "";
			String number = "";
			
			//**********************************************************************************//
			
			
			
			
			
			//**********************************************************************************//
			//TRAITEMENT DE LA CHAINE DE CARACTERE DU NOM DE L'IMAGE
			
			int i = 0;
			//on vérifie qu'on a pas atteint la fin de la chaine et que le caractère courant est différent de "_"
			while(sb.length()<imageName.length() && imageName.charAt(i)!='_'){
				sb.append(imageName.charAt(i));
				i++;
			}
			number = sb.toString();
			i++; // on saute le "_"
			sb = new StringBuffer();

			//on vérifie qu'on a pas atteint la fin de la chaine et que le caractère courant est différent de "_" ou de "."
			while(sb.length()<imageName.length() && imageName.charAt(i)!='_' && imageName.charAt(i)!='.'){
				sb.append(imageName.charAt(i));
				i++;
			}
			figure = sb.toString();
			
			//**********************************************************************************//
			
			
			
			
			
			//**********************************************************************************//
			//TRAITEMENT DE LA CHAINE DE CARACTERE OBTENUE AVEC LE TEMPLATE MATCHING
			
			//format : "1.png pique.png 50.3232233"
			
			i = 0;
			sb = new StringBuffer();
			
			//on ajoute tous les caracteres jusqu'au ".png"
			while(sb.length()<cardResult.length() && cardResult.charAt(i)!='.'){
				sb.append(cardResult.charAt(i));
				i++;
			}
			//les nombres sont tjs ajoutés en premier dans la chaine de caractère (CC plus haute que celle de la figure)
			numberResult = sb.toString();
			
			//on veut atteindre la sous-chaine correspondant à la figure
			while(sb.length()<cardResult.length() && cardResult.charAt(i)!=' '){
				i++;
			}
			i++;//on saute l'espace
			sb = new StringBuffer();
			
			//on ajoute tous les caracteres entre l'esapce et le ".png" suivant
			while(sb.length()<cardResult.length() && cardResult.charAt(i)!='.'){
				sb.append(cardResult.charAt(i));
				i++;
			}
			figureResult = sb.toString();
			
			//**********************************************************************************//

			
			
			
			
			//**********************************************************************************//
			//STATISTIQUES EFFECTUEES
			
			if(numberResult == number && figureResult == figure){
				IJ.showMessage("Valide");
				nbValide++;
			}
			else if(numberResult == number){
				IJ.showMessage("Nombre valide et Figure invalide");
				nbValideAMoitie++;
			}
			else if(figureResult == figure){
				IJ.showMessage("Figure valide et nombre invalide");
				nbValideAMoitie++;
			}
			else{
				IJ.showMessage("Invalide");
				nbInvalide++;
			}
			nbCards++;
			
			//**********************************************************************************//

		}//fin for toutes les cartes
		
		
		
		
		//affichage des stats obtenues
		IJ.showMessage("Pourcentage valide : " + nbValide/nbCards + 
				"\nPourcentage à moitié valide : " + nbValideAMoitie/nbCards + 
				"\nPourcentage invalide : " + nbInvalide/nbCards		
		);
		
		
	}

	@Override
	public void run(ImageProcessor ip) {

	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
}
