package templateMatching;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import binarisation.Otsu_;
import extraction.CCIdentifier;
import extraction.ConnectedComponent;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import morpho.ElementStructurant;
import morpho.Morpho;
import scaling.Resizer;
import tools.Convolution;

public class TemplateMatching_ implements PlugInFilter{
	
	private List<ImagePlus> templates = new ArrayList<ImagePlus>();
	private double size;
	
	public TemplateMatching_(){
		this.initTemplates("plugins/image");
	}
	
	/** 
	 * Inits the templates attributes.
	 * Create an ImagePlus object for each template in the given directory
	 * @params path the relative path of the directory containing the templates
	 */
	private void initTemplates(String path){
		Opener opener = new Opener();  
		File dir = new File(path);
		File[] files = dir.listFiles();
		for(File file:files){
			ImagePlus imp = opener.openImage(file.getAbsolutePath());
			// Tant qu'on a pas des images en noir et blanc, c'est provisoire mais ça permet de faire le café
			// Mais pour du code évolutif, cette ligne a rien à faire la. 
			// L'utilisateur est libre de faire du template matching par niveau de gris ou non.
			imp.setProcessor(Otsu_.apply(imp.getProcessor()));
			
			imp.setTitle(file.getName());
			templates.add(imp);
		}
		
		size = templates.get(0).getWidth();
	}
	
	/**
	 * Match the given ConnectedComponent with all the template and return the string corresponding to the best match
	 * @param cc the given connected component
	 * @return the name of the template which gave the best match with the cc.
	 */
	public String matchCC(ConnectedComponent cc){
		ImageProcessor temp = cc.createImage();
		ImageProcessor image = new ByteProcessor(temp, true);
		
		Morpho.fermeture(temp, ElementStructurant.creerRectangleCentre(3, 3), image);	
		
		//image = Resizer.scale(image, size/image.getWidth());
		ImagePlus imp = new ImagePlus("Connected Component", image);
		new ImageWindow(imp);
		
		double scale = size/image.getWidth();
		image = Resizer.scale(image, scale);
		
		double max = - Double.MAX_VALUE;
		String result = null;
		
		for(ImagePlus template:templates){
			double value = Convolution.getPercent(template.getProcessor(), image);
			if(value>max){
				result = template.getTitle();
				max = value;
			}
		}
		
		IJ.showMessage(result + ": " + max);
		if(max<60){
			result = null;
		}
		
		return result;
	}
	
	/**
	 * Runs the template matching on the given image
	 */
	public void run(ImageProcessor ip){
		List<ConnectedComponent> ccs = CCIdentifier.getCC(0, ip);
		String result = "";
		for(ConnectedComponent cc:ccs){
			String match = matchCC(cc);
			if(match != null){
				result += " " + match;
			}
		}
		
		IJ.showMessage(result);
	}
	
	/**
	 * 
	 * Même méthode que run mais permet de retourner String affichée
	 */
	public String statCC(ImageProcessor ip){
		List<ConnectedComponent> ccs = CCIdentifier.getCC(0, ip);
		String result = "";
		for(ConnectedComponent cc:ccs){
			String match = matchCC(cc);
			if(match != null){
				result += " " + match;
			}
		}
		return result;
	}
	
	/**
	 * Obsolète, mais peut servir. Autant le garder tant qu'on a pas une architecture fixe.
	 */
	private static double startTemplateMatching(ImageProcessor ip, ImageProcessor template){

		/*//pourcentage de la ccn obtenu
		double percent = 0;

		//on récupère la liste de toutes les CC noires de la carte
		ArrayList<ConnectedComponent> ccs = CCIdentifier.getCC(0, ip);

		//on applique le template matching dessus

		//1 ) soit on applique le tm en faisant correspondre 1 pixel du template sur chaque pixel de la cc détectée (surement très long et peu optimisé)
		//2 ) soit on fait correspondre le pixel le plus en haut à gauche de la cc au pixel le plus en haut à gauche du template (peut être très très approximatif)

		//---> 2) 

		Point topleftTemplate = ipPss;

		//on récupère le pixel en haut à gauche du template pour effectuer le décalage
		for(int i = 0;i<template.getWidth(); i++){

			for(int j = 0;j<template.getHeight(); j++){
				if(template.getPixel(i, j) == 0){
					topleftTemplate = new Point(i, j);
					break;
				}
			}//fin for j

			if(topleftTemplate!=ipPss){
				break;
			}
		}//fin for i

		//on calcule le décalage entre le bord de l'image et le pixel noir obtenu précedemment
		double decalageX = topleftTemplate.getX();
		double decalageY = topleftTemplate.getY();

		for(ConnectedComponent cc:ccs){

			//on copie la partie de l'image qui nous interresse
			ImageProcessor ipBis = new ByteProcessor(template.getWidth(), template.getHeight());
			for(int x = 0;x<ipBis.getWidth(); x++){
				for(int y = 0;y<ipBis.getHeight(); y++){

					//on découpe l'image pour faire correspondre les pixels noirs en haut à gauche
					int coordX = (int)(cc.getPoints().get(0).getX() - decalageX + x);
					int coordY = (int)(cc.getPoints().get(0).getY() - decalageY + y);

					//si ça sort de l'image, on prend le pixel en haut à gauche de l'image
					if(coordX<0) {
						coordX=0;
					}
					else if(coordX>=ip.getWidth()){
						coordX = ip.getWidth()-1;
					}
					if(coordY<0 ){
						coordY=0;
					}
					else if(coordY>=ip.getHeight()){
						coordY = ip.getHeight()-1;
					}


					ipBis.putPixel(x, y, ip.getPixel(coordX, coordY));
				}
			}

			//on applique la ccn en retournant celle qui a le meilleur résultat
			0;//TODO
			if(result > percent)
				percent = result;

		}//fin for cc

		return percent;*/
		//return Convolution.correlationCroisee(template, ip);
		return Convolution.getPercent(template, ip);
	}

	@Override
	public int setup(String args, ImagePlus imp) {
		return NO_CHANGES + DOES_8G;
	}
}
