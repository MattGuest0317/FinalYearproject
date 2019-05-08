package org.TensorFlowDetection;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.slf4j.LoggerFactory;
import ModelDetections.Detection;
import jsonPackage.JSONObjectWriter;
import ModelDetections.BoxPos;

/**
 * Util class for image processing.
 */
public class ImageUtil {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ImageUtil.class);
	private static ImageUtil imageUtil;
	private static final int SIZE = 416;
	private static final String OUTPUTDIR = "D:\\MLModels\\TF\\outputDir";
	private int counter = 0;
	private JSONObjectWriter json = new JSONObjectWriter();
	private ImageUtil() {
		IOUtil.createDirIfNotExists(new File(OUTPUTDIR));
	}

	/**
	 * It returns the singleton instance of this class.
	 * 
	 * @return ImageUtil instance
	 */
	public static ImageUtil getInstance() {
		if (imageUtil == null) {
			imageUtil = new ImageUtil();
		}

		return imageUtil;
	}

	/**
	 * Label image with classes and predictions given by the ThensorFLow
	 * 
	 * @param image        buffered image to label
	 * @param recognitions list of recognized objects
	 */
	public void labelImage(final byte[] image, final List<Detection> recognitions, final String fileName, String imageType) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = imageUtil.createImageFromBytes(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		float scaleX = (float) bufferedImage.getWidth() / (float) SIZE;
		float scaleY = (float) bufferedImage.getHeight() / (float) SIZE;
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
		JSONArray imageJsonObject = json.createImageArray(fileName, imageType);
		for (Detection recognition : recognitions) {
			
			BoxPos box = recognition.getScaledLocation(scaleX, scaleY);
			imageJsonObject = json.addObject(imageJsonObject, box.getBottom(), box.getHeightInt(), box.getLeft(), box.getWidthInt(), recognition.getConfidence(), recognition.getTitle());
			// draw text
			graphics.drawString(recognition.getTitle() + " " + recognition.getConfidence(), box.getLeft(),
					box.getTop() - 7);
			// draw bounding box
			graphics.drawRect(box.getLeftInt(), box.getTopInt(), box.getWidthInt(), box.getHeightInt());
			
		}
		json.addImageToOutput(imageJsonObject);
		graphics.dispose();
		saveImage(bufferedImage, OUTPUTDIR + "/" + counter +".jpg");
		counter++;
	}
	
	/**
	 * Saves image to a file as a jpg
	 * @param image
	 * @param target
	 */
	public void saveImage(final BufferedImage image, final String target) {
		try {
			ImageIO.write(image, "jpg", new File(target));
		} catch (IOException e) {
			LOG.error("Unable to save image {}!", target);
		}
	}
	/**
	 * Takes in a bye[] of an image and returns an image of bufferedImage type
	 * @param imageData
	 * @return
	 * @throws IOException
	 */
	private BufferedImage createImageFromBytes(final byte[] imageData) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bais);
		} catch (IOException ex) {
			throw new IOException("Unable to create image from bytes!", ex);
		}
	}
}
