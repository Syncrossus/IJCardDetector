package rotation;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Rotation {

	/**
	 * 
	 * @param ip image to rotate
	 * @param theta the angle to rotate the image
	 * This function rotates a card with the equation of a line
	 */
	public static ImageProcessor rotate(ImageProcessor ip, double theta){

		ImageProcessor imgToRotate = new ByteProcessor(ip, true);
		ImageProcessor ipRotate = new ByteProcessor(ip.getWidth(), ip.getHeight());

		//calcul de l'angle en radian
		//double theta =  - angle/(180.0/Math.PI); 

		//tourne l'image par rapport au centre de l'image d'origine
		int centerX = ip.getWidth()/2;
		int centerY = ip.getHeight()/2;

		for(int i=0; i<ipRotate.getWidth();i++){
			for(int j=0;j<ipRotate.getHeight();j++){

				//récupère les anciennes coordonnées du pixel concerné
				int coordX = (int)((i-centerX)*Math.cos(theta) - (j-centerY)*Math.sin(theta)+centerX); 
				int coordY = (int)((i-centerX)*Math.sin(theta) + (j-centerY)*Math.cos(theta)+centerY);

				//si le pixel concerné fait bien partie de l'image
				if(coordX<ip.getWidth() && coordX>=0 && coordY<ip.getHeight() && coordY>=0){

					int value = imgToRotate.getPixel(coordX, coordY);

					//on met la valeur de ce pixel dans l'image
					ipRotate.putPixel(i, j, value);
				}
			}
		}
		return ipRotate;
	}
}
