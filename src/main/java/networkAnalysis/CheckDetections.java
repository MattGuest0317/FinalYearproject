package networkAnalysis;

import networkAnalysis.NetworkRate;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import ModelDetections.Detection;

/**
 * A class to display the output of 2 neural networks by checking its detections
 * against the marked up images
 * 
 * @author UP766290
 *
 */
public class CheckDetections {
	private static ArrayList<NetworkRate> networkRate = new ArrayList<NetworkRate>();
	private static ArrayList<ImageStats> imageDetectionPercentage = new ArrayList<ImageStats>();

	/**
	 * Constructer takes a array of marked upimages and a array of detected images
	 * 
	 * @param proposedDetections
	 * @param actualDetections
	 * @param imageName
	 */
	public static void checkDetections(ArrayList<Detection> proposedDetections, ArrayList<Detection> actualDetections,
			String imageName) {
		// produce console output
		System.out.println("For Image " + imageName + " the percentage of detections are as followed");
		System.out.println("Marked up number of detections : " + proposedDetections.size());

		System.out.println("Number of Detections : " + actualDetections.size());
		float average = 0;
		for (Detection element : actualDetections) {
			average = average + element.getConfidence();
		}
		average = average / actualDetections.size();
		System.out.println("Average Confidence : " + average);

		System.out.println("Intersection Of Union from marked up detections :");
		IoU(proposedDetections, actualDetections, imageName, average);

		System.out.println("");

	}

	/**
	 * Method to produce the intersection of union
	 * 
	 * @param proposedDetections
	 * @param actualDetections
	 * @param imageName
	 * @param averageConf
	 */
	public static void IoU(ArrayList<Detection> proposedDetections, ArrayList<Detection> actualDetections,
			String imageName, Float averageConf) {
		float averageIoU = 0;
		for (Detection detection : proposedDetections) {
			// Use rectangles as they have an intersection property
			Rectangle proposedDetectionRect = new Rectangle((int) detection.getLocation().getLeft(),
					(int) detection.getLocation().getTop(), detection.getLocation().getWidthInt(),
					detection.getLocation().getHeightInt());
			ArrayList<Float> detectionarray = new ArrayList<Float>();
			ArrayList<Float> totalImageDetections = new ArrayList<Float>();
			for (Detection actualDetection : actualDetections) {
				// Only allow images with the same classifier name
				if (detection.getTitle().compareTo(actualDetection.getTitle()) == 0) {
					Rectangle actualDetectionRectangle = new Rectangle((int) actualDetection.getLocation().getLeft(),
							(int) actualDetection.getLocation().getTop(), actualDetection.getLocation().getWidthInt(),
							actualDetection.getLocation().getHeightInt());
					proposedDetectionRect.getBounds();
					// Check if they intersect or if they completely overlap each other
					if (actualDetectionRectangle.intersects(proposedDetectionRect)
							|| (actualDetectionRectangle.getX() == proposedDetectionRect.getX()
									&& actualDetectionRectangle.getY() == proposedDetectionRect.getY()
									&& actualDetectionRectangle.getWidth() == proposedDetectionRect.getWidth()
									&& actualDetectionRectangle.getHeight() == proposedDetectionRect.getHeight())) {
						Rectangle intersection = actualDetectionRectangle.intersection(proposedDetectionRect);
						float percentageOverlap = area(intersection.getHeight(), intersection.getWidth())
								/ (area(actualDetectionRectangle.getWidth(), actualDetectionRectangle.getHeight())
										+ area(proposedDetectionRect.width, proposedDetectionRect.height)
										- area(intersection.getHeight(), intersection.getWidth()));
						// Add a intersected array
						detectionarray.add(percentageOverlap);
					}
				}
			}

			if (!detectionarray.isEmpty() && detectionarray != null) {
				// Get the maximum value
				Float obj = (Float) Collections.max(detectionarray);
				totalImageDetections.add(obj);
				float sum = 0;

				// Sum all elements
				for (Float element : totalImageDetections) {
					sum = sum + element;
				}
				System.out.println("Intersection of Union for marked up element " + detection.getTitle() + " "
						+ detection.getId() + " has an overlap of " + (sum * 100) + "%");
				averageIoU = averageIoU + sum;
			}
		}
		System.out.println(
				"Average Intersection of Union for image is " + (averageIoU / actualDetections.size() * 100) + "%");
		if (!networkRate.isEmpty()) {
			boolean addItem = false;
			for (NetworkRate image : networkRate) {
				if (image.getImageName().equals(imageName)) {
					// Add values to static class for data collection
					image.addElementIou(averageIoU);
					image.addElementConf(averageConf);

					addItem = false;
					break;
				} else {
					addItem = true;
				}
			}
			if (addItem) {
				networkRate.add(new NetworkRate(imageName, averageIoU, averageConf));
			}

		} else {
			networkRate.add(new NetworkRate(imageName, averageIoU, averageConf));
		}

	}
	/**
	 * 
	 * Returns the area of d times e
	 * @param d
	 * @param e
	 * @return
	 */
	public static float area(double d, double e) {
		return (float) (d * e);

	}

	public static ArrayList<ImageStats> getImageDetectionPercentage() {
		return imageDetectionPercentage;
	}

	public static void setImageDetectionPercentage(ArrayList<ImageStats> imageDetectionPercentage) {
		CheckDetections.imageDetectionPercentage = imageDetectionPercentage;
	}

	public static ArrayList<NetworkRate> getNetworkRate() {
		return networkRate;
	}

	public static void setNetworkRate(ArrayList<NetworkRate> networkRate) {
		CheckDetections.networkRate = networkRate;
	}

}
