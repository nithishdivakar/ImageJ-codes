import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.lang.reflect.Array;
import java.util.Arrays;

public class meanvar_ implements PlugInFilter{
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
			{{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
			{1,1,1,1,1}};
		
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		ImagePlus imOutm = IJ.createImage("Mean","8bit", w, h, 1);
		ImageProcessor ipProcm = imOutm.getProcessor();
		
		ImagePlus imOutv = IJ.createImage("Var","8bit", w, h, 1);
		ImageProcessor ipProcv = imOutv.getProcessor();

		float[][] M = new float[h][w];
		float[][] V = new float[h][w];
		

		//M = MeanFilter_.mean(ip.getFloatArray(),2,2);
		V = var(ip.getFloatArray(),2,2);
		
		M = Conv.conv2d(ip.getFloatArray(),kernel,2,2,25.0f);
		
		V = scale(V);
		
		ipProcm.setFloatArray(M);
		ipProcv.setFloatArray(V);
		
		
		
		imOutm.show();	
		imOutv.show();
	}

	public float[][] scale(float[][] I){
		float min = I[0][0];
		float max = I[0][0];
		for(int x=0;x<I.length;x++)for(int y=0;y<I[0].length;y++){
			if(I[x][y]<min) min = I[x][y];
			if(I[x][y]>max) max = I[x][y];
		}
		for(int x=0;x<I.length;x++)for(int y=0;y<I[0].length;y++){
			I[x][y] = 255.0f*(I[x][y]-min)/(max-min);
		}
		return I;
	}
	
	public float[][] var(float[][] inp_img,int ker_h,int ker_w){
		int h = inp_img.length;
		int w = inp_img[0].length;
		
		MirrorBoundaryMatrix V = new MirrorBoundaryMatrix(h,w);
		MirrorBoundaryMatrix inp = new MirrorBoundaryMatrix(inp_img);
		
		
		
		for(int x=0;x<h;x++)for(int y=0;y<w;y++){
			int count = 0;
			float []pixels = new float[(2*ker_w+1)*(2*ker_h+1)];
			
			for(int i=-ker_w;i<=ker_w;i++) for(int j=-ker_h;j<=ker_h;j++){
				pixels[count]= inp.getPixel(i+x, j+y);
				count = count+1;
			}	
			float mean = 0.0f;
			for(int i=0;i<count;i++){
				mean += pixels[i];
			}
			mean = mean/count;
			float var = 0.0f;
			for(int i=0;i<count;i++){
				float t = (pixels[i]-mean);
				var += t*t;
			}
			var = var/count;
			V.putPixel(x, y, var); 
		}
		return V.getMatrix();
	}
}
