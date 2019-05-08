package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light.Point;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
@SuppressWarnings("restriction")
public class UIMain extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		// Loads FXML
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("UI.fxml"));
		
	        Pane root1 = new Pane();
	        @SuppressWarnings("restriction")
			WebView wv = new WebView();
	        final Rectangle selection = new Rectangle();
	        final Point anchor = new Point();
	        // Create on press event to get mouse data
	        wv.setOnMousePressed(event -> {
	            anchor.setX(event.getX());
	            anchor.setY(event.getY());
	            selection.setX(event.getX());
	            selection.setY(event.getY());
	            selection.setFill(null); // transparent 
	            selection.setStroke(Color.BLACK); // border
	            selection.getStrokeDashArray().add(10.0);
	            root1.getChildren().add(selection);
	        });
	         
	        
	         
	        wv.setOnMouseReleased(event -> {
	            // Get selection properties
	            root1.getChildren().remove(selection);
	            selection.setWidth(0);
	            selection.setHeight(0);
	        });
	             
	        root1.getChildren().add(wv);
	        Scene scene = new Scene(root, 500, 500);
	        stage.setScene(scene);
	        stage.setTitle("Final Year Project");
	        stage.show();
	    }
	     

	}

