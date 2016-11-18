
public class MirrorBoundaryMatrix {

	private float Img[][];
	private int W,H;
	
	public MirrorBoundaryMatrix(float[][] I){
		Img = I;
		W   = I.length;
		H   = I[0].length;
	}
	public MirrorBoundaryMatrix(int w, int h){
		Img = new float[w][h];
		W   = w;
		H   = h;
	}
	
	int xcord(int x){
		if (x<0)  x=-x;
		if (x>=W-1) x=2*W-x-2;
		return x;
	}
	int ycord(int y){
		if (y<0)  y=-y;
		if (y>=H-1) y=2*H-y-2;
		return y;
	}
	
	float getPixel(int x,int y){
		return Img[xcord(x)][ycord(y)];
	}
	void putPixel(int x,int y, float val){
		Img[xcord(x)][ycord(y)] = val;
	}
	
	float [][] getMatrix(){
		return Img;
	}
}