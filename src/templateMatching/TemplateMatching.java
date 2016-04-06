package templateMatching;
import java.util.Arrays;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import models.Figures;
import models.Numbers;

public class TemplateMatching {
	private static double startTemplateMatching(ImageProcessor ip, ImageProcessor template){
		//TODO si le template sort de l'image, à faire pour plusieurs taille, template étant donné à la taille minimale
		//va retourner le pourcentage max obtenu à différentes tailles
		double maxPercent = 0;
		//pour chaque pixel de l'image
		for(int i=0;i<ip.getWidth();i++){
			for(int j=0;j<ip.getHeight();j++){
				int nbPixel = 0;
				//on compare l'image ipRef au carré (pixel par pixel)
				for(int k=0;k<template.getWidth();k++){
					for(int l=0;l<template.getHeight();l++){
						if(ip.getPixel(i+k, j+l) == template.getPixel(k, l)){
							nbPixel ++;
						}
							
					}
				}
				
				
				//si le nouveau pourcentage est le maximum rencontré à cet instant
				if(nbPixel / (template.getWidth()*ip.getHeight())>maxPercent){
					//maxPercent est alors = au pourcentage de correspondance avec l'image à partir de ce pixel
					maxPercent = nbPixel / (template.getWidth()*ip.getHeight());
				}
				
			}
		}
		return maxPercent;
	}
	
	public static Figures CardFigure(ImageProcessor ip){
		//TODO revoir appel de template, fait comme ça pour ne pas faire des erreurs partout
		ImageProcessor template = new ByteProcessor(ip, true);
		
		double trefle = startTemplateMatching(ip, template);
		double carreau = startTemplateMatching(ip, template);
		double pique = startTemplateMatching(ip, template);
		double coeur = startTemplateMatching(ip, template);
		
		double[] tab = {trefle, carreau, pique, coeur};
		Arrays.sort(tab);
		
		Figures figure = null;
		
		//TODO optimisable
		if(tab[3] == trefle)
			figure = Figures.TREFLE;
		else if(tab[3] == carreau)
			figure = Figures.CARREAU;
		else if(tab[3] == pique)
			figure = Figures.PIQUE;
		else
			figure = Figures.COEUR;
		
		return figure;
	}
	
	public static Numbers CardNumber(ImageProcessor ip){
		//TODO revoir appel de template, fait comme ça pour ne pas faire des erreurs partout
		ImageProcessor template = new ByteProcessor(ip, true);
		
		double un = startTemplateMatching(ip, template);
		double deux = startTemplateMatching(ip, template);
		double trois = startTemplateMatching(ip, template);
		double quatre = startTemplateMatching(ip, template);
		double cinq = startTemplateMatching(ip, template);
		double six = startTemplateMatching(ip, template);
		double sept = startTemplateMatching(ip, template);
		double huit = startTemplateMatching(ip, template);
		double neuf = startTemplateMatching(ip, template);
		double dix = startTemplateMatching(ip, template);
		double valet = startTemplateMatching(ip, template);
		double dame = startTemplateMatching(ip, template);
		double roi = startTemplateMatching(ip, template);
		
		double[] tab = {un, deux, trois, quatre, cinq, six, sept, huit, neuf, dix, valet, dame, roi};
		Arrays.sort(tab);
		
		int nb = tab.length;
		
		Numbers number = null;
		
		//TODO optimisable
		if(tab[nb] == un)
			number = Numbers.UN;
		else if(tab[nb] == deux)
			number = Numbers.DEUX;
		else if(tab[nb] == trois)
			number = Numbers.TROIS;
		else if(tab[nb] == quatre)
			number = Numbers.QUATRE;
		else if(tab[nb] == cinq)
			number = Numbers.CINQ;
		else if(tab[nb] == six)
			number = Numbers.SIX;
		else if(tab[nb] == sept)
			number = Numbers.SEPT;
		else if(tab[nb] == huit)
			number = Numbers.HUIT;
		else if(tab[nb] == neuf)
			number = Numbers.NEUF;
		else if(tab[nb] == dix)
			number = Numbers.DIX;
		else if(tab[nb] == valet)
			number = Numbers.VALET;
		else if(tab[nb] == dame)
			number = Numbers.DAME;
		else if(tab[nb] == roi)
			number = Numbers.ROI;
		
		
		return number;
	}
}
