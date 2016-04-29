package templateMatching;
import java.awt.Point;
import java.util.ArrayList;

import extraction.CCIdentifier;
import extraction.ConnectedComponent;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import tools.Convolution;

public class TemplateMatching_ implements PlugInFilter{
	
	private static double startTemplateMatching(ImageProcessor ip, ImageProcessor template){
		
		//pourcentage de la ccn obtenu
		double percent = 0;
		
		//on récupère la liste de toutes les CC noires de la carte
		ArrayList<ConnectedComponent> ccs = CCIdentifier.getCC(0, ip);
		
		//on applique le template matching dessus
		
		//1 ) soit on applique le tm en faisant correspondre 1 pixel du template sur chaque pixel de la cc détectée (surement très long et peu optimisé)
		//2 ) soit on fait correspondre le pixel le plus en haut à gauche de la cc au pixel le plus en haut à gauche du template (peut être très très approximatif)
		
		//---> 2) 
		
		Point topleftTemplate = null;
		
		//on récupère le pixel en haut à gauche du template pour effectuer le décalage
		for(int i = 0;i<template.getWidth(); i++){
			
			for(int j = 0;j<template.getHeight(); j++){
				if(template.getPixel(i, j) == 0){
					topleftTemplate = new Point(i, j);
					break;
				}
			}//fin for j
			
			if(topleftTemplate!=null){
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
					if(coordX<0 || coordX>=ip.getWidth()){
						coordX=0;
					}
					if(coordY<0 || coordY>=ip.getHeight()){
						coordY=0;
					}
					
					ipBis.putPixel(x, y, ip.getPixel(coordX, coordY));
				}
			}
			
			//on applique la ccn en retournant celle qui a le meilleur résultat
			double result =  /*Convolution.correlationCroisee(template, ipBis)*/0;//TODO
			if(result > percent)
				percent = result;
			
		}//fin for cc
		
		return percent;
	}
	
	
	@Override
	public void run(ImageProcessor ip) {
		//on l'applique pour chaque template et on garde celui ayant le meilleur résultat
		//TODO mettre lien templates
		double figure = 0;
		
		String fig = "trefle";
		double trefle = startTemplateMatching(ip, null);
		figure = trefle;
		
		double pique = startTemplateMatching(ip, null);
		if(pique>figure){
			figure = pique;
			fig = "pique";
		}
		double coeur = startTemplateMatching(ip, null);
		
		if(coeur>figure){
			figure = coeur;
			fig = "coeur";
		}
		
		double carreau = startTemplateMatching(ip, null);
		if(carreau>figure){
			figure = carreau;
			fig = "carreau";
		}
		
		double number = 0;
		String num = "";
		
		double as = startTemplateMatching(ip, null);
		number = as;
		num = "As de ";
		
		double deux = startTemplateMatching(ip, null);
		if(deux>number){
			number = deux;
			num = "Deux de ";
		}
		
		double trois = startTemplateMatching(ip, null);
		if(trois>number){
			number = trois;
			num = "Trois de ";
		}
		
		double quatre = startTemplateMatching(ip, null);
		if(quatre>number){
			number = quatre;
			num = "Quatre de ";
		}
		
		double cinq = startTemplateMatching(ip, null);
		if(cinq>number){
			number = cinq;
			num = "Cinq de ";
		}
		
		double six = startTemplateMatching(ip, null);
		if(six>number){
			number = six;
			num = "Six de ";
		}
		
		double sept = startTemplateMatching(ip, null);
		if(sept>number){
			number = sept;
			num = "Sept de ";
		}
		
		double huit = startTemplateMatching(ip, null);
		if(huit>number){
			number = huit;
			num = "Huit de ";
		}
		
		double neuf = startTemplateMatching(ip, null);
		if(neuf>number){
			number = neuf;
			num = "Neuf de ";
		}
		
		double zero = startTemplateMatching(ip, null);
		if((zero + as)/2>number){
			number = (zero + as)/2;
			num = "Dix de ";
		}
		
		double v = startTemplateMatching(ip, null);
		if(v>number){
			number = v;
			num = "Valet de ";
		}
		
		double d = startTemplateMatching(ip, null);
		if(d>number){
			number = d;
			num = "Dame de ";
		}
		
		double r = startTemplateMatching(ip, null);
		if(r>number){
			number = r;
			num = "Roi de ";
		}
		
		double j = startTemplateMatching(ip, null);
		if(j>number){
			number = j;
			num = "Valet de ";
		}
		
		double q = startTemplateMatching(ip, null);
		if(q>number){
			number = q;
			num = "Dame de ";
		}
		
		double k = startTemplateMatching(ip, null);
		if(k>number){
			number = k;
			num = "Roi de ";
		}
		
		IJ.showMessage(num + fig);
    }

	@Override
    public int setup(String args, ImagePlus imp) {
        return NO_CHANGES + DOES_8G;
    }
}
