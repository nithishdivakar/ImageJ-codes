import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class BilateralFilter_  implements PlugInFilter{
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
		
		ImagePlus imOut = IJ.createImage("min filter","8bit", w, h, 1);
		ImageProcessor ipProc = imOut.getProcessor();
		
		float[][] OUT = bilateralFilter(ip.getFloatArray(),3);
		System.out.println(OUT.length);
		System.out.println(OUT[0].length);
		ipProc.setFloatArray(OUT);
		imOut.show();
	}
	
	private float domainKernel(int i,int j,int k, int l,float Iij,float Ikl,double sd,double sr){
		
		double dist_spat = Math.pow(i-k,2.0)+ Math.pow(j-l, 2.0);
		
		double dist_chro = Math.pow(Iij-Ikl, 2.0);
		
		double exponent = -(dist_spat/(2*sd*sd)) -(dist_chro/(2.0*sr*sr));
		return (float) Math.exp(exponent);
	}
	
	public float[][] bilateralFilter(float[][] I,int s) {
		MirrorBoundaryMatrix InImg = new MirrorBoundaryMatrix(I);
		MirrorBoundaryMatrix OuImg = new MirrorBoundaryMatrix(I.length,I[0].length);
		
		double sigma_G=5.0,sigma_D=5.0;
		
		// float[][] G=Kernels.GaussianKernal(1, sigma_G);
		
		for(int x=0;x<I.length;x++)for(int y=0;y<I[0].length;y++){
			float pix_val = 0.0f;
			float normali = 0.0f;
			for(int i=-s;i<=s;i++) for(int j=-s;j<=s;j++){
				
				float ker = 0.0f;
				
				ker = domainKernel(x,y,x+i,y+j,InImg.getPixel(x, y),InImg.getPixel(x+i, y+j),32.0,32.0);
				pix_val += InImg.getPixel(i+x, j+y)*ker;
				normali += ker;
			}
			OuImg.putPixel(x, y, pix_val/normali);
		}
		return OuImg.getMatrix();
	}

}
