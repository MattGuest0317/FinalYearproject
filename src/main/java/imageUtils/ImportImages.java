/**package imageUtils;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import imageUtils.ImageSet;
import javafx.stage.Stage;

import javax.imageio.ImageIO;



public class ImportImages {
// Constants
	private static final String LINE_SEPERATOR = ";";
	private static final String JSON_EXTENSION = ".json";
	private ArrayList<ImageSet> imageArrayList = new ArrayList<ImageSet>();
	String imageConfigPath = null;
	
 
	public ImportImages() {
		String basePath = new File("").getAbsolutePath();
		imageConfigPath = new File(basePath + "/src/main/java/resources/ImagePath.txt").getAbsolutePath();
		parseStringToFile();
		
	}

	private Path[] parseStringToFile() {
		ArrayList<File> imageFilePath = new ArrayList<File>();
		String stringPath = null;
		
		try {
			stringPath = readFile();
		} catch (IOException e) {
			// Catch IO exception of errors within reading file
			e.printStackTrace(); 
		}
		ArrayList<String> filePathArrayString = new ArrayList<String>();
		for (String filePath : stringPath.split(LINE_SEPERATOR)) {
			File dirPath = new File(filePath);
			if (dirPath.isDirectory()) {
				
			for (File file : dirPath.listFiles()) {			
				for (final String ext : ALLOWED_FILE_FORMATS) {
					if (file.getPath().endsWith(ext)) {
						
						addImageToList( file, ext);
					}
				}
				}
			}
		}
		

		return null;
	}

	private String readFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(imageConfigPath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(LINE_SEPERATOR);
			}
			return stringBuilder.toString();
		} finally {
			reader.close();
		}

	}
	
	private ArrayList<ImageSet> addImageToList(File fileName, String fileType){
		BufferedImage image = null;
		try {
			String noFileExtenstion = FilenameUtils.removeExtension(fileName.getName());
			File jsonFile = new File(fileName.getParentFile(),noFileExtenstion+JSON_EXTENSION);
			
			image = ImageIO.read(fileName);
			ImageSet customImage = new ImageSet(image, fileName, fileType);
			imageArrayList.add(customImage);
                   
		} catch (final IOException e){
			System.out.println(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageArrayList;
		
	}
}**/
