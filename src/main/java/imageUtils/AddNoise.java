package imageUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.image.*;
/**
 * 
 * A class to add gaussian noise to an image in the form of a byte[]
 * @author UP766290
 *
 */
public class AddNoise {

	public final static int IMPULSE = 0;

	protected static double stdDev = 10.0;

	protected static double impulseRatio = 0.05;
	/**
	 * Blank constructor
	 */
	public AddNoise() {
		
	}

	/**
	 * Sets the amount of noise to add to the image
	 * @param impulseRatio
	 */
	public static void setImpulseRatio(double impulseRatio) {
		AddNoise.impulseRatio = impulseRatio;
	}
	/**
	 * Gets the current amount of noise to be added to the image
	 * @return
	 */
	public static double getImpulseRatio() {
		return impulseRatio;
	}
	/**
	 * Add's gaussian noise to an byte[] and returns the image in the form byte[]. Adds noise based on impluse ratio
	 * @param image
	 * @return
	 */
	public static byte[] impulseNoise(byte[] image) {
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
		int[] imageColours = new int[] { 1, 2, 3, 255 };

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				rand = randGen.nextDouble();
				if (rand < impulseRatio) {
					imageColours = changeArrayColor(imageColours);
					out.setPixel(i, j, imageColours);
				} 
			} // Width i
		} // Height j
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// Saves image as a jpg
			ImageIO.write(output, "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	/**
	 * Takes in [R,G,B,A] changes the colour of the pixel to a random colour 
	 * @param imageColours
	 * @return
	 */
	private static int[] changeArrayColor(int[] imageColours) {
		Random randGen = new Random();
		// From 1-3 as alpha must be set at 255 to be visable
		for (int i = 0; i < imageColours.length - 1; i++) {

			imageColours[i] = randGen.nextInt(255);
		}

		return imageColours;

	}

}
