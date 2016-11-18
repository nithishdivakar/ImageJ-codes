import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class FourierDemo_ implements PlugIn{
	@Override
	public void run(String arg) {
		int n = WindowManager.getImageCount();
		
		ImagePlus img1 = WindowManager.getImage(WindowManager.getNthImageID(n));
		ImagePlus img2 = WindowManager.getImage(WindowManager.getNthImageID(n-1));
		
		int w = img1.getHeight();
		int h = img1.getWidth();
		try{
			if(img2.getHeight() != w || img2.getWidth() != h){
				IJ.error("Dimensions are incompatible");
				throw new Exception("Dimensions are not equal");
			}
			Complex [][] Fimg1 = FFT2d.fft2d_image(img1.getProcessor());
			Complex [][] Fimg2 = FFT2d.fft2d_image(img2.getProcessor());



			Complex [][] Img1 = FFT2d.ifft2d(Fimg1);
			Complex [][] Img2 = FFT2d.ifft2d(Fimg2);
			
			float [][]img1mag = FFT2d.fft2mag(Fimg1);
			float [][]img2mag = FFT2d.fft2mag(Fimg2);
			float [][]img1phase = FFT2d.fft2phase(Fimg1);
			float [][]img2phase = FFT2d.fft2phase(Fimg2);

			Complex [][] Fimg12 = new Complex[Fimg1.length][Fimg1[0].length];
			Complex [][] Fimg21 = new Complex[Fimg2.length][Fimg2[0].length];
			
			for(int i=0;i<Fimg1.length;i++)for(int j=0;j<Fimg1[0].length;j++){
				Fimg12[i][j] = new Complex(Math.cos(img1phase[i][j])*img2mag[i][j],Math.sin(img1phase[i][j])*img2mag[i][j]);
				Fimg21[i][j] = new Complex(Math.cos(img2phase[i][j])*img1mag[i][j],Math.sin(img2phase[i][j])*img1mag[i][j]);
			}

			Complex [][] img12 = FFT2d.ifft2d(Fimg12);
			Complex [][] img21 = FFT2d.ifft2d(Fimg21);

			ImagePlus       imOut12 = IJ.createImage("img12","8bit", w, h, 1);
			ImageProcessor ipProc12 = imOut12.getProcessor();
			ImagePlus       imOut21 = IJ.createImage("img21","8bit", w, h, 1);
			ImageProcessor ipProc21 = imOut21.getProcessor();
			
			for(int i=0;i<Img1.length;i++)for(int j=0;j<Img1[0].length;j++){
				ipProc12.putPixelValue(j,i,img12[i][j].abs());
				ipProc21.putPixelValue(j,i,img21[i][j].abs());
			}
			imOut12.show();
			imOut21.show();
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}