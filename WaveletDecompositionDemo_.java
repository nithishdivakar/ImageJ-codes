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
		
		float [][][]T = wavelet_decompose(I);
		float [][]  K = wavelet_recompose(T);
		
		float [][]J = new float[I.length][I[0].length];
		

		copy(T[0],J,0,0);
		copy(T[1],J,T[1].length,0);
		copy(T[2],J,0,T[2][0].length);
		copy(T[3],J,T[3].length,T[3][0].length);
		
		ImagePlus imOut1 = IJ.createImage("wavelet Demo2","8bit", w, h, 1);
		ImageProcessor ipProc1 = imOut1.getProcessor();
		ipProc1.setFloatArray(abs(J));
		imOut1.show();
		
		ImagePlus imOut2 = IJ.createImage("wavelet Demo2","8bit", w, h, 1);
		ImageProcessor ipProc2 = imOut2.getProcessor();
		ipProc2.setFloatArray(K);
		imOut2.show();
	}
	/*
	public void run2(ImageProcessor ip) {
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
		
		float[][] K = wavelet_recompose(wavelet_decompose(I));
		ImagePlus imOut2 = IJ.createImage("wavelet Demo2","8bit", w, h, 1);
		ImageProcessor ipProc2 = imOut2.getProcessor();
		ipProc2.setFloatArray(abs(K));
		imOut2.show();
	}
	*/
	
	float[][][] wavelet_decompose(float I[][]){
		float sqrt2 = 2.0f;//(float) Math.sqrt(2.0);
		float [][]ker_L_x = {{0.0f , 1.0f/sqrt2 , 1.0f/sqrt2}};
		float [][]ker_L_y = {{0.0f},{1.0f/sqrt2},{1.0f/sqrt2}};
		float [][]ker_H_x = {{0.0f , 1.0f/sqrt2 , -1.0f/sqrt2}};
		float [][]ker_H_y = {{0.0f},{1.0f/sqrt2},{-1.0f/sqrt2}};

		//for(int i=0;i<10;i++) System.out.print(I[0][i]+" "); System.out.println();
		float [][]L  = filter(I,ker_L_x,0,1);
		//for(int i=0;i<10;i++) System.out.print(L[0][i]+" "); System.out.println();
		L = downsample_y(L) ; 
		//for(int i=0;i<10;i++) System.out.print(L[0][i]+" "); System.out.println();
				
		//for(int i=0;i<10;i++) System.out.print(I[0][i]+" "); System.out.println();
		float [][]H  = filter(I,ker_H_x,0,1);
		//for(int i=0;i<10;i++) System.out.print(H[0][i]+" "); System.out.println();
		H = downsample_y(H) ;
		//for(int i=0;i<10;i++) System.out.print(H[0][i]+" "); System.out.println();
				
		
		
		//float [][]L  = filter(I,ker_L_x,0,1);  L = downsample_y(L) ;
		//float [][]H  = filter(I,ker_H_x,0,1);  H = downsample_y(H) ;
		
		float [][]LL = filter(L,ker_L_y,1,0); LL = downsample_x(LL);
		float [][]HL = filter(L,ker_H_y,1,0); HL = downsample_x(HL);
		float [][]LH = filter(H,ker_L_y,1,0); LH = downsample_x(LH);
		float [][]HH = filter(H,ker_H_y,1,0); HH = downsample_x(HH);
		
		
		float [][][]W = new float[4][I.length/2][I[0].length/2];
		copy(LL,W[0],0,0);
		copy(HL,W[1],0,0);
		copy(LH,W[2],0,0);
		copy(HH,W[3],0,0);
		return W;
	}
	
	float[][] wavelet_recompose(float[][][]W){
		float [][]LL = W[0];
		float [][]HL = W[1];
		float [][]LH = W[2];
		float [][]HH = W[3];
		
		float sqrt2 = 1.0f;//(float) Math.sqrt(2.0);
		float [][]ker_L_x = {{1.0f/sqrt2 , 1.0f/sqrt2  , 0}};
		float [][]ker_L_y = {{1.0f/sqrt2},{1.0f/sqrt2} ,{0}};
		float [][]ker_H_x = {{1.0f/sqrt2 ,-1.0f/sqrt2  , 0}};
		float [][]ker_H_y = {{1.0f/sqrt2},{-1.0f/sqrt2},{0}};
		
		

		//for(int i=0;i<10;i++) System.out.print(LL[i][0]+" "); System.out.println();
		LL = upsample_x(LL); 
		//for(int i=0;i<10;i++) System.out.print(LL[i][0]+" "); System.out.println();
		LL = filter(LL,ker_L_y,1,0); 
		//for(int i=0;i<10;i++) System.out.print(LL[i][0]+" "); System.out.println();
		
		

		//for(int i=0;i<10;i++) System.out.print(HL[i][0]+" "); System.out.println();
		HL = upsample_x(HL); 
		//for(int i=0;i<10;i++) System.out.print(HL[i][0]+" "); System.out.println();
		HL = filter(HL,ker_H_y,1,0); 
		//for(int i=0;i<10;i++) System.out.print(HL[i][0]+" "); System.out.println();
		
		
		//for(int i=0;i<10;i++) System.out.print(LH[i][0]+" "); System.out.println();
		LH = upsample_x(LH); 
		//for(int i=0;i<10;i++) System.out.print(LH[i][0]+" "); System.out.println();
		LH = filter(LH,ker_L_y,1,0); 
		//for(int i=0;i<10;i++) System.out.print(LH[i][0]+" "); System.out.println();
		
		//for(int i=0;i<10;i++) System.out.print(HH[i][0]+" "); System.out.println();
		HH = upsample_x(HH); 
		//for(int i=0;i<10;i++) System.out.print(HH[i][0]+" "); System.out.println();
		HH = filter(HH,ker_H_y,1,0); 
		//for(int i=0;i<10;i++) System.out.print(HH[i][0]+" "); System.out.println();

		
		
		
		
		float[][] L = add(LL,HL);
		float[][] H = add(LH,HH);
		
		//for(int i=0;i<10;i++) System.out.print(L[0][i]+" "); System.out.println();
		L = upsample_y(L); 
		//for(int i=0;i<10;i++) System.out.print(L[0][i]+" "); System.out.println();
		L = filter(L,ker_L_x,0,1); 
		//for(int i=0;i<10;i++) System.out.print(L[0][i]+" "); System.out.println();
		
		//for(int i=0;i<10;i++) System.out.print(H[0][i]+" "); System.out.println();
		H = upsample_y(H); 
		//for(int i=0;i<10;i++) System.out.print(H[0][i]+" "); System.out.println();
		H = filter(H,ker_H_x,0,1); 
		//for(int i=0;i<10;i++) System.out.print(H[0][i]+" "); System.out.println();
	
		
		
		float [][]I = add(L,H);
		//for(int i=0;i<10;i++) System.out.print(L[0][i]+" "); System.out.println();
		//for(int i=0;i<10;i++) System.out.print(H[0][i]+" "); System.out.println();
		//for(int i=0;i<10;i++) System.out.print(I[0][i]+" "); System.out.println();
		
		
		//*/
		//float [][] I = new float[256][256];
		//copy(abs(HL),I,0,0);
		//copy(HL2,I,0,128);
		return I;
	}
	
	void copy(float [][]src,float [][]des, int x, int y){
		for(int i=0;i<src.length;i++){for(int j=0;j<src[0].length;j++){
			des[i+x][j+y] = src[i][j];
		}}
	}
	
	float[][] add(float[][]A, float[][]B){
		float[][]C = new float[A.length][A[0].length];
		for(int i=0;i<A.length;i++){for(int j=0;j<A[0].length;j++){
			C[i][j] = A[i][j]+B[i][j];
		}}
		return C;
	}
	
	
	float[][] filter(float[][] I, float [][]ker, int ker_h, int ker_w){
		float [][] Ix = Conv.conv2d(I, ker, ker_h, ker_w);
		for(int i=0;i<Ix.length;i++){for(int j=0;j<Ix[0].length;j++){
			//Ix[i][j] = Math.abs(Ix[i][j]);
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
	float[][] upsample_x(float[][]I){
		float [][]Ix = new float[2*I.length][I[0].length];
		for(int i=0;i<I.length;i++){for(int j=0;j<I[0].length;j++){
			Ix[2*i][j]= I[i][j];
			Ix[2*i+1][j]= 0.0f;
		}}
		return Ix;
	}
	float[][] upsample_y(float[][]I){
		float [][]Iy = new float[I.length][2*I[0].length];
		for(int i=0;i<I.length;i++){for(int j=0;j<I[0].length;j++){
			Iy[i][2*j]= I[i][j];
			Iy[i][2*j+1] = 0.0f;
		}}
		return Iy;
	}
	
	float[][] abs(float [][]A){
		float [][]B = new float[A.length][A[0].length];
		for(int i=0;i<A.length;i++){for(int j=0;j<A[0].length;j++){
			B[i][j]= Math.abs(A[i][j]);
		}}
		return B;
	}
}
