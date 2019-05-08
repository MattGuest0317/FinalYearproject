package jsonPackage;

import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
	/**
	 * Class to write the customs Json string for neural network comparison
	 */
public final class JSONObjectWriter {
	
	private static JSONArray json = new JSONArray();
	private static ArrayList<JSONArray> imageArray = new ArrayList<JSONArray>();
	/**
	 * Blank constructuer
	 */
	public JSONObjectWriter() {

	}
	/**
	 * Publishes Json string to file is used as static
	 * @param outputFile
	 */
	public void publish(String outputFile) {

		try (FileWriter file = new FileWriter(outputFile)) {
			file.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * Publishes json string to file if used as object
	 * @param outputFile
	 * @param jsonObject
	 */
	public void publish(String outputFile, JSONArray jsonObject) {

		try (FileWriter file = new FileWriter(outputFile)) {
			file.write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public JSONArray getJson() {
		return json;
	}

	public void setJson(JSONArray json) {
		JSONObjectWriter.json = json;
	}
	/**
	 * Creates a image object for Json
	 * @param imageName
	 * @param imageType
	 * @return
	 */
	public JSONArray createImageArray(String imageName, String imageType) {
		JSONArray imageArray = new JSONArray();
		imageArray.put(imageName);
		imageArray.put(imageType);
		return imageArray;
	}
	
	/**
	 * Adds anoher object to the Json Array to have another image in the set
	 * @param imageJson
	 */
	public void addImageToOutput(JSONArray imageJson) {
		json.put(imageJson);
	}
	
	/**
	 * Adds data into the Json Object returns a Json published with data over empty one supplied 
	 * Null imageArray can be passed in 
	 * @param imageArray
	 * @param top
	 * @param heightInt
	 * @param left
	 * @param widthInt
	 * @param confidence
	 * @param title
	 * @return
	 */
	public JSONArray addObject(JSONArray imageArray, float top, int heightInt, float left, int widthInt,
			Float confidence, String title) {
		JSONObject detection = new JSONObject();
		detection.put("Title",title);
		detection.put("Confidence",confidence);
		detection.put("Top",top);
		detection.put("Left",left);
		detection.put("Width",widthInt);
		detection.put("Height",heightInt);
		imageArray.put(detection);
		return imageArray;
	}

}
