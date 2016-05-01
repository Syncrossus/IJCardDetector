package card_detection;

import java.util.List;

import binarisation.Otsu_;
import edges.Canny_;
import extraction.Hough_;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import mean.FiltreGaussien_;
import templateMatching.TemplateMatching_;
import tools.Card;
import tools.Line;

public class Main_ implements PlugInFilter{
	@Override
	public void run(ImageProcessor ip) {

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
			// Template matching sur la carte	

			// Template matching sur l'image générale
			ImagePlus imp = new ImagePlus("Apres extraction", ip);
			new ImageWindow(imp);
		}
		catch(RuntimeException e){
			
		}
		
		ip = Otsu_.apply(ip);
		TemplateMatching_ matcher = new TemplateMatching_();
		matcher.run(ip);
	}


	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
}