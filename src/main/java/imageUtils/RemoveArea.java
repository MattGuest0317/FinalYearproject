package imageUtils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
/**
 * Remove certain areas of a image x times through an area s n x n
 * @author UP766290
 *
 */
public class RemoveArea {
	private static int areaOfBlackWidth = 0;
	private static int areaOfBlackHeight = 0;
	private static double ratio;
	private static int[] blackColor = new int[] { 0, 0, 0, 255 };

	public RemoveArea() {

	}
	/**
	 * Methods that removes an area from a byte{} image with the ratio x
	 * @param image
	 * @param ratio
	 * @return
	 */
	@SuppressWarnings("null")
	public static byte[] removeArea(byte[] image, double ratio) {
		InputStream in = new ByteArrayInputStream(image);
		BufferedImage bImage = null;
		try {
			bImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage output = bImage;
		output.setData(bImage.getData());
		WritableRaster out = output.getRaster();
		double rand;
		int width = bImage.getWidth(); // width of the image
		int height = bImage.getHeight(); // height of the image
		Random randGen = new Random();
		// Array for rgb alpha

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				rand = randGen.nextDouble();
				if (rand < ratio) {
					for (int k = 0; k < areaOfBlackHeight; k++) {
						for (int m = 0; m < areaOfBlackWidth; m++) {
							try {
								out.setPixel(i + k, j + m, blackColor);
							} catch (ArrayIndexOutOfBoundsException e) {
							}
						}
					}
					out.setPixel(i, j, blackColor);
				}
			} // Width i
		} // Height j

		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// Save image as a jpg to byte array output stream
			ImageIO.write(output, "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	public static int getAreaOfBlackWidth() {
		return areaOfBlackWidth;
	}

	public static void setAreaOfBlackWidth(int areaOfBlackWidth1) {
		areaOfBlackWidth = areaOfBlackWidth1;
	}

	public static int getAreaOfBlackHeight() {
		return areaOfBlackHeight;
	}

	public static void setAreaOfBlackHeight(int areaOfBlackHeight1) {
		areaOfBlackHeight = areaOfBlackHeight1;
	}

	public static double getRatio() {
		return ratio;
	}
	
	public static void setRatio(int ratio1) {
		RemoveArea.ratio = ratio1;
	}

}
