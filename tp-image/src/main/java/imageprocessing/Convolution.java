package imageprocessing;

import boofcv.core.image.ConvertImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;


public class Convolution {

  public static void meanFilter(GrayU8 input, GrayU8 output, int size) {
    int largeur = input.width;
    int hauteur = input.height;
    
    int offset = size / 2;
    
    for (int y = offset; y < hauteur - offset; y++) {
        for (int x = offset; x < largeur - offset; x++) {
            int somme = 0;
            int nombrePixels = 0;
            
            for (int dy = -offset; dy <= offset; dy++) {
                for (int dx = -offset; dx <= offset; dx++) {
                    int pixelValue = input.get(x + dx, y + dy);
                    somme += pixelValue;
                    nombrePixels++;
                }
            }
            
            int moyenne = somme / nombrePixels;
            if (moyenne>255) {
              moyenne = 255;
            }
              else if  (moyenne<0){
                moyenne = 0;
              }
            output.set(x, y, moyenne);
        }
    }
  }

/*    */

  public static void convolution(GrayU8 input, GrayS16 output, int[][] kernel) {
    int largeur = input.width;
    int hauteur = input.height;
    int n = kernel.length / 2; 
    
    for (int y = 0; y < hauteur; y++) {
        for (int x = 0; x < largeur; x++) {
            int sommeConvolution = 0;
            
            
            for (int ky = -n; ky <= n; ky++) {
                for (int kx = -n; kx <= n; kx++) {
                    int px = x + kx;
                    int py = y + ky;
                    

                    if (px >= 0 && px < largeur && py >= 0 && py < hauteur) {
                        int valeurPixel = input.get(px, py);
                        sommeConvolution += valeurPixel * kernel[n + ky][n + kx];
                    }
                }
            }
            
            output.set(x, y, sommeConvolution);
        }
    }  }
    
    public static void gradientImage(GrayU8 input, GrayU8 output, int[][] kernelX, int[][] kernelY) {
        GrayS16 gradX = new GrayS16(input.width, input.height);
        GrayS16 gradY = new GrayS16(input.width, input.height);
        
        convolution(input, gradX, kernelX);
        convolution(input, gradY, kernelY);
        
        for (int y = 0; y < input.height; y++) {
            for (int x = 0; x < input.width; x++) {
                int gx = gradX.get(x, y);
                int gy = gradY.get(x, y);
                
                int gradientNorm = (int)Math.sqrt(gx * gx + gy * gy);
                
                gradientNorm = Math.min(255, Math.max(0, gradientNorm));
                
                output.set(x, y, gradientNorm);
            }
        }
    }
    

  public static void gradientImageSobel(GrayU8 input, GrayU8 output){
    int[][] kernelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    int[][] kernelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    gradientImage(input, output, kernelX, kernelY);
  }

  public static void gradientImagePrewitt(GrayU8 input, GrayU8 output) {
    int[][] kernelX = {
        {-1, 0, 1},
        {-1, 0, 1},
        {-1, 0, 1}
    };
    
    int[][] kernelY = {
        { 1,  1,  1},
        { 0,  0,  0},
        {-1, -1, -1}
    };
    
    GrayS16 gradX = new GrayS16(input.width, input.height);
    GrayS16 gradY = new GrayS16(input.width, input.height);
    
    convolution(input, gradX, kernelX);
    convolution(input, gradY, kernelY);
    
    for (int y = 0; y < input.height; y++) {
        for (int x = 0; x < input.width; x++) {
            int gx = gradX.get(x, y);
            int gy = gradY.get(x, y);
            int gradientNorm = (int)Math.sqrt(gx * gx + gy * gy);
            
            gradientNorm = Math.min(255, Math.max(0, gradientNorm));
            
            output.set(x, y, gradientNorm);
        }
    }
}



  public static void main(final String[] args) {
    // load image
    if (args.length < 2) {
      System.out.println("missing input or output image filename");
      System.exit(-1);
    }
    final String inputPath = args[0];
    GrayU8 input = UtilImageIO.loadImage(inputPath, GrayU8.class);
    GrayU8 output = input.createSameShape();

		// décommenter une procédure pour pouvoir l'utiliser. (Par défaut, gradientImagePrewitt(input, output);)
    //meanFilter(input, output, 11);
    //gradientImageSobel(input, output);
    gradientImagePrewitt(input, output);


    // pour l'output
    final String outputPath = args[1];
    UtilImageIO.saveImage(output, outputPath);
    System.out.println("Image saved in: " + outputPath);
  }

}
