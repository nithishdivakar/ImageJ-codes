import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class WaveletDecompositionDemo_ implements PlugInFilter{
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
		int LEVELS = 3;
		int h = ip.getHeight();
		int w = ip.getWidth();
		ImagePlus imOut = IJ.createImage("wavelet Demo","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		float [][]I = ip.getFloatArray();
		float [][]O = new float[I.length][I[0].length];
		float [][]Q = new float[I.length][I[0].length];
		
		copy(I,Q,0,0);
		for (int l=0;l<LEVELS;l++){
			float [][][]T = wavelet_decompose(Q);
			copy(T[0],O,0,0);
			copy(T[1],O,T[1].length,0);
			copy(T[2],O,0,T[2][0].length);
			copy(T[3],O,Q.length/2,Q[0].length/2);
			Q = new float[Q.length/2][Q[0].length/2];
			for(int i=0;i<Q.length;i++){for(int j=0;j<Q[0].length;j++){
				Q[i][j] = O[i][j];
			}}
		}

		ipProc.setFloatArray(O);
		imOut.show();
	}
	
	
	float[][][] wavelet_decompose(float I[][]){
		float [][]ker_L_x = {{1/2.0f,1/2.0f,0}};
		float [][]ker_L_y = {{1/2.0f},{1/2.0f},{0}};
		float [][]ker_H_x = {{1.0f/1.414f,-1.0f/1.414f,0.0f}};
		float [][]ker_H_y = {{1.0f/1.414f},{-1.0f/1.414f},{0}};

		float [][]L  = filter(I,ker_L_x,0,1);  L = downsample_x(L) ;
		float [][]H  = filter(I,ker_H_x,0,1);  H = downsample_x(H) ;
		float [][]LL = filter(L,ker_L_y,1,0); LL = downsample_y(LL);
		float [][]HL = filter(L,ker_H_y,1,0); HL = downsample_y(HL);
		float [][]LH = filter(H,ker_L_y,1,0); LH = downsample_y(LH);
		float [][]HH = filter(H,ker_H_y,1,0); HH = downsample_y(HH);
		
		
		float [][][]Img = new float[4][I.length/2][I[0].length/2];
		copy(LL,Img[0],0,0);
		copy(HL,Img[1],0,0);
		copy(LH,Img[2],0,0);
		copy(HH,Img[3],0,0);
		return Img;
	}
	
	void copy(float [][]src,float [][]des, int x, int y){
		for(int i=0;i<src.length;i++){for(int j=0;j<src[0].length;j++){
			des[i+x][j+y] = src[i][j];
		}}
	}
	
	
	float[][] filter(float[][] I, float [][]ker, int ker_h, int ker_w){
		float [][] Ix = Conv.conv2d(I, ker, ker_h, ker_w);
		for(int i=0;i<Ix.length;i++){for(int j=0;j<Ix[0].length;j++){
			Ix[i][j] = Math.abs(Ix[i][j]);
		}}
		return Ix;
	}
	
	float[][] downsample_x(float[][]I){
		float [][]Ix = new float[I.length/2][I[0].length];
		for(int i=0;i<Ix.length;i++){for(int j=0;j<Ix[0].length;j++){
			Ix[i][j]= I[2*i][j];
		}}
		return Ix;
	}
	float[][] downsample_y(float[][]I){
		float [][]Iy = new float[I.length][I[0].length/2];
		for(int i=0;i<Iy.length;i++){for(int j=0;j<Iy[0].length;j++){
			Iy[i][j]= I[i][2*j];
		}}
		return Iy;
	}
}
