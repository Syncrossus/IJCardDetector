package extraction;

import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class CCTest_ implements PlugInFilter{
	
	@Override
	public void run(ImageProcessor ip) {
		//nombre de composantes connexes de pixels à 0
		ArrayList<ConnectedComponent> cc_0 = CCIdentifier.getCC(0, ip);
		//nombre de composantes connexes de pixels à 255
		//ArrayList<ConnectedComponent> cc_255 = CCIdentifier.getCC(255, ip);
		
		IJ.showMessage("nombre de cc 0 : " + cc_0.size() /*+ " ; nombre de cc 255 : " + cc_255.size()*/);

		
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
}
