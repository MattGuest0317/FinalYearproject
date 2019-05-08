package networkAnalysis;

import java.util.ArrayList;
/**
 *  A class to hold all IoU and Confidence's for each image type
 * @author UP766290
 *
 */
public class NetworkRate {
	private String imageName;
	ArrayList<Float> imagePercentage = new ArrayList<Float>();
	ArrayList<Float> confidenceArray = new ArrayList<Float>();
	
	
	/**
	 * Construcor to create a new image type
	 * @param imageName
	 * @param Iou
	 * @param conf
	 */
	public NetworkRate (String imageName, Float Iou, Float conf) {
		this.imageName = imageName;
		addElementIou(Iou);
		addElementConf(conf);
	}
	
	public void addElementIou(Float value) {
		imagePercentage.add(value);
	}
	public void addElementConf(Float value) {
		confidenceArray.add(value);
	}
	
	public ArrayList<Float> getImagePercentage() {
		return imagePercentage;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImagePercentage(ArrayList<Float> imagePercentage) {
		this.imagePercentage = imagePercentage;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ArrayList<Float> getConfidenceArray() {
		return confidenceArray;
	}

	public void setConfidenceArray(ArrayList<Float> confidenceArray) {
		this.confidenceArray = confidenceArray;
	}
}
