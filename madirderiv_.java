import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class madirderiv_ implements PlugInFilter{
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

		float kernel[][] = 
			{{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
			{1,1,1,1,1}};
		int h = ip.getHeight();
		int w = ip.getWidth();

		ImagePlus imOut = IJ.createImage("exp","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		ImagePlus imOut2 = IJ.createImage("2","8bit", w, h, 1);
		ImageProcessor ipProc2 = imOut2.getProcessor();
		ImagePlus imOut3 = IJ.createImage("3","8bit", w, h, 1);
		ImageProcessor ipProc3 = imOut3.getProcessor();
		
		float [][] I = Conv.conv2d(ip.getFloatArray(),kernel,2,2,25.0f);

		float [][]Ix = der_X(I);
		float [][]Iy = der_Y(I);
		float [][][]J = structureTensor(Ix,Iy);
		float [][]D = dirderiv(J,Ix,Iy);
		
		ipProc.setFloatArray(scale(D)); imOut.show();
		ipProc2.setFloatArray(scale(Ix)); imOut2.show();
		ipProc3.setFloatArray(scale(Iy)); imOut3.show();
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
	
	public float[][] der_Y(float[][] I){
		float kernel[][] = {{1,-1, 0}};
		
		float[][]Ix  = Conv.conv2d(I,kernel,0,1);
		return Ix;
	}
	public float[][] der_X(float[][] I){
		float kernel[][] = {{1},{-1}, {0}};
		
		float[][]Ix  = Conv.conv2d(I,kernel,1,0);
		return Ix;
	}
	
	public float[][][] structureTensor(float[][]Ix, float[][]Iy){
		float[][][]J = new float[Ix.length][Ix[0].length][4];
		for(int x=0;x<Ix.length;x++)for(int y=0;y<Ix[0].length;y++){
			float fx = Ix[x][y];
			float fy = Iy[x][y];
			
			J[x][y][0] = fx*fx;
			J[x][y][1] = fx*fy;
			J[x][y][2] = fy*fx;
			J[x][y][3] = fy*fy;
		}	
		return J;
	}
	
	public float[][] dirderiv(float[][][]J, float[][]Ix, float[][]Iy){
		float[][]D = new float[Ix.length][Ix[0].length];
		
		for(int x=0;x<Ix.length;x++)for(int y=0;y<Ix[0].length;y++){
			double q =  0.5* Math.atan2(2.0*J[x][y][1],J[x][y][0]-J[x][y][3]);

			double cosq = Math.cos(q);
			double sinq = Math.sin(q);
			
			
			double ix = Ix[x][y]*cosq;
			double iy = Iy[x][y]*sinq;

			D[x][y] = (float) Math.sqrt(ix*ix+iy*iy);
		}
		return D;
	}
	
}
