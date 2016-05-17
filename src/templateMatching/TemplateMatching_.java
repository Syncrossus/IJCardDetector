package templateMatching;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import binarisation.Otsu_;
import extraction.CCIdentifier;
import extraction.ConnectedComponent;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import morpho.ElementStructurant;
import morpho.Morpho;
import scaling.Resizer;
import tools.Match;
import tools.MathTools;

public class TemplateMatching_ implements PlugInFilter{
	
	private List<ImagePlus> templates = new ArrayList<ImagePlus>();
	private double size;
	
	/** 
	 * Inits the templates attributes.
	 * Create an ImagePlus object for each template in the given directory
	 * @params path the relative path of the directory containing the templates
	 */
	private void initTemplates(String path){
		templates.clear();
		Opener opener = new Opener();  
		File dir = new File(path);
		File[] files = dir.listFiles();
		for(File file:files){
			ImagePlus imp = opener.openImage(file.getAbsolutePath());
			// Tant qu'on a pas des images en noir et blanc, c'est provisoire mais ça permet de faire le café
			// Mais pour du code évolutif, cette ligne a rien à faire la. 
			// L'utilisateur est libre de faire du template matching par niveau de gris ou non.
			imp.setProcessor(Otsu_.apply(imp.getProcessor()));
			
			imp.setTitle(file.getName());
			templates.add(imp);
		}
		
		size = templates.get(0).getWidth();
	}
	
	/**
	 * Match the given ConnectedComponent with all the template and return the string corresponding to the best match
	 * @param cc the given connected component
	 * @return the name of the template which gave the best match with the cc.
	 */
	public Match matchCC(ConnectedComponent cc){
		ImageProcessor temp = cc.createImage();
		ImageProcessor image = new ByteProcessor(temp, true);
		Morpho.fermeture(temp, ElementStructurant.creerRectangleCentre(3, 3), image);	
		
		double scale = size/image.getWidth();
		image = Resizer.scale(image, scale);
		
		double max = - Double.MAX_VALUE;
		Match result = null;
		
		for(ImagePlus template:templates){
			double value = getMatchPercentWithOffset(template.getProcessor(), image, 0,0);
			if(value>max){
				// Retourne uniquement le nom du template
				result = new Match(value, template.getTitle().substring(0, template.getTitle().indexOf('.')));
				max = value;
			}
		}
		
		return result;
	}
	
	/**
	 * Starts the template matching on the image
	 */
	public String launch(ImageProcessor ip){
		List<ConnectedComponent> ccs = CCIdentifier.getCC(0, ip);
		
		if(ccs.size() < 2){
			return "invalide";
		}
		// Indentification du numero
		initTemplates("src/image/numero");
		List<Match> matches = new ArrayList<Match>();
		
		for(ConnectedComponent cc:ccs){
			matches.add(matchCC(cc));
		}
		Collections.sort(matches, Collections.reverseOrder());
		matches.subList(0, 2);


		
		double min = (matches.get(0).compareTo(matches.get(1)) <= 0)? matches.get(1).getValue():matches.get(0).getValue();
		// Identification de la couleur
		initTemplates("src/image/couleur");
		Match match = new Match();
		
		for(ConnectedComponent cc:ccs){
			Match temp = matchCC(cc);
			if(temp.getValue() > match.getValue()){
				match = temp;
			}
		}

		min = (min<match.getValue())? min:match.getValue();
		if(min<50){
			return "invalide";
		}
		else{
			String numero = (matches.contains("un") && matches.contains("zero"))? "10_":matches.get(0).getTitle()+"_";
			String couleur = match.getTitle();
			return numero+couleur;
		}
	}

