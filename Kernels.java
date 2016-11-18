
public class Kernels {	
	public static float[][] GaussianKernal(int s, double sigma){
		float [][]K = new float[2*s+1][2*s+1];
		double deno = 2.0*Math.PI*sigma;
		
		for(int x=-s;x<=s;x++)for(int y=-s;y<=s;y++){
			double expo = -0.5*(x*x+y*y)/(sigma*sigma);
			K[x+s][y+s] =  (float) (Math.pow(Math.E, expo)/deno);
		}
		float sum=0.0f;
		for(int x=-s;x<=s;x++)for(int y=-s;y<=s;y++){
			sum+=K[x+s][y+s];
		}
		for(int x=-s;x<=s;x++)for(int y=-s;y<=s;y++){
			K[x+s][y+s] /= sum;
		}
		return K;
	}
	
	public static float[][] BoxKernel(int s){
		float [][]K = new float[2*s+1][2*s+1];
		float val = 1.0f/(2.0f*s+1.0f)*(2.0f*s+1.0f);
		for(int x=-s;x<=s;x++)for(int y=-s;y<=s;y++){
			K[x+s][y+s] =  val;
		}
		return K;
	}
	
}
