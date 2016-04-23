package rotation;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class RotationTest_ implements PlugInFilter {

	@Override
	public void run(ImageProcessor arg0) {
		ImageProcessor ipRot = Rotation.rotate(arg0, 45);
        ImagePlus impRot = new ImagePlus("Apres rotation", ipRot);
        new ImageWindow(impRot);
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
}