package networkAnalysis;

import java.util.ArrayList;

public class ImageStats {
	private String imageName;
	private ArrayList<Float> averageDetetionPercentage = new  ArrayList<Float>();
	
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public ArrayList<Float> getAverageDetetionPercentage() {
		return averageDetetionPercentage;
	}
	public void setAverageDetetionPercentage(ArrayList<Float> averageDetetionPercentage) {
		this.averageDetetionPercentage = averageDetetionPercentage;
	}
}
