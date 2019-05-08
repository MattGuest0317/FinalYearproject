package ui;

import javafx.scene.image.Image;


import java.io.File;
import java.util.ArrayList;

import ModelDetections.Detection;
/**
 * A class that extends images to add in image name and array list of detections to an image
 * @author UP766290
 *
 */
public class ImageDetections extends Image{

	private ArrayList<Detection> detections;
	private File imageName;
	public ImageDetections(String url, ArrayList<Detection>  detections, File imageName) {
		super(url);
		this.detections = detections;
		this.imageName = imageName;
		
	}
	
	public ArrayList<Detection> getDetections() {
		return detections;
	}
	public void setDetections(ArrayList<Detection> detections) {
		this.detections = detections;
	}

	public File getImageFile() {
		return imageName;
	}

	public void setImageName(File imageName) {
		this.imageName = imageName;
	}
	
}