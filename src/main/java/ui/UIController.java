package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import org.TensorFlowDetection.ObjectDetectorTensorFlow;
import org.json.JSONArray;
import org.json.JSONObject;

import ModelDetections.BoxPos;
import ModelDetections.Detection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jsonPackage.JSONObjectWriter;
import jsonPackage.JSONReader;
import networkAnalysis.CheckDetections;
import networkAnalysis.ImageDetection;
import networkAnalysis.ImageStats;
import networkAnalysis.NetworkRate;
/**
 * The user interface controller
 * @author UP766290
 *
 */
public class UIController implements Initializable {
	// load needed fxml elements
	@FXML
	private ImageView imageView;
	@FXML
	private Button nextImage;
	@FXML
	private Button previousImage;
	@FXML
	private AnchorPane imagePane;
	@FXML
	private ComboBox classifierDropDown;
	@FXML
	private Text boundingText;

	private ArrayList<ImageDetections> imagelist = new ArrayList<ImageDetections>();
	private ArrayList<Detection> detections = new ArrayList<Detection>();
	private ArrayList<BoundingBoxUI> curentImage = new ArrayList<BoundingBoxUI>();
	private int currentDisplayImage = 0;
	private Boolean flipFlop = true;
	private Canvas canvas;
	private GraphicsContext gc;

	private double click1x;
	private double click1y;
	private double click2x;
	private double click2y;
	Rectangle line;
	Point mousePos = new Point();

	String result = "";
	InputStream inputStream;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set inital launch criteria
		boundingText.setText("No Bounding Selected");
		// Load properties file
		try {
			Properties prop = new Properties();
			String propFileName = "Classifier.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			String[] classifiers = prop.getProperty("Classifiers").split(",");

			// Set initial Visable;

			nextImage.setVisible(false);
			previousImage.setVisible(false);

			classifierDropDown.setItems(FXCollections.observableArrayList(classifiers));

		} catch (IOException exception) {

		}
		// add mouse click
		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				imageView.setFocusTraversable(true);
				imageView.requestFocus();
				// Implement flip flop
				if (flipFlop == true) {
					boundingText.setText("Select Bounding 2");
					click1x = 0;
					click1y = 0;
					// Get event data
					click1x = event.getX();
					click1y = event.getY();

					flipFlop = false;
				} else {
					// Get event daya
					click2x = event.getX();
					click2y = event.getY();
					boundingText.setText("Select Bounding 1");
					
					// Draw rectangle on screen
					line = new Rectangle(Math.min(click2x, click1x), Math.min(click2y, click1y),
							(Math.max(click2x, click1x) - Math.min(click2x, click1x)),
							Math.max(click2y, click1y) - Math.min(click2y, click1y));
					try {
						// Create a bounding box on image
						BoundingBoxUI bounding = new BoundingBoxUI(Math.min(click1x, click2x),
								Math.min(click1y, click2y), (click1x - click2x), (click1y - click2y),
								classifierDropDown.getSelectionModel().getSelectedItem().toString());

						curentImage.add(bounding);
						Text text = new Text();
						text.setX(Math.min(click1x, click2x) + 2);
						text.setY(Math.min(click2y, click1y) + 12);
						text.setText(classifierDropDown.getSelectionModel().getSelectedItem().toString());
						line.setFill(null);
						line.setStroke(Color.RED);
						line.setStrokeWidth(2);
						imagePane.getChildren().add(line);
						imagePane.getChildren().add(text);

					} catch (NullPointerException npe) {
						boundingText.setText("Please select a classifier");
						flipFlop = false;
					}

					click1x = event.getSceneX() > 400 ? 400 : event.getSceneX();
					click1y = event.getSceneY() > 400 ? 400 : event.getSceneY();
					flipFlop = true;
				}

