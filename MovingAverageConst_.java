import javax.swing.JOptionPane;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MovingAverageConst_ implements PlugInFilter{
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
	public double pix(double[][] A, int xpos,int ypos,int h,int w){
		if (xpos<0)  xpos=-xpos;
		if (ypos<0)  ypos=-ypos;
		
		if (xpos>=h-1) xpos=2*h-xpos-2;
		if (ypos>=w-1) ypos=2*w-ypos-2;
		return A[xpos][ypos];
	}
	@Override
	public void run(ImageProcessor ip) {
		
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		String input = JOptionPane.showInputDialog("Please filter size" ) ;
		int L= Integer.parseInt(input) ;
		
		ImagePlus       imOut = IJ.createImage("Moving Average","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		
		double img_array[][] = new double[w][h];
		double img_array2[][] = new double[w][h];
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				img_array[x][y] = ip.getPixel(x, y);
			}
		}
		
		for (int x=0;x<w;x++){
			int y=0;
			double pix_val=0.0;
			for (int i=-L;i<=L;i++){
				for (int j=-L;j<=L;j++){
					int xpos = i+x;
					int ypos = j+y;
					
					if (xpos<0)  xpos=-xpos;
					if (ypos<0)  ypos=-ypos;
					
					if (xpos>=h-1) xpos=2*h-xpos-2;
					if (ypos>=w-1) ypos=2*w-ypos-2;
					
					pix_val += ip.getPixel(xpos, ypos);
				}	
			}
			img_array[x][y] = pix_val/(4.0*L*L+4.0*L+1.0);
			img_array2[x][y] = pix_val/(4.0*L*L+4.0*L+1.0);
		}
		for (int y=0;y<h;y++){
			//for (int x=0;x<w;x++){
			int x=0;
			double pix_val=0.0;
			for (int i=-L;i<=L;i++){
				for (int j=-L;j<=L;j++){
					int xpos = i+x;
					int ypos = j+y;
					
					if (xpos<0)  xpos=-xpos;
					if (ypos<0)  ypos=-ypos;
					
					if (xpos>=h-1) xpos=2*h-xpos-2;
					if (ypos>=w-1) ypos=2*w-ypos-2;
					
					pix_val += ip.getPixel(xpos, ypos);
				}	
			}
			img_array[x][y] = pix_val/(4.0*L*L+4.0*L+1.0);
			img_array2[x][y] = pix_val/(4.0*L*L+4.0*L+1.0);
		}//}
		

		for (int x=1;x<w;x++){
		for (int y=1;y<h;y++){
			double temp = 
					+pix(img_array,x+L  ,y+L  ,h,w)
					-pix(img_array,x-L-1,y+L  ,h,w)
					-pix(img_array,x+L  ,y-L-1,h,w)
					+pix(img_array,x-L-1,y-L-1,h,w)
					;
			
			double pix_val=
					+pix(img_array2,x  ,y-1,h,w)
					+pix(img_array2,x-1,y  ,h,w)
					-pix(img_array2,x-1,y-1,h,w)
					+temp/(4.0*L*L+4.0*L+1.0);

			img_array2[x][y] = pix_val;
		}}
		
		for (int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				ipProc.putPixel(x, y, (int)img_array2[x][y]);
			}
		}
		imOut.show();
	}
}
