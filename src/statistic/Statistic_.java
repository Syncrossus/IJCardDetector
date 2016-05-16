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
		this.initImageRef("dataset/personnel/Hugo_Martin");
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
			
			//**********************************************************************************//


			//**********************************************************************************//
			//TRAITEMENT DE LA CHAINE DE CARACTERE DU NOM DE L'IMAGE

			/**int i = imageName.length() - 1;

				//on ne veut pas prendre en compte le .png
				while(i>=0 && imageName.charAt(i)!='.'){
					i--;
				}
				i--;//on saute le "."

				//on vérifie qu'on a pas atteint le début de la chaine et que le caractère courant est différent de "-"
				while(i>=0 && imageName.charAt(i)!='-'){
					sb.append(imageName.charAt(i));
					i--;
				}

				sb.reverse();
				IJ.showMessage(figure);
				figure = sb.toString();

				i--; // on saute le "."
				sb = new StringBuffer();

				//on vérifie qu'on a pas atteint le début de la chaine et que le caractère courant est différent de "_"
				while(i>=0 && imageName.charAt(i)!='_'){
					sb.append(imageName.charAt(i));
					i--;
				}

				sb.reverse();

				//pour virer un éventuel "0" devant le nombre
				try{
					int num = Integer.parseInt(sb.toString());
					number = String.valueOf(num);
				}catch(Exception e){
					//si la chaine est vide par exemple
				}**/

			//**********************************************************************************//


			/**IJ.showMessage("Figure : '" + figure + "' et on trouve : '" + ssChaine2 + "'-> " + (figure.equals(ssChaine2)));
				IJ.showMessage("Nombre : '" + number + "' et on trouve : '" + ssChaine1 + "'-> " + (number.equals(ssChaine1)));




				//**********************************************************************************
				//STATISTIQUES EFFECTUEES

				if(ssChaine1.equals(number) && ssChaine2.equals(figure)){
					IJ.showMessage("Valide");
					nbValide++;
				}

				//en cas d'inversion dans l'ordre de reconnaissance
				else if(ssChaine1.equals(figure) && ssChaine2.equals(number)){
					IJ.showMessage("Valide");
					nbValide++;
				}

				else if(ssChaine1.equals(number)){
					IJ.showMessage("Nombre valide et Figure invalide");
					nbValideAMoitie++;
				}

				//en cas d'inversion
				else if(ssChaine2.equals(number)){
					IJ.showMessage("Figure valide et nombre invalide");
					nbValideAMoitie++;
				}

				else if(ssChaine2.equals(figure)){
					IJ.showMessage("Figure valide et nombre invalide");
					nbValideAMoitie++;
				}

				//en cas d'inversion
				else if(ssChaine2.equals(number)){
					IJ.showMessage("Figure valide et nombre invalide");
					nbValideAMoitie++;
				}

				else{
					IJ.showMessage("Invalide");
					nbInvalide++;
				}
				nbCards++;

				//**********************************************************************************
			}catch(Exception e){
				//si pb lors de l'ouverture d'un fichier
				IJ.showMessage("Pb");
			}

			 */
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
