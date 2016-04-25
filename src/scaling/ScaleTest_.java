package scaling;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class ScaleTest_ implements PlugInFilter {

	@Override
	public void run(ImageProcessor arg0) {
		GenericDialog gd = new GenericDialog("Scaling Factor");
        gd.addNumericField("Scaling Factor", 2, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;
        int factor = (int) gd.getNextNumber();
        new ImageWindow(new ImagePlus("Scaled Image", Resizer.scale(arg0, factor)));
                
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
}