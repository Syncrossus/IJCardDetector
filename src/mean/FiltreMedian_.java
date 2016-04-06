package mean;
import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;
import java.util.*;

public class FiltreMedian_ implements PlugInFilter {

    public void run(ImageProcessor ip) {

        GenericDialog gd = new GenericDialog("Options du filtre median");
        gd.addNumericField("Rayon du voisinage", 1, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;

        // rayon: rayon du voisinage considere pour chaque pixel
        // Autrement dit, le voisinage est un carre de cote (2 * rayon + 1)
        int rayon = (int) gd.getNextNumber();

        ImagePlus impMedian = NewImage.createByteImage("Filtre median",
                ip.getWidth(), ip.getHeight(), 1, NewImage.FILL_BLACK);

        // ipMedian est l'image dans laquelle vous stockerez le
        // resultat du filtre median
        ImageProcessor ipMedian = impMedian.getProcessor();

        /**
         * A faire: appliquer un filtre median a l'image ip en considerant
         * un voisinage dont la taille est definie plus haut, et stoker le
         * resultat dans l'image 'ipMedian'.
         */
        
        for(int i=0; i<ip.getWidth(); i++){
        	for(int j=0; j<ip.getHeight(); j++){
        		int valeur = getMediane(ip, rayon, i, j);
        		ipMedian.putPixel(i, j, valeur);
        	}
        }

        /**
         * Fin de la partie a completer
         */
        new ImageWindow(impMedian);

    }
    /**
     * Retourne la valeur médiane de l'entourage de (i,j) dans l'image
     * @param ip l'image à modifier
     * @param rayon le rayon d'étude
     * @param col la colonne du pixel
     * @param line la ligne du pixel
     * @return la valeur médiane
     */
    public static int getMediane(ImageProcessor ip, int rayon, int col, int line){
    	int largeur = 2*rayon+1;
    	int nbElement = largeur*largeur;
    	int []temp = new int[nbElement];
    	
    	for(int i=-rayon; i<=rayon; i++){
    		for(int j=-rayon; j<=rayon; j++){
    			int x=col+i, y=line+j;
    			if(x<0){
    				x=0;
    			}
    			else if(x>=ip.getWidth()){
    				x=ip.getWidth()-1;
    			}
    			
    			if(y<0){
    				y=0;
    			}
    			else if(y>=ip.getHeight()){
    				y=ip.getHeight()-1;
    			}
    			
    			temp[(j + rayon) * largeur + i + rayon] = ip.getPixel(x, y);
    		}
    	}
    	
    	Arrays.sort(temp);
    	return temp[nbElement/2+1];
    }

    public int setup(String args, ImagePlus imp) {
        return NO_CHANGES + DOES_8G;
    }

}
