import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class LaplacianFilter_ implements PlugInFilter{
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
			{{0,-1,0},
			{-1,4,-1},
			{0,-1,0}};
		
		
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		ImagePlus       imOut = IJ.createImage("Laplacian Filter","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		
		float img_array[][] = Conv.conv2d(ip.getFloatArray(),kernel,1,1);
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				img_array[x][y] = Math.abs(img_array[x][y]);
			}
		}
	
		ipProc.setFloatArray(img_array);
		imOut.show();
	}
}
