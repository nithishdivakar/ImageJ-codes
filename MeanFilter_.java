import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MeanFilter_ implements PlugInFilter{
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
		
		float kernel[][] = 
			{{1,1,1},
			{1,1,1},
			{1,1,1}};
		
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		ImagePlus imOut = IJ.createImage("Mean Filter","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		
		float img_array[][] = Conv.conv2d(ip.getFloatArray(),kernel,1,1,9.0f);
		ipProc.setFloatArray(img_array);
		
		imOut.show();
	}

	/*
	@Override
	public void run(ImageProcessor ip) {
		// TODO Auto-generated method stub
		int h = ip.getHeight();
		int w = ip.getWidth();
		ImagePlus imOut = IJ.createImage("Mean Filter", "8bit", w, h, 1);
		ImageProcessor ipOut = imOut.getProcessor();
		
		int filt_size = 5;
		int n = filt_size/2;
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				int pix_val=0;
				
				for (int i=-n;i<=n;i++){
					for (int j=-n;j<=n;j++){
						int xpos = i+x;
						int ypos = j+y;
						
						if (xpos<0)  xpos=-xpos;
						if (ypos<0)  ypos=-ypos;
						
						if (xpos>=w) xpos=2*w-xpos;
						if (ypos>=h) ypos=2*h-ypos;
						
						pix_val += ip.getPixel(xpos, ypos);
					}	
				}
				pix_val = pix_val/(4*n*n+4*n+1);
				ipOut.putPixel(x,y,pix_val);
			}
		}
		imOut.show();
	}
	*/

}
