package templateMatching;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import tools.Convolution;

public class TemplateMatching_ implements PlugInFilter{

	private static double startTemplateMatching(ImageProcessor ip, ImageProcessor template){

		/*//pourcentage de la ccn obtenu
		double percent = 0;

		//on récupère la liste de toutes les CC noires de la carte
		ArrayList<ConnectedComponent> ccs = CCIdentifier.getCC(0, ip);

		//on applique le template matching dessus

		//1 ) soit on applique le tm en faisant correspondre 1 pixel du template sur chaque pixel de la cc détectée (surement très long et peu optimisé)
		//2 ) soit on fait correspondre le pixel le plus en haut à gauche de la cc au pixel le plus en haut à gauche du template (peut être très très approximatif)

		//---> 2) 

		Point topleftTemplate = ipPss;

		//on récupère le pixel en haut à gauche du template pour effectuer le décalage
		for(int i = 0;i<template.getWidth(); i++){

			for(int j = 0;j<template.getHeight(); j++){
				if(template.getPixel(i, j) == 0){
					topleftTemplate = new Point(i, j);
					break;
				}
			}//fin for j

			if(topleftTemplate!=ipPss){
				break;
			}
		}//fin for i

		//on calcule le décalage entre le bord de l'image et le pixel noir obtenu précedemment
		double decalageX = topleftTemplate.getX();
		double decalageY = topleftTemplate.getY();

		for(ConnectedComponent cc:ccs){

			//on copie la partie de l'image qui nous interresse
			ImageProcessor ipBis = new ByteProcessor(template.getWidth(), template.getHeight());
			for(int x = 0;x<ipBis.getWidth(); x++){
				for(int y = 0;y<ipBis.getHeight(); y++){

					//on découpe l'image pour faire correspondre les pixels noirs en haut à gauche
					int coordX = (int)(cc.getPoints().get(0).getX() - decalageX + x);
					int coordY = (int)(cc.getPoints().get(0).getY() - decalageY + y);

					//si ça sort de l'image, on prend le pixel en haut à gauche de l'image
					if(coordX<0) {
						coordX=0;
					}
					else if(coordX>=ip.getWidth()){
						coordX = ip.getWidth()-1;
					}
					if(coordY<0 ){
						coordY=0;
					}
					else if(coordY>=ip.getHeight()){
						coordY = ip.getHeight()-1;
					}


					ipBis.putPixel(x, y, ip.getPixel(coordX, coordY));
				}
			}

			//on applique la ccn en retournant celle qui a le meilleur résultat
			0;//TODO
			if(result > percent)
				percent = result;

		}//fin for cc

		return percent;*/
		return Convolution.correlationCroisee(template, ip);
	}


	@Override
	public void run(ImageProcessor ip) {
		//on l'applique pour chaque template et on garde celui ayant le meilleur résultat
		String fig = "";
		String num = "";

		double figure = 0;
		double number = 0;
		
		
		//*********************************************************************************//
		//FIGURE
		
		//TREFLE
		try{

			fig = "trefle";
			double trefle = startTemplateMatching(ip, loadImg("src/image/trefle.png"));
			figure = trefle;

		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template trefle");
		}

		//PIQUE
		try{

			double pique = startTemplateMatching(ip, loadImg("src/image/pic.png"));

			if(pique>figure){
				figure = pique;
				fig = "pique";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template pique");
		}

		//COEUR
		try{

			double coeur = startTemplateMatching(ip, loadImg("src/image/coeur.png"));

			if(coeur>figure){
				figure = coeur;
				fig = "coeur";
			}

		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template coeur");
		}

		//CARREAU
		try{

			double carreau = startTemplateMatching(ip, loadImg("src/image/carreau.png"));
			if(carreau>figure){
				figure = carreau;
				fig = "carreau";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template carreau");
		}

		//*********************************************************************************//
		
		
		
		
		
		
		//*********************************************************************************//
		//NOMBRES
		
		//AS
		try{
			double as = startTemplateMatching(ip, loadImg("src/image/ace.png"));
			number = as;
			num = "As de ";
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template as");
		}
		
		//DEUX
		try{
			double deux = startTemplateMatching(ip, loadImg("src/image/2.png"));
			if(deux>number){
				number = deux;
				num = "Deux de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 2");
		}
		
		//TROIS
		try{
			double trois = startTemplateMatching(ip, loadImg("src/image/3.png"));
			if(trois>number){
				number = trois;
				num = "Trois de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 3");
		}
		
		//QUATRE
		try{
			double quatre = startTemplateMatching(ip, loadImg("src/image/4.png"));
			if(quatre>number){
				number = quatre;
				num = "Quatre de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 4");
		}
		
		//CINQ
		try{
			double cinq = startTemplateMatching(ip, loadImg("src/image/5.png"));
			if(cinq>number){
				number = cinq;
				num = "Cinq de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 5");
		}
		
		//SIX
		try{
			double six = startTemplateMatching(ip, loadImg("src/image/6.png"));
			if(six>number){
				number = six;
				num = "Six de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 6");
		}
		
		//SEPT
		try{
			double sept = startTemplateMatching(ip, loadImg("src/image/7.png"));
			if(sept>number){
				number = sept;
				num = "Sept de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 7");
		}
		
		
		//HUIT
		try{
			double huit = startTemplateMatching(ip, loadImg("src/image/8.png"));
			if(huit>number){
				number = huit;
				num = "Huit de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 8");
		}
		
		//NEUF
		try{
			double neuf = startTemplateMatching(ip, loadImg("src/image/9.png"));
			if(neuf>number){
				number = neuf;
				num = "Neuf de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 9");
		}
		
		//DIX
		try{
			double un = startTemplateMatching(ip, loadImg("src/image/0.png"));
			double zero = startTemplateMatching(ip, loadImg("src/image/1.png"));
			if((zero + un)/2>number){
				number = (zero + un)/2;
				num = "Dix de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template 10");
		}
		
		//VALET
		try{
			double v = startTemplateMatching(ip, loadImg("src/image/valet.png"));
			if(v>number){
				number = v;
				num = "Valet de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template valet");
		}
		
		//DAME
		try{
			double d = startTemplateMatching(ip, loadImg("src/image/dame.png"));
			if(d>number){
				number = d;
				num = "Dame de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template dame");
		}
		
		//ROI
		try{
			double r = startTemplateMatching(ip, loadImg("src/image/roi.png"));
			if(r>number){
				number = r;
				num = "Roi de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template roi");
		}
		
		//JACK
		try{
			double j = startTemplateMatching(ip, loadImg("src/image/jack.png"));
			if(j>number){
				number = j;
				num = "Valet de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template jack");
		}
		
		//QUEEN
		try{
			double q = startTemplateMatching(ip, loadImg("src/image/queen.png"));
			if(q>number){
				number = q;
				num = "Dame de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template queen");
		}
		
		//KING
		try{
			double k = startTemplateMatching(ip, loadImg("src/image/king.png"));
			if(k>number){
				number = k;
				num = "Roi de ";
			}
		}catch(Exception e){
			e.printStackTrace();
			IJ.showMessage("Une erreur est survenue pour le template king");
		}
		//*********************************************************************************//
		
		//AFFICHAGE DU RESULTAT OBTENU
		IJ.showMessage(num + fig);
	}

	private static ImageProcessor loadImg(String url){
		Opener opener = new Opener();  
		ImagePlus imp = opener.openImage(url);
		return imp.getProcessor();
	}

	@Override
	public int setup(String args, ImagePlus imp) {
		return NO_CHANGES + DOES_8G;
	}
}
