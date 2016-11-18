import java.util.Arrays;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MinFilter_ implements PlugInFilter{
	ImagePlus inImg;
	String title = null;
	@Override
	public int setup(String arg, ImagePlus imp) {
		if(imp == null){
			IJ.error("No input image");
		}
		else{
			this.inImg = imp;
		}
		return DOES_ALL + NO_CHANGES;
	}

	@Override
	public void run(ImageProcessor ip) {
		ImagePlus imOut =min_fiter(ip);
		imOut.show();
	}
	
	public static ImagePlus min_fiter(ImageProcessor ip) {
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		ImagePlus imOut = IJ.createImage("min filter","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		
		int filt_size = 5;
		int n = filt_size/2;
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				int count =0;
				int pixels[]=new int[4*n*n+4*n+1];
				for (int i=-n;i<=n;i++){
					for (int j=-n;j<=n;j++){
						int xpos = i+x;
						int ypos = j+y;
						
						if (xpos<0)  xpos=-xpos;
						if (ypos<0)  ypos=-ypos;
						
						if (xpos>=w) xpos=2*w-xpos;
						if (ypos>=h) ypos=2*h-ypos;
						
						pixels[count] = ip.getPixel(xpos, ypos);
						count++;
					}	
				}
				Arrays.sort(pixels);
				ipProc.putPixel(x,y,pixels[0]);
			}
		}
		return imOut;
	}
}
