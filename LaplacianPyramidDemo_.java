import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class LaplacianPyramidDemo_ implements PlugInFilter{
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
		int LEVEL = 8;
		int h = ip.getHeight();
		int w = ip.getWidth();
		
		ImagePlus imOut = IJ.createImage("min filter","8bit", 2*w, 2*h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		float [][]I = ip.getFloatArray();
		
		double incr = 0.0;
		for(int level = 0;level<LEVEL;level++){
			float[][] D = blur(I,5.0,1);
			D = downsample(D);
			float[][] F = upsample(D);
			F = interpolate(F,5.0,1);
			float[][] G = diff(I,F);
			
			for(int i=0;i<I.length;i++){for(int j=0;j<I[0].length;j++){
				ipProc.putPixelValue(i+(int)(w*incr), j, I[i][j]);
				ipProc.putPixelValue(i+(int)(w*incr), j+h,G[i][j] );
			}}
			incr = incr + 1.0/(Math.pow(2.0,level));
			
			I = D;
			
		}
		imOut.show();
	}
	
	
	public static float[][] diff(float I[][],float J[][]){
		float dff[][]=new float[I.length][I[0].length];
		for(int i=0;i<J.length;i++)for(int j=0;j<J[0].length;j++){
			dff[i][j] = Math.abs(I[i][j]-J[i][j]);
		}
		return dff;
	}
	
	public static float[][] blur(float I[][],double sigma,int s){
		float kernel[][] = Kernels.GaussianKernal(s, sigma);
		float blured_img[][] = Conv.conv2d(I,kernel,s,s);
		return blured_img;
	}
	
	public static float[][] interpolate(float I[][],double sigma,int s){
		float kernel[][] = Kernels.GaussianKernal(s, sigma);
		
		MirrorBoundaryMatrix InImg = new MirrorBoundaryMatrix(I);
		MirrorBoundaryMatrix OuImg = new MirrorBoundaryMatrix(I.length,I[0].length);
		for (int x=0;x<I.length;x++){for(int y=0;y<I[0].length;y++){
			float pix_val=0.0f;
			float normalizer=0.0f;
			
			for (int i=-s;i<=s;i++){for (int j=-s;j<=s;j++){
				float pixel = InImg.getPixel(x+i, y+j);
				pix_val += pixel*kernel[i+s][j+s];
				if (pixel>0.0f){
					normalizer+=kernel[i+s][j+s];
				}
			}}
			OuImg.putPixel(x, y, pix_val/normalizer);
		}}
		return OuImg.getMatrix();
	}
	
	
	public static float[][] downsample(float I[][]){
		float down_samp[][]=new float[I.length/2][I[0].length/2];
		for(int i=0;i<I.length/2;i++){for(int j=0;j<I[0].length/2;j++){
			down_samp[i][j] = I[2*i][2*j];
		}}
		return down_samp;
	}

	public static float[][] upsample(float I[][]){
		float up_samp[][]=new float[I.length*2][I[0].length*2];
		for(int i=0;i<I.length;i++){ for(int j=0;j<I[0].length;j++){
			up_samp[2*i][2*j] = I[i][j];
		}}
		return up_samp;
	}
}