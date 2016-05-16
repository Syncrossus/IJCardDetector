package statistic;

import java.io.File;

import card_detection.Main_;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Statistic_ implements PlugInFilter{

	public Statistic_(){
		this.initImageRef("dataset/normal");
	}

	private void initImageRef(String path){

		//Convention : nom de carte "divers_numero-figure.png"

		Opener opener = new Opener();  
		File dir = new File(path);
		File[] files = dir.listFiles();

		//résultats des stats
		int nbValide = 0, nbValideAMoitie = 0, nbInvalide = 0;
		int nbCards = 0;

		for(File file:files){
			//**********************************************************************************//
			// APPLICATION TEMPLATE MATCHING
			ImagePlus implus = opener.openImage(file.getAbsolutePath());
			String cardResult = Main_.launch(implus.getProcessor());
			String imageName = file.getName();

			//**********************************************************************************//

			//**********************************************************************************//
			// COMPARAISON DES RESULTATS

			// On récupère le numero et la couleur
			if(cardResult.contains("_")){
				String figure = cardResult.substring(cardResult.indexOf("_")+1, cardResult.length());
				String number = cardResult.substring(0, cardResult.indexOf("_"));;
	
				if(imageName.contains(figure) && imageName.contains(number)){
					nbValide++;
				}
				else if(imageName.contains(figure) || imageName.contains(number)){
					nbValideAMoitie++;
				}
				else{
					nbInvalide++;
				}
	
				nbCards++;
			}
			else{
				if(imageName.contains(cardResult)){
					nbValide++;
				}
				else{
					nbInvalide++;
				}
			}
			
	
		}//fin for toutes les cartes


		try{
			//affichage des stats obtenues
			IJ.showMessage("Pourcentage valide : " + ((double)nbValide/(double)nbCards)*100 + " %" + 
					"\nPourcentage à moitié valide : " + ((double)nbValideAMoitie/(double)nbCards)*100 + " %" +  
					"\nPourcentage invalide : " + ((double)nbInvalide/(double)nbCards)*100 + " %"	
					);

		}catch(Exception e){
			//si pas d'image dans le dossier, division par 0
			IJ.showMessage("Pas d'image dans le dossier");
		}
	}

	@Override
	public void run(ImageProcessor ip) {

	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
}
