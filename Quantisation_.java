import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


import javax.swing.JOptionPane;

public class Quantisation_ implements PlugInFilter{
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
		String input = JOptionPane.showInputDialog("Please enter Number of Quantization Level" ) ;
		int level = Integer.parseInt(input) ;
		
		
		ImagePlus imOut = quantize(ip,level);
		imOut.show();
	}
	
	public static ImagePlus quantize(ImageProcessor ip, int quantisation_level) {
		
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		ImagePlus imOut = IJ.createImage("Quantised"+quantisation_level,"8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		
		
		for (int x=0;x<h;x++){
			for(int y=0;y<w;y++){
				int pixel = (int) ((256.0/quantisation_level)*Math.floor(ip.getPixel(x, y)/(256/quantisation_level)));
				
				
				ipProc.putPixel(x, y, pixel);
			}
		}
		return imOut;
	}
}
