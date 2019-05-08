package jsonPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ModelDetections.BoxPos;
import ModelDetections.Detection;
import networkAnalysis.ImageDetection;
/**
 * A class to read the custom Json object needed to compare a neural network the the user defined boundings
 * @author UP766290
 *
 */
public class JSONReader {
	
	private ArrayList<ImageDetection> imageArray = new ArrayList<ImageDetection>();
	
	public JSONReader() {

	}
	/**
	 * reads Json object from a file
	 * @param filePath
	 */
	public void readJson(String filePath) {

		try {
			System.out.println("ParsingJson : " + filePath);
			readJsonObject(filePath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Reads Json object and parses all objects and boundings
	 * @param filePath
	 * @throws IOException
	 * @throws ParseException
	 */
	public void readJsonObject(String filePath) throws IOException, ParseException {
		ArrayList<ImageDetection> imageArray = new ArrayList<ImageDetection>();
		// Reads file as a UTF-8
		String jsonString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		JSONArray jsonArray = new JSONArray(jsonString);
		// For all images in array
		for (int i = 0; i < jsonArray.length(); i++) {
			
			JSONArray elementArray = new JSONArray(jsonArray.get(i).toString());
			ArrayList<Detection> detectionArray = new ArrayList<Detection>();
			try {
				// Starts at 2 as we only want detections
				for (int k = 2; k < elementArray.length(); k++) {
					JSONObject object = new JSONObject(elementArray.get(k).toString());
					Detection detectionObject = new Detection(k-2, (String) object.get("Title"), Float.parseFloat(object.get("Confidence").toString()), 
							new BoxPos(Float.parseFloat(object.get("Left").toString()), Float.parseFloat(object.get("Top").toString()), 
									Float.parseFloat(object.get("Width").toString()), Float.parseFloat(object.get("Height").toString())));
					detectionArray.add(detectionObject);
				}
				
			} catch (org.json.JSONException ex) {
				// Exception caught for no detections in image
				System.out.println(ex);
			} catch (Exception e) {
				System.out.println(e);
			}
			
			
			imageArray.add(new ImageDetection(elementArray.get(0).toString(),
					elementArray.get(1).toString(), 
					detectionArray));
			setImageArray(imageArray);
		}
		

	}

	public ArrayList<ImageDetection> getImageArray() {
		return imageArray;
	}

	public void setImageArray(ArrayList<ImageDetection> imageArray) {
		this.imageArray = imageArray;
	}

}
