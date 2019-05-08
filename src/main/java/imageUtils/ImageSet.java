package imageUtils;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * A class to hold a set of distorted images
 * @author UP766290
 *
 */
public class ImageSet {
	private BufferedImage originalImage;
	private File originalFilePath;
	private String fileType;
	private BufferedImage noiseImage;
	int height, width;
	private int imageType;
	// DistortedImageSets
	private ArrayList<BufferedImage> bluredImages = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> guassainDistortImages = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> areaRemovedImages = new ArrayList<BufferedImage>();
	
	/**
	 * Constructer to make blured, area removed and gussaian blur
	 * @param image
	 * @param filePath
	 * @param fileType
	 */
	public ImageSet(BufferedImage image, File filePath, String fileType) {
		originalImage = image;
		height = image.getHeight();
		width = image.getWidth();
		imageType = image.getType();
		originalFilePath = filePath;
		this.fileType = fileType;		
		
		double[] blurArray = new double[] {2,4,8,10};
		BlurImage blurImage = new BlurImage();
		// DistortImages
		
		double[] distortion = new double[] {0.1, 0.2, 0.3};
		// Blur image for each blur in array
		for (double distorionInt: distortion) {
			BufferedImage output = new BufferedImage(this.getWidth(), this.getHeight(), this.getImageType());
			AddNoise.setImpulseRatio(distorionInt);
			//output = AddNoise.impulseNoise(this.getOriginalImage(), output);
			guassainDistortImages.add(output);
			
		}
		// Array to set Height, width and frequency of black areas on the image, A height of 0 and width of 0 will result in single pixel being black per frequency
		double[][] areaRemoved = new double[][] {{5,5,0.0001},{10,10,0.0002},{20,20,0.0003}};
		for (double[] square:areaRemoved) {
			RemoveArea.setAreaOfBlackHeight((int) square[0]);
			RemoveArea.setAreaOfBlackWidth((int) square[1]);
			BufferedImage output = new BufferedImage(this.getWidth(), this.getHeight(), this.getImageType());
			
			areaRemovedImages.add(output);
		}
		// Displays image to screen
		displayAll(areaRemovedImages);
		displayAll(bluredImages);
		displayAll(guassainDistortImages);
		
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public BufferedImage getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(BufferedImage originalImage) {
		this.originalImage = originalImage;
	}
	
	public File getOriginalFilePath() {
		return originalFilePath;
	}

	public void setOriginalFilePath(File originalFilePath) {
		this.originalFilePath = originalFilePath;
	}
	/**
	 * Displays an image within a J frame for the user to see it
	 * @param image
	 */
	private void displayImage(BufferedImage image) {
		// Displays Image in a JFrame
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
	}
	// Displays all the images in a new Jframe
	private void displayAll (ArrayList<BufferedImage> arrayOfImages) {
		for (BufferedImage image: arrayOfImages) {
			displayImage(image);
		}
		
	}
	
	

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

}
