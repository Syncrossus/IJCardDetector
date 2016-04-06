package card_detection;

import java.util.List;

import binarisation.Otsu_;
import edges.Canny_;
import extraction.CardFinder;
import extraction.Line;
import extraction.Hough_;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import mean.FiltreGaussien_;

public class Main_ implements PlugInFilter{
	@Override
	public void run(ImageProcessor ip) {
		
		ImageProcessor result = FiltreGaussien_.apply(ip, 3);
		result = Otsu_.apply(result);	 
		
		Canny_ cannyFilter = new Canny_(result);
		result = cannyFilter.apply( 5);
		ImageProcessor copy = new ByteProcessor(result, true);
		
		Hough_ houghFilter = new Hough_(result);
		
		//Affichage de la detection simple de Hough
		List<Line> lines = houghFilter.apply();
		Hough_.displayLine(result, lines);
		ImagePlus imp = new ImagePlus("Avant détéction de la carte", result);
		new ImageWindow(imp);
		
		// Affichage de la detection de carte issu de la transformée de Hough
		lines = CardFinder.findCard(lines);
		Hough_.displayLine(copy, lines);
		imp = new ImagePlus("Après détection de la carte", copy);
		new ImageWindow(imp);
	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
}