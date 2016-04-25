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
import rotation.Rotation;
import tools.Card;
import tools.Line;

public class Main_ implements PlugInFilter{
	@Override
	public void run(ImageProcessor ip) {
		
		ImageProcessor result = FiltreGaussien_.apply(ip, 3);
		result = Otsu_.apply(result);	 
		
		Canny_ cannyFilter = new Canny_(result);
		result = cannyFilter.apply(5);
		
		Hough_ houghFilter = new Hough_(result);
		//Affichage de la detection simple de Hough
		List<Line> lines = houghFilter.apply();
		
		// Affichage de la detection de carte issu de la transformée de Hough
		Card card = new Card(lines);
		result = Rotation.rotate(ip, (int)card.getRotationRadius());
		ImagePlus imp = new ImagePlus("Apres rotation", result);
        new ImageWindow(imp);
	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
}