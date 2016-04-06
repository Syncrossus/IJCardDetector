package rotation;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;


public class RotationPetation_ implements PlugInFilter {
	/**
	 * 
	 * @param ip image to rotate
	 * @param theta the angle to rotate the image
	 * This function rotates a card with the equation of a line
	 */
	public static ImageProcessor rotate(ImageProcessor ip, int angle){
		
		ImageProcessor ipRotate = new ByteProcessor(ip, false);
		ImageProcessor dst = new ByteProcessor(ipRotate.getWidth(), ipRotate.getHeight());
		
		//calcul de l'angle en radian
        double theta = angle/(180.0/Math.PI); 
        
		//tourne l'image par rapport au centre de l'image d'origine
		int centerX = ip.getWidth()/2;
        int centerY = ip.getHeight()/2;
        
		for(int i=0; i<ip.getWidth();i++){
			for(int j=0;j<ip.getHeight();j++){
				
				//récupère la valeur du pixel concerné
				int value = ip.getPixel(i, j);
				
				//récupère les nouvelles coordonnées du pixel concerné
                int coordX = (int)((i-centerX)*Math.cos(theta) - (j-centerY)*Math.sin(theta)+centerX); 
                int coordY = (int)((i-centerX)*Math.sin(theta) + (j-centerY)*Math.cos(theta)+centerY);
                
                //place le pixel dans la nouvelle image
				try{
					dst.putPixel(coordX, coordY, value);
				}
				catch(Exception e){
					//ne fait rien dans le cas où ça sort de l'image
					
				}
				
			}
		}
		return dst;
	}
	
	/**
	 * Find the angle of the line
	 * @param a in ax+b
	 * @param b in ax+b
	 * @return the angle of the line
	 */
	public int getTheta(int a, int b){
		return (int)Math.atan(b/a);
	}

	@Override
	public void run(ImageProcessor arg0) {
		ImageProcessor ipRot = rotate(arg0.duplicate(), 45);
        ImagePlus impRot = new ImagePlus("kjderhfdfbvjd", ipRot);
        new ImageWindow(impRot);
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_ALL;
	}
}
