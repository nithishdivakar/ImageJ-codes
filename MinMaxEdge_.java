
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MinMaxEdge_ implements PlugInFilter{
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
		ImagePlus imOut =min_max_edge(ip);
		imOut.show();
	}
	public static ImagePlus min_max_edge(ImageProcessor ip) {
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		
		ImagePlus imOut = IJ.createImage("min -max edge","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();

		ImagePlus imMin= MinFilter_.min_fiter(ip);
		ImagePlus imMax= MaxFilter_.max_fiter(ip);

		ImageProcessor ipMin = imMin.getProcessor();
		ImageProcessor ipMax = imMax.getProcessor();
		
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				int min,max;
				min = ipMin.getPixel(x, y);
				max = ipMax.getPixel(x, y);
				ipProc.putPixel(x,y,max-min);
			}
		}
		return imOut;
	}
}
