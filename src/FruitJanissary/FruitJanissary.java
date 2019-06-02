
package FruitJanissary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FruitJanissary extends Application
{
    // Create a public data field so we can access stage and set new scene for it
    public static Stage primaryStage;


    @Override
    public void start(Stage stage) throws Exception
    {
        // Load the starting scene fxml file at the start of the game
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        
        // The starting scene
        Scene scene = new Scene(root);
        
        // Set the scene to the stage
        stage.setScene(scene);
        
        // Set title/icon for our stage
        stage.setTitle("Fruit Janissary");

        
        // Show our stage
        stage.show();
        
        // Store our stage refrence here
        primaryStage = stage;
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}