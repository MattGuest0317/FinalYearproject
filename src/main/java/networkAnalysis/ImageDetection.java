package networkAnalysis;

import java.util.ArrayList;

import org.json.JSONArray;

import ModelDetections.Detection;
/**
 *  A class to hold all detections for an image
 * @author UP766290
 *
 */
public class ImageDetection {
	private String imageName;
	private String type;
	private ArrayList<Detection> detection;

	/**
	 * contrucor to take all nessasary data to create the Json object
	 * @param imageName
	 * @param type
	 * @param detectionArray
	 */
	public ImageDetection(String imageName, String type, ArrayList<Detection> detectionArray) {
		super();
		this.imageName = imageName;
		this.type = type;
		this.detection = detectionArray;
	}

	public ImageDetection() {
		
	}

	public String getImageName() {
		return imageName;
	}

	public ArrayList<Detection> getDetection() {
		return detection;
	}

	public void setDetection(ArrayList<Detection> detection) {
		this.detection = detection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
