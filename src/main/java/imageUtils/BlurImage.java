package imageUtils;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
/**
 * Class to add a blur distortion to an image
 * @author up766290
 *
 */
public class BlurImage {
	/**
	 * blank constructure
	 */
	public BlurImage() {
		
	}
	
	/**
	 * takes in an image as a byte[] and a radius for the blur. Returns distorted image as byte[]
	 * @param orginalImage
	 * @param radius
	 * @return
	 */
	public static byte[] blurImage(byte[] orginalImage, double radius) {
		InputStream in = new ByteArrayInputStream(orginalImage);
		BufferedImage bImage = null;
		try {
			bImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		double size = radius * 2 + 1;
	    float weight = (float) (1.0f / (size * size));
	    float[] data = new float[(int) (size * size)];

	    for (int i = 0; i < data.length; i++) {
	        data[i] = weight;
	    }

	    Kernel kernel = new Kernel((int)size, (int)size, data);
	    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(op.filter(bImage, null), "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    byte[] bytes = baos.toByteArray();
		return bytes;
	    
	}
}
