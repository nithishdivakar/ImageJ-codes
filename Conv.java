public class Conv{

	public static float[][] conv2d(float[][] inp_img, float[][] kernel,  int ker_w, int ker_h) {
		return conv2d(inp_img, kernel, ker_w,ker_h,1.0F);
	}
	
	public static float[][] conv2d(float[][] inp_img, float[][] kernel,  int ker_w, int ker_h,float normalizer) {
			
		int h = inp_img.length;
		int w = inp_img[0].length;
		
		MirrorBoundaryMatrix out = new MirrorBoundaryMatrix(h,w);
		MirrorBoundaryMatrix inp = new MirrorBoundaryMatrix(inp_img);
		
		
		for(int x=0;x<h;x++)for(int y=0;y<w;y++){
			float pix_val=0.0f;
			
			for(int i=-ker_w;i<=ker_w;i++) for(int j=-ker_h;j<=ker_h;j++){
				pix_val += inp.getPixel(i+x, j+y)*kernel[i+ker_w][j+ker_h];
			}	
			
			out.putPixel(x, y, pix_val/normalizer); 
		}
		return out.getMatrix();
	}
}