	public static double getMatchPercent(ImageProcessor template, ImageProcessor image){
		int windowSize = (template.getWidth()/2)%2==0?template.getWidth()/2+1:template.getWidth()/2;
		double[] values = new double[windowSize*windowSize]; // this array will contain [match percentage with i and j offset]
		
		for (int i = -windowSize/2; i < windowSize/2; i++) {      // i is the x offset in the template matching
			for (int j = -windowSize/2; j < windowSize/2; j++) {// j is the y offset in the template matching
				values[(i+windowSize/2)*(j+windowSize/2)+j+windowSize/2]=getMatchPercentWithOffset(template, image, i, j);  //match percentage at x offset i and y offset j
//				IJ.showMessage("Match percentage at "+i+", "+j+": "+a);
			}
		}
		return MathTools.max(values);
	}
	
//	public static double getPercentMean(ImageProcessor template, ImageProcessor image){
//		double[][] values = new double[(2*template.getWidth()+1)*(2*template.getHeight()+1)][3]; // this matrix will contain [match percentage with i and j offset][i][j] for every i and j
//		double value=0, sommeCoeffs=0, coeff;
//		for (int i = -template.getWidth(); i < template.getWidth(); i++) {      // i is the x offset in the template matching
//			for (int j = -template.getHeight(); j < template.getHeight(); j++) {// j is the y offset in the template matching
//				values[(i+template.getWidth())*(j+template.getHeight())+(j+template.getHeight())][0]=getPercentOld(template, image, i, j);  //match percentage at x offset i and y offset j
//				values[(i+template.getWidth())*(j+template.getHeight())+(j+template.getHeight())][1]=i; //we'll need i and j later
//				values[(i+template.getWidth())*(j+template.getHeight())+(j+template.getHeight())][2]=j;
//			}
//		}
//		
//		for (int i = 0; i < values.length; i++) {   //this loop calculates the final match percentage based on all the previously computed match percentages
//			coeff=1/(Math.hypot(values[i][1], values[i][2])+1);   //the absolute offset is the pythagorean distance between the point described by (xOffset, yOffset) and (0,0)
//															  //the importance of a value is computed by 1/absoluteOffset
//			sommeCoeffs+=coeff;  //we'll need the sum of coefficients to bring the value back to 0-100
//			value+=values[i][0]*coeff; //we add each weighted match percentage to the general match percentage
//		}
//		return value/sommeCoeffs;  // we bring back the match percentage to 0-100 and return it
//	}
	
	public static double getMatchPercentWithOffset(ImageProcessor template, ImageProcessor image, int xOffset, int yOffset){
		double pixelBlanc = 0 , pixelNoir = 0;
		double nbPixelBlancTemplate = 0, nbPixelBlancImage = 0;
		double nbPixelNoirTemplate = 0, nbPixelNoirImage = 0;

		for(int i = xOffset; i<template.getWidth()+xOffset; i++){
			for(int j = yOffset; j<template.getHeight()+yOffset; j++){
				try{
					if(template.getPixel(i, j) == image.getPixel(i, j)){
						if(template.getPixel(i, j) == 0){
							pixelNoir++;
							nbPixelNoirTemplate++;
							nbPixelNoirImage++;
						}
						else if(template.getPixel(i,j) == 255){
							pixelBlanc++;
							nbPixelBlancTemplate++;
							nbPixelBlancImage++;
						}
					}

					else{
						if (template.getPixel(i, j) == 0){
							nbPixelNoirTemplate++;
							nbPixelBlancImage++;
						}
						else{
							nbPixelBlancTemplate++;
							nbPixelNoirImage++;
						}
					}
				}
				catch(Exception e){
					//erreur si images pas de la même taille et qu'on sort de "image"
				}
			}
		}

		double nbPixelNoir = (nbPixelNoirImage<nbPixelNoirTemplate)? nbPixelNoirTemplate:nbPixelNoirImage;
		double nbPixelBlanc = (nbPixelBlancImage<nbPixelBlancTemplate)? nbPixelBlancTemplate:nbPixelBlancTemplate;
		
		return (2*((pixelNoir/nbPixelNoir) * 100) + ((pixelBlanc/nbPixelBlanc)*100))/3;
	}
	
	/**
	 * OBSOLETE
	 * Init the result string of the template matching, considering the detected matches
	 * @param matches the matches detected in the method matchCC.
	 * @return the result string of the template matching
	 */
	public String initResult(List<Match> matches){
		StringBuilder result = new StringBuilder();
		
		Collections.sort(matches, Collections.reverseOrder());
		if(matches.size() >= 3){
			matches = matches.subList(0, 3);
		
			if(matches.contains("un") && matches.contains("zero")){
				result.append("10_");
				matches.remove("un");
				matches.remove("zero");
			}
			else{
				matches.remove(2);
			}
		}
		else{
			matches.subList(0, 2);
		}
		
		for(Match match:matches){
			result.append(match.getTitle() + "_");
		}
		
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
	
	@Override
	public int setup(String args, ImagePlus imp) {
		return NO_CHANGES + DOES_8G;
	}

	@Override
	public void run(ImageProcessor ip) {
		String result = launch(ip);
		IJ.showMessage(result);
	}
}