				event.consume();
			}
		});
		// Set on key pressed
		imageView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// Set keypresseed search
				if (event.getCode() == KeyCode.F) {
					
					ArrayList<BoundingBoxUI> toRemove = new ArrayList<BoundingBoxUI>();
					// Search for bounding intersect
					for (BoundingBoxUI box : curentImage) {
						if (box.getX() < mousePos.getX() && mousePos.getX() < box.getX() + Math.abs(box.getWidth())
								&& box.getY() < mousePos.getY()
								&& mousePos.getY() < box.getY() + Math.abs(box.getHeight())) {
							toRemove.add(box);
							
						}

					}
					for (BoundingBoxUI remove : toRemove) {
						// Remove bounding from image
						curentImage.remove(remove);

					}
					// Refresh page
					refresh();
				}

			}
		});


	}
	/**
	 * Run tests against neural network
	 * @param event
	 */
	@FXML
	public void runTests(ActionEvent event) {
		JSONReader output = new JSONReader();
		JSONReader markedUp = new JSONReader();
		// Load external Json file
		markedUp.readJson("D:\\MLModels\\TF\\outputDir\\markedUp.json");
		output.readJson("D:\\MLModels\\TF\\outputDir\\out.json");
		// Go through each element
		for (ImageDetection element : output.getImageArray()) {
				for (ImageDetection markedElements : markedUp.getImageArray()) {
					if (element.getImageName().equals(markedElements.getImageName())) {
						if (!element.getDetection().isEmpty() || markedElements.getDetection() != null) {
							System.out.println("Image Name : " + element.getImageName());
							// Check detections on image
							CheckDetections.checkDetections(markedElements.getDetection(), element.getDetection(),
									element.getType());
						}
					}
					;
				}
			
		}
		// Output total NN data
		System.out.println("Neural Network Total Data : ");
		for (NetworkRate image : CheckDetections.getNetworkRate()) {
			float sum = 0;
			for (float value : image.getImagePercentage()) {
				sum = value + sum;
			}
			float conf = 0;
			for (float value : image.getConfidenceArray()) {
				conf = value + conf;
			}
			sum = sum / image.getImagePercentage().size();
			conf = conf / image.getConfidenceArray().size();
			System.out.print("For Image Type : " + image.getImageName());
			System.out.println("\n	Average IoU is " + sum *100 + "%");
			System.out.println("	Average Confidence is " + conf *100 + "%");
		}
		

	}
	/**
	 * Allows user to see images to mark up
	 * @param event
	 */
	@FXML
	public void markImages(ActionEvent event) {
		nextImage.setVisible(true);
		previousImage.setVisible(true);
		// create image list
		ArrayList<ImageDetections> imagelist = new ArrayList<ImageDetections>();
		// Load images from folder
		File folder = new File("D:\\MLModels\\TF\\inputDir");
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				System.out.println(fileEntry.getName());
				imagelist.add(new ImageDetections(fileEntry.toURI().toString(), null, fileEntry));
			}
		}
		setImagelist(imagelist);
		setImageView(currentDisplayImage);
	}

	@FXML
	public void nextImage(ActionEvent event) {
		setDetections(curentImage);
		currentDisplayImage++;
		// Rotate round if at begining
		if (currentDisplayImage > getImagelist().size() - 1) {
			currentDisplayImage = 0;
		}
		setImageView(currentDisplayImage);

		loadCurrentImages();
	}

	@FXML
	public void previousImage(ActionEvent event) {
		setDetections(curentImage);
		currentDisplayImage--;
		// Rotate round if at end
		if (currentDisplayImage < 0) {
			currentDisplayImage = getImagelist().size() - 1;
		}
		setImageView(currentDisplayImage);
		loadCurrentImages();
	}
	/**
	 * Sets detections
	 * @param list
	 */
	private void setDetections(ArrayList<BoundingBoxUI> list) {
		int i = 0;
		// Set detections for image, when image changes
		for (BoundingBoxUI item : list) {
			detections.add(new Detection(i, item.getDetectionClass(), (float) 1, new BoxPos((float) item.getX(),
					(float) item.getY(), (float) item.getWidth(), (float) item.getHeight())));
			i++;
		}
		imagelist.get(currentDisplayImage).setDetections(detections);

	}
	/**
	 * Loads all the bounding on the current image
	 */
	private void loadCurrentImages() {
		ArrayList<Detection> list = getImagelist().get(currentDisplayImage).getDetections();

		if (list != null) {
			detections = imagelist.get(currentDisplayImage).getDetections();

			for (Detection item : list) {
				Rectangle rectangle = new Rectangle(item.getLocation().getLeft(), item.getLocation().getTop(),
						item.getLocation().getWidth(), item.getLocation().getHeight());
				Text text = new Text();
				text.setX(item.getLocation().getLeft() + 2);
				text.setY(item.getLocation().getLeft() + 11);
				text.setText(item.getTitle());
				rectangle.setFill(null);
				rectangle.setStroke(Color.RED);
				rectangle.setStrokeWidth(2);
				imagePane.getChildren().add(rectangle);
				imagePane.getChildren().add(text);
			}
		} else {
			detections = new ArrayList<Detection>();
		}
	}

	@FXML
	public void createJson(ActionEvent event) {
		setDetections(curentImage);
		JSONObjectWriter json = new JSONObjectWriter();
		JSONArray fullArray = new JSONArray();
		for (int i = 0; i < getImagelist().size(); i++) {
			JSONArray image = new JSONArray();
			image = json.createImageArray(getImagelist().get(i).getImageFile().getAbsolutePath(), "MarkedUp");
			try {
				JSONArray detectionsArray = new JSONArray();
				for (Detection detections : getImagelist().get(i).getDetections()) {
					System.out.println(detections.getLocation().getBottom());
					json.addObject(detectionsArray, detections.getLocation().getTop(),
							(int) (detections.getLocation().getHeight()), detections.getLocation().getLeft(),
							(int) (detections.getLocation().getWidth()), detections.getConfidence(),
							detections.getTitle());
				}
				image.put(detectionsArray);
				System.out.println(getImagelist().get(i).getDetections());

				fullArray.put(image);
			} catch (NullPointerException npe) {
				System.out.println("NPE");
			}
		}
		json.publish("D:\\MLModels\\TF\\outputDir\\markedUp.Json", fullArray);
	}

	private void setImageView(int pos) {
		// Display the first image
		curentImage.removeAll(curentImage);
		imagePane.getChildren().clear();
		imagePane.getChildren().add(imageView);
		imageView.setFocusTraversable(true);
		imageView.requestFocus();
		imageView.setImage(getImagelist().get(pos));
	}

	private void refresh() {
		// Display the first image
		imagePane.getChildren().clear();
		System.out.println(imagePane.getChildren());
		imagePane.getChildren().add(imageView);
		for (BoundingBoxUI box : curentImage) {
			System.out.println(box);
			Text text = new Text();
			text.setX(box.getX() + 2);
			text.setY(box.getY() + 11);
			text.setText(box.getDetectionClass());
			box.setFill(null);
			box.setStroke(Color.RED);
			box.setStrokeWidth(2);
			imagePane.getChildren().add(box);
			imagePane.getChildren().add(text);

		}
		System.out.println(imagePane.getChildren());

	}
	/**
	 * The method called by tthe user interface to send all images to the neural network module
	 * this then generates the Json Object for the analysis
	 * @param event
	 */
	@FXML
	public void detect(ActionEvent event) {
		JSONObjectWriter json = new JSONObjectWriter();
		JSONObject imageObject = new JSONObject();
		ObjectDetectorTensorFlow objectdetector = new ObjectDetectorTensorFlow();
		File folder = new File("D:\\MLModels\\TF\\inputDir");
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				System.out.println(fileEntry.getName());
				objectdetector.detect(fileEntry, imageObject);
			}
		}
		

	}

	public ArrayList<ImageDetections> getImagelist() {
		return imagelist;
	}

	public void setImagelist(ArrayList<ImageDetections> imagelist) {
		this.imagelist = imagelist;
	}
}
