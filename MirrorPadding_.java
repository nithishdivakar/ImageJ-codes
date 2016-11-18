import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MirrorPadding_ implements PlugInFilter{
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
		
				mirror_pad_(ipIn);
		
	}
	
	public int mirror_pixel(ImageProcessor ip,int x,int y){
		int h = ip.getHeight();
		int w = ip.getWidth();
		int xpos;
		int ypos;
		xpos = x %(2*h);
		ypos = y %(2*w);

		if (xpos<0)  xpos=-xpos;
		if (ypos<0)  ypos=-ypos;
		
		if (xpos>=h-1) xpos=2*h-xpos-2;
		if (ypos>=w-1) ypos=2*w-ypos-2;

		return ip.getPixel(xpos, ypos);
	}
	
	public ImageProcessor mirror_pad_ (ImageProcessor ip) {
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		int H = h + (4*h);
		int W = w + (4*w);
		
		int K = (W - w)/2;
		int L = (H - h)/2;
		ImagePlus imOut = IJ.createImage("Mirror-padding", "8bit", 5*w, 5*h, 1);
		ImageProcessor ipOut = imOut.getProcessor();
		
		
		for(int i=0;i<H;i++){
			for(int j=0;j<W;j++){
				int xpos =i;//-2*h; 
				int ypos =j;//-2*w;
				int pixel = mirror_pixel(ip,xpos,ypos);
				ipOut.putPixel(xpos, ypos, pixel);
			}
		}
				
		imOut.show();			
		return ipOut;
	}
}