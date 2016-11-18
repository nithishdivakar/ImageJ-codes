import ij.process.ImageProcessor;


public class FFT2d{
	
	
	
	private static float[][] pad_to_power_of_two(float[][] A){
		int x,y,i,j;
		x=A.length;
		y=A[0].length;
		int S=0;
		if (x>y){
			S=min_power_two(x);
		}else{
			S=min_power_two(y);
		}
		float [][] P = new float[S][S];
		for(i=0;i<S;i++)
			for(j=0;j<S;j++)
				P[i][j]=0.0f;
		
		for(i=0;i<x;i++)
			for(j=0;j<y;j++)
				P[i][j]=A[i][j];
		return P;
	}
	
	private static int min_power_two(int x){
		
		int i=1;
		do{
			i = 2*i;
		}while(i<x);
		return i;
	}
	
	public static Complex[][] fft2d_image(ImageProcessor ip) {
		Complex F[][]= fft2d(pad_to_power_of_two(ip.getFloatArray()));
		return F;
	}
	
	public static Complex[][] fft2d(float I[][]) {
		
		int S = I.length;
		int i,j;
		
		Complex C[][] = new Complex[S][S];
		
		for(i=0;i<S;i++)for( j=0;j<S;j++){
			C[j][i]=new Complex(I[j][i],0.0);
		}

		//x pass
		for(j = 0;j<S;j++){
			//System.out.println(j);
			C[j] = FFT.fft(C[j]);
		}
		//transpose
		for(i=0;i<S;i++)for( j=i;j<S;j++){
			Complex temp = C[j][i];
			C[j][i] = C[i][j];
			C[i][j]=temp;
		}
		//y pass
		for(j = 0;j<S;j++){
			//System.out.println(j);
			C[j] = FFT.fft(C[j]);
		}
		return C;
	}
	public static Complex[][] ifft2d(Complex I[][]) {
		
		int w = I.length;
		int h = I[0].length;
		
		int i,j;
		int W = min_power_two(w);
		int H = min_power_two(h);
		int S = (W>H)?W:H;
		
		Complex C[][] = new Complex[S][S];
		
		for(i=0;i<S;i++)for( j=0;j<S;j++){
			Complex val=new Complex(0.0f,0.0f);
			if(i<h && j< w) val = I[j][i];
			C[j][i]=val;
		}

		//transpose
				for(i=0;i<S;i++)for( j=i;j<S;j++){
					Complex temp = C[j][i];
					C[j][i] = C[i][j];
					C[i][j]=temp;
				}
		//x pass
		for(j = 0;j<S;j++){
			//System.out.println(j);
			C[j] = FFT.ifft(C[j]);
		}
		//transpose
		for(i=0;i<S;i++)for( j=i;j<S;j++){
			Complex temp = C[j][i];
			C[j][i] = C[i][j];
			C[i][j]=temp;
		}
		//y pass
		for(j = 0;j<S;j++){
			//System.out.println(j);
			C[j] = FFT.ifft(C[j]);
		}
		return C;
	}
	
	public static float[][] fft2mag(Complex I[][]) {
		float [][]mag = new float[I.length][I[0].length];
		for(int i=0;i<I.length;i++)for(int j=0;j<I[0].length;j++){
			mag[i][j] = (float) I[i][j].abs();
		}
		return mag;
	}
	public static float[][] fft2phase(Complex I[][]) {
		float [][]phase = new float[I.length][I[0].length];
		for(int i=0;i<I.length;i++)for(int j=0;j<I[0].length;j++){
			phase[i][j] = (float) I[i][j].phase();
		}
		return phase;
	}
}
