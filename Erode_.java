import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Erode_ implements PlugInFilter{
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
		// TODO Auto-generated method stub
		int h = ip.getHeight();
		int w = ip.getWidth();
		ImagePlus imOut = IJ.createImage("Erosion", "8bit", w, h, 1);
		ImageProcessor ipOut = imOut.getProcessor();
		
		
		int filt_size = 3;
		int n = filt_size/2;
		int kernel[][]=new int[2*n+1][2*n+1];
		
		// erosion kernel
		for(int k = 0;k<(2*n+1);k++)
			for(int l = 0;l<(2*n+1);l++)
				kernel [k][l]=1;
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				boolean erode =false;
				for (int i=-n;i<=n;i++){
					for (int j=-n;j<=n;j++){
						int xpos = i+x;
						int ypos = j+y;
						
						if (xpos<0)  xpos=-xpos;
						if (ypos<0)  ypos=-ypos;
						
						if (xpos>=w) xpos=2*w-xpos;
						if (ypos>=h) ypos=2*h-ypos;
						
						int pix_val = ip.getPixel(xpos, ypos);
						if (pix_val > 127 && kernel[i+n][j+n]==1)
							// white is black
							erode = true; // kernel is not included in image
					}	
				}
				if(erode)
					ipOut.putPixel(x, y, 255);
				else
					ipOut.putPixel(x, y, 0);
			}
		}
		imOut.show();
	}

}
