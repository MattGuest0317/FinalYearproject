package ui;


import javafx.scene.shape.Rectangle;
/**
 * Class to hold bounding Box data for a detection
 * @author UP766290
 *
 */
public class BoundingBoxUI extends Rectangle {
	
	private String detectionClass;

	public BoundingBoxUI(double minX, double minY, double width, double height) {
		super(minX, minY, width, height);
		
	}
	
	public BoundingBoxUI(double minX, double minY, double width, double height, String detectionClass) {
		super(minX, minY, width, height);
		
		this.detectionClass = detectionClass;
		
	}

	public String getDetectionClass() {
		return detectionClass;
	}

	public void setDetectionClass(String detectionClass) {
		this.detectionClass = detectionClass;
	}

}
