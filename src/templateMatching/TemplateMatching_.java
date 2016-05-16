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
import tools.Convolution;
import tools.Match;

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
			double value = Convolution.getPercent(template.getProcessor(), image);
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
		initTemplates("src/templates/numero");
		List<Match> matches = new ArrayList<Match>();
		
		for(ConnectedComponent cc:ccs){
			matches.add(matchCC(cc));
		}
		Collections.sort(matches, Collections.reverseOrder());
		matches.subList(0, 2);


		
		double min = (matches.get(0).compareTo(matches.get(1)) <= 0)? matches.get(1).getValue():matches.get(0).getValue();
		// Identification de la couleur
		initTemplates("src/templates/couleur");
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
