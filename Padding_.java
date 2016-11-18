import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Padding_ implements PlugInFilter{
	ImagePlus inImg;
	String title = null;
	public int setup(String args, ImagePlus inImg) {
		if(inImg == null){
			IJ.error("No input image");
		}
		else{
			this.inImg = inImg;
		}
		return DOES_ALL + NO_CHANGES;		
	}	
	
	public void run(ImageProcessor ipIn){
		
		int choice = 3;
		switch (choice) {
		case 1 :
				zero_pad_(ipIn);
				break;
		case 2 :
				periodic_pad_(ipIn);
				break;
		case 3 :
				mirror_pad_(ipIn);
				break;
		default :
				zero_pad_(ipIn);
				break;
		}
	}

	public void zero_pad_ (ImageProcessor ipIn) {
		int h = ipIn.getHeight();
		int w = ipIn.getWidth();
		
		int H = 3*h;
		int W = 3*w;
		
		ImagePlus imOut = IJ.createImage("Zero-padding", "8bit", W, H, 1);
		ImageProcessor ipOut = imOut.getProcessor();
		
		for (int i=0; i<W; i++){
			for (int j=0; j<H; j++){
				if(i < w || j < h || i > 2*w || j > 2*h){
					ipOut.putPixel(i, j, 0);
				}
				else {
					int val = ipIn.getPixel(i-w, j-h);
					ipOut.putPixel(i, j, val);
				}
			}
		}
		
		imOut.show();			
	}

	public void periodic_pad_ (ImageProcessor ipIn) {
		int h = ipIn.getHeight();
		int w = ipIn.getWidth();
		
		int H = 3*h;
		int W = 3*w;
		
		int k = 0;
		int l = 0;
		
		ImagePlus imOut = IJ.createImage("Periodic-padding", "8bit", W, H, 1);
		ImageProcessor ipOut = imOut.getProcessor();
		
		for (int i=0; i<W; i++){
			for (int j=0; j<H; j++){
				
					int val = ipIn.getPixel(k, l);
					ipOut.putPixel(i, j, val);
//				}
				l = l + 1;
				if (l == h)
					l = 0;
			}
			k = k + 1;
			if (k == w)
				k = 0;
		}
		
		imOut.show();			
	}
	public ImageProcessor mirror_pad_ (ImageProcessor ipIn) {
		int h = ipIn.getHeight();
		int w = ipIn.getWidth();
		
		int H = h + (4*h);
		int W = w + (4*w);
		
		int K = (W - w)/2;
		int L = (H - h)/2;
		
		int k = 0;
		int l = 0;

		int m = K;
		int n = L;
		
		ImagePlus imOut = IJ.createImage("Mirror-padding", "8bit", W, H, 1);
		ImageProcessor ipOut = imOut.getProcessor();
		
		for (int i=0; i<w; i++){
			for (int j=0; j<h; j++){
				
					int value = ipIn.getPixel(i, j);
					for (k = 0; k < 10; k=k+2){
						if (m-(k*w)+i < 0 && m+(k*w)-i > m+w+K){
							break;
						}
						for (l = 0; l < 10; l=l+2){
							if (n-(l*h)+j < 0 && n+(l*h)-j > n+h+L){
								break;
							}
							else {
								ipOut.putPixel(m+(k*w)-i, (n+(l*h)-j), value); 
								ipOut.putPixel(m+(k*w)+i, (n+(l*h)-j), value);
								ipOut.putPixel(m+(k*w)-i, (n+(l*h)+j), value);
								ipOut.putPixel(m+(k*w)+i, (n+(l*h)+j), value);
								ipOut.putPixel(m+(k*w)-i, (n-(l*h)-j), value);
								ipOut.putPixel(m+(k*w)+i, (n-(l*h)-j), value);
								ipOut.putPixel(m+(k*w)-i, (n-(l*h)+j), value);
								ipOut.putPixel(m+(k*w)+i, (n-(l*h)+j), value);
								ipOut.putPixel(m-(k*w)-i, (n+(l*h)-j), value);
								ipOut.putPixel(m-(k*w)+i, (n+(l*h)-j), value);
								ipOut.putPixel(m-(k*w)-i, (n+(l*h)+j), value);
								ipOut.putPixel(m-(k*w)+i, (n+(l*h)+j), value);
								ipOut.putPixel(m-(k*w)-i, (n-(l*h)-j), value);
								ipOut.putPixel(m-(k*w)+i, (n-(l*h)-j), value);
								ipOut.putPixel(m-(k*w)-i, (n-(l*h)+j), value);
								ipOut.putPixel(m-(k*w)+i, (n-(l*h)+j), value);
							}						
						}	
					}
			}
		
			imOut.show();			
		}
		return ipOut;
	}
}