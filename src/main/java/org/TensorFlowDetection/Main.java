package org.TensorFlowDetection;

import java.io.File;

import ui.UIMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jsonPackage.JSONObjectWriter;
import jsonPackage.JSONReader;
import networkAnalysis.CheckDetections;
import networkAnalysis.ImageDetection;

/**
 * 
 * @author UP766290
 *
 */
public class Main extends Application
{
	/**
	 * Runs application
	 * @param args
	 */
    public static void main( String[] args )
    {
   
    	// Launch GUI
    	javafx.application.Application.launch(UIMain.class);

		}

	@Override
	public void start(Stage primaryStage) throws Exception {

			Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
	        primaryStage.setTitle("Registration Form FXML Application");
	        primaryStage.setScene(new Scene(root, 800, 500));
	        primaryStage.show();

		
	}
 
}


