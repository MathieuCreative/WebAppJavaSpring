package imageprocessing;

import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;


public class GrayLevelProcessing {

	public static void threshold(GrayU8 input, int t) {
		for (int y = 0; y < input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
				int gl = input.get(x, y);
				if (gl < t) {
					gl = 0;
				} else {
					gl = 255;
				}
				input.set(x, y, gl);
			}
		}
	}

	public static void modiflumi(GrayU8 input, int delta) {
		for (int y = 0; y < input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
				int gl = input.get(x, y);
				int nv_pixel = gl + delta;
				if (nv_pixel>255) {
				nv_pixel = 255;
			}
				else if  (nv_pixel<0){
					nv_pixel = 0;
				}
				input.set(x, y, nv_pixel);
			}
		}
	}
    

	public static void modifyContrast(GrayU8 input) {
		int largeur = input.getWidth();
		int hauteur = input.getHeight();
		int maxVal = Integer.MIN_VALUE; // Initialisé avec la valeur minimale pour int
		int minVal = Integer.MAX_VALUE; // Initialise avec la valeur maximale pour int
		
		// Itérer sur chaque pixel pour trouver les valeurs min et max
		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur; x++) {
				int pixelValue = input.get(x, y);
				if (pixelValue > maxVal) {
					maxVal = pixelValue; // Nouvelle valeur maximale trouvée
				}
				if (pixelValue < minVal) {
					minVal = pixelValue; // Nouvelle valeur minimale trouvée
				}
			}
		}
		for (int y = 0; y < input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
				int gl = input.get(x, y);
				int nv_pixel = ((255*(gl-minVal))/(maxVal-minVal));
				if (nv_pixel>255) {
				nv_pixel = 255;
			}
				else if  (nv_pixel<0){
					nv_pixel = 0;
				}
				input.set(x, y, nv_pixel);
			}
		}
	}


	public static void modifyContrastOptimized(GrayU8 input) {
		int largeur = input.getWidth();
		int hauteur = input.getHeight();
		int maxVal = Integer.MIN_VALUE; // Initialisé avec la valeur minimale pour int
		int minVal = Integer.MAX_VALUE; // Initialise avec la valeur maximale pour int
		
		// Itérer sur chaque pixel pour trouver les valeurs min et max
		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur; x++) {
				int pixelValue = input.get(x, y);
				if (pixelValue > maxVal) {
					maxVal = pixelValue; // Nouvelle valeur maximale trouvée
				}
				if (pixelValue < minVal) {
					minVal = pixelValue; // Nouvelle valeur minimale trouvée
				}
			}
		}
		
		int[] lookUpTableau = new int[256]; // 3 lignes et 4 colonnes

		//Implémentation du lookUpTableau
		for (int x = 0; x < 255; ++x) {
				int nv_pixel = ((255*(x-minVal))/(maxVal-minVal));
				if (nv_pixel>255) {
				nv_pixel = 255;
			}
				else if  (nv_pixel<0){
					nv_pixel = 0;
				}
				lookUpTableau[x] = nv_pixel;
			}
		//Parcours de tous les pixels pour leur appliquer le contraste
		for (int y = 0; y < input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
				int gl = input.get(x, y);
				int nv_pixel = lookUpTableau[gl];
				if (nv_pixel>255) {
				nv_pixel = 255;
			}
				else if  (nv_pixel<0){
					nv_pixel = 0;
				}
				input.set(x, y, nv_pixel);
			}
		}
	}

	public static void egalisationHistogramContrast(GrayU8 input) {
		//Initialisations histogramme
		int[] histogramTab = new int[256];
		for (int i = 0; i<255 ; i++){
			histogramTab[i] = 0;
		}
		//Calcul histogramme de l'image
		for (int y = 0; y < input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
				int gl = input.get(x, y);
				histogramTab[gl] ++;
			}
		}
		//Calcul histogramme CUMULE de l'image
		for (int i = 1; i<255 ; i++){
			histogramTab[i] += histogramTab[i-1];
		}
		
		// Trouver la valeur minimale non nulle dans l'histogramme cumulé
		int minVal = histogramTab[0];
		for (int i = 1; i < 256; i++) {
			if (histogramTab[i] != 0) {
				minVal = histogramTab[i];
				break;
			}
		}

		// Normalisation de l'histogramme cumulé (formule duc cours)
		int total = input.width * input.height;
		for (int i = 0; i < 256; i++) {
			histogramTab[i] = (histogramTab[i] - minVal) * 255 / (total - minVal);
		}

		// Le mettre à à toute l'image
		for (int y = 0; y < input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
				int gl = input.get(x, y);
				int nv_pixel = histogramTab[gl];
				if (nv_pixel>255) {
				nv_pixel = 255;
			}
				else if  (nv_pixel<0){
					nv_pixel = 0;
				}
				input.set(x, y, nv_pixel);
			}
		}
	}

    public static void main( String[] args ) {

    	// load image
		if (args.length < 2) {
			System.out.println("missing input or output image filename");
			System.exit(-1);
		}
		final String inputPath = args[0];
		GrayU8 input = UtilImageIO.loadImage(inputPath, GrayU8.class);
		if(input == null) {
			System.err.println("Cannot read input file '" + inputPath);
			System.exit(-1);
		}

		// décommenter une procédure pour pouvoir l'utiliser. (Par défaut, modiflumi(input, 100);)
		modiflumi(input, 100);
		//modifyContrastOptimized(input);
        //egalisationHistogramContrast(input);

		// pour l'output
		final String outputPath = args[1];
		UtilImageIO.saveImage(input, outputPath);
		System.out.println("Image saved in: " + outputPath);
	}

}