package org.TensorFlowDetection;

import ModelDetections.*;
import org.TensorFlowDetection.IOUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import ModelDetections.GraphBuilder;
import ModelDetections.Detection;
import imageUtils.AddNoise;
import imageUtils.RemoveArea;
import jsonPackage.JSONObjectWriter;
import imageUtils.BlurImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * ObjectDetector class to detect objects using pre-trained models with
 * TensorFlow Java API.
 */
public class ObjectDetectorTensorFlow {
	private final static Logger LOG = LoggerFactory.getLogger(ObjectDetectorTensorFlow.class);
	private byte[] GRAPH_DEF;
	private List<String> LABELS;
	private static final int SIZE = 416;
	private static final float MEAN = 255f;
	private JSONObjectWriter json = new JSONObjectWriter();
	

	public ObjectDetectorTensorFlow() {
		try {
			LABELS = IOUtil.readAllLinesOrExit("D:\\MLModels\\TF\\yolo-voc-labels.txt");
			GRAPH_DEF = IOUtil.readBytesFromFile("D:\\MLModels\\TF\\yolo-voc.pb");

		} catch (Exception ex) {
			LOG.error("Failed to load graphs and labels");
		}
	}

	/**
	 * Detect objects on the given image
	 * 
	 * @param imageLocation the location of the image
	 * @param json
	 * @return
	 */
	public JSONObject detect(final File imageFile, JSONObject json) {
		byte[] image = IOUtil.readBytesFromFile(imageFile.getAbsolutePath());
		JSONArray detectionsArray;
		double[] blurArray = new double[] { 2, 4, 8, 10 };
		double[] distortion = new double[] { 0.01, 0.1, 0.2 };
		double[][] areaRemoved = new double[][] { { 5, 5, 0.0001 }, { 10, 10, 0.0002 }, { 20, 20, 0.0003 } };
		JSONObjectWriter writer = new JSONObjectWriter();
		// Original Image
		detectionsArray = testImage(image, imageFile.getAbsolutePath(), "Original");
		writer.createImageArray(imageFile.getName(), "Original");
		
		// Test all distorted images
		for (double distort : distortion) {
			AddNoise.setImpulseRatio(distort);
			byte[] distortImage = AddNoise.impulseNoise(image);
			testImage(distortImage, imageFile.getAbsolutePath(), "Distortion" + distort);
			writer.createImageArray(imageFile.getName(), "Distortion" + distort);
			writer.publish("D:\\MLModels\\TF\\outputDir\\out.json");
			
		}
		// Test Blur image
		for (double blurRate : blurArray) {
			byte[] blueImage = BlurImage.blurImage(image, blurRate);
			testImage(blueImage, imageFile.getAbsolutePath(), "Blur" + blurRate);
			writer.createImageArray(imageFile.getName(), "Blur" + blurRate);
			writer.publish("D:\\MLModels\\TF\\outputDir\\out.json");
		}

		// test remove part of image
		for (double[] square : areaRemoved) {
			RemoveArea.setAreaOfBlackHeight((int) square[0]);
			RemoveArea.setAreaOfBlackWidth((int) square[1]);
			byte[] removeArea = RemoveArea.removeArea(image, 0.01);
			testImage(removeArea, imageFile.getAbsolutePath(), "PartsRemoved" + square[0] + square[1]);
			writer.createImageArray(imageFile.getName(), "PartsRemoved" + square[0] + square[1]);
			writer.publish("D:\\MLModels\\TF\\outputDir\\out.json");

		}
		writer.publish("D:\\MLModels\\TF\\outputDir\\out.json");
		return json;
	}
	/**
	 * tests neural network against the user makred up images
	 * @param image
	 * @param imageLocation
	 * @param imageType
	 * @return
	 */
	public JSONArray testImage(byte[] image, String imageLocation, String imageType) {
		try (Tensor<Float> normalizedImage = normalizeImage(image)) {
			List<Detection> recognitions = YOLOClassifier.getInstance().classifyImage(executeYOLOGraph(normalizedImage),
					LABELS);
			JSONArray detectionsArray = new JSONArray();
			for (Detection recognition : recognitions) {
			
				for (Detection detections : recognitions) {
					json.addObject(detectionsArray, detections.getLocation().getTop(),
							(int) (detections.getLocation().getHeight()), detections.getLocation().getLeft(),
							(int) (detections.getLocation().getWidth()), detections.getConfidence(),
							detections.getTitle());
					LOG.info("Object: {} - confidence: {}", recognition.getTitle(), recognition.getConfidence());
				}
	
				
			}
			ImageUtil.getInstance().labelImage(image, recognitions, IOUtil.getFileName(imageLocation), imageType);
			return detectionsArray;
		}
	}

	/**
	 * Pre-process input. It resize the image and normalize its pixels
	 * 
	 * @param imageBytes Input image
	 * @return Tensor<Float> with shape [1][416][416][3]
	 */
	private Tensor<Float> normalizeImage(final byte[] imageBytes) {
		try (Graph graph = new Graph()) {
			GraphBuilder graphBuilder = new GraphBuilder(graph);

			final Output<Float> output = graphBuilder.div( // Divide each pixels with the MEAN
					graphBuilder.resizeBilinear( // Resize using bilinear interpolation
							graphBuilder.expandDims( // Increase the output tensors dimension
									graphBuilder.cast( // Cast the output to Float
											graphBuilder.decodeJpeg(graphBuilder.constant("input", imageBytes), 3),
											Float.class),
									graphBuilder.constant("make_batch", 0)),
							graphBuilder.constant("size", new int[] { SIZE, SIZE })),
					graphBuilder.constant("scale", MEAN));

			try (Session session = new Session(graph)) {
				return session.runner().fetch(output.op().name()).run().get(0).expect(Float.class);
			}
		}
	}

	/**
	 * Executes graph on the given preprocessed image
	 * 
	 * @param image preprocessed image
	 * @return output tensor returned by tensorFlow
	 */
	private float[] executeYOLOGraph(final Tensor<Float> image) {
		try (Graph graph = new Graph()) {
			graph.importGraphDef(GRAPH_DEF);
			try (Session s = new Session(graph);
					Tensor<Float> result = s.runner().feed("input", image).fetch("output").run().get(0)
							.expect(Float.class)) {
				float[] outputTensor = new float[YOLOClassifier.getInstance().getOutputSizeByShape(result)];
				FloatBuffer floatBuffer = FloatBuffer.wrap(outputTensor);
				result.writeTo(floatBuffer);
				return outputTensor;
			}
		}
	}

	/**
	 * Prints out the recognize objects and its confidence
	 * NOT CALLED CURRENTLY 
	 * @param recognitions list of recognitions
	 */
	private void printToConsole(final List<Detection> recognitions) {
		for (Detection recognition : recognitions) {
			LOG.info("Object: {} - confidence: {}", recognition.getTitle(), recognition.getConfidence());
		}
	}

}
