package FruitJanissary;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Orange extends Fruit implements Sliceable
{
    // Data Fields - The orange image and the two halves
    public ImageView imgFullOrange;
    public ImageView imgHalfOrange1;
    public ImageView imgHalfOrange2;
    public ImageView scoreOrange;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;


    // Constructors
    public Orange(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullOrange = new ImageView("media/Orange.png");
        
        // Set any X, Y, and the orange dimensions

        this.imgFullOrange.setFitWidth(150);
        this.imgFullOrange.setFitHeight(100);
        
        // Place the orange on the pane
        anchorPane.getChildren().add(this.imgFullOrange);
    }
    
    public Orange(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the orange data from parametars above
        this.imgFullOrange = new ImageView(filePath);
        
        this.imgFullOrange.setX(x);
        this.imgFullOrange.setX(y);

        
        anchorPane.getChildren().add(this.imgFullOrange);
    }
    
    // Methods
    @Override
    public void slice(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        imgHalfOrange1 = new ImageView("media/orangehalf.png");
        imgHalfOrange2 = new ImageView("media/orangehalf.png");
        scoreOrange = new ImageView("media/scoreOrange.png");


        // Fix them at a hard-coded position - They will start moving/rotating from it
        imgHalfOrange1.setRotate(-80);
        imgHalfOrange2.setRotate(90);

        imgHalfOrange1.setLayoutX(this.imgFullOrange.getLayoutX());
        imgHalfOrange1.setLayoutY(this.imgFullOrange.getLayoutY());
        
        imgHalfOrange2.setLayoutX(this.imgFullOrange.getLayoutX());
        imgHalfOrange2.setLayoutY(this.imgFullOrange.getLayoutY());
        
        imgHalfOrange1.setFitWidth(120);
        imgHalfOrange1.setFitHeight(100);
        
        imgHalfOrange2.setFitWidth(120);
        imgHalfOrange2.setFitHeight(100);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(imgHalfOrange1);
        anchorPane.getChildren().add(imgHalfOrange2);
        anchorPane.getChildren().add(scoreOrange);

        //score gorselinin boyutlarinin ve koordinatlarinin ayarlanmasi
        scoreOrange.setLayoutX(this.imgFullOrange.getLayoutX()+50);
        scoreOrange.setLayoutY(this.imgFullOrange.getLayoutY());

        scoreOrange.setFitWidth(70);
        scoreOrange.setFitHeight(100);
        
        // Remove the orange and make the two halves of orange visible
        this.imgFullOrange.setVisible(false);
        imgHalfOrange1.setVisible(true);
        imgHalfOrange2.setVisible(true);
        scoreOrange.setVisible(true);

        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(3), e ->
        {
            gravity(this.imgHalfOrange1, this.imgHalfOrange2,this.scoreOrange);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }



    @Override
    public void gravity(ImageView imgHalfOrange1, ImageView imgHalfOrange2, ImageView scoreOrange)
    {
        // Rotate and move in X, Y
        imgHalfOrange1.setRotate(imgHalfOrange1.getRotate() + 1);
        imgHalfOrange2.setRotate(imgHalfOrange2.getRotate() + 1);
        
        imgHalfOrange1.setX(imgHalfOrange1.getX() + 0.4);
        imgHalfOrange1.setY(imgHalfOrange1.getY() + 1);
        
        imgHalfOrange2.setX(imgHalfOrange2.getX() - 0.4);
        imgHalfOrange2.setY(imgHalfOrange2.getY() + 1);

        scoreOrange.setY(scoreOrange.getY()-1);


    }

    
    @Override
    public void createSliceTrace(double startX, double startY, double endX, double endY, AnchorPane pane)
    {
        // Get slash slope
        double slope = (endY - startY) / (endX - startX);

        // Get angle of incline with +ve X axis
        double angle = Math.toDegrees(Math.atan(slope));

        // Define the slash image and create the object for it
        ImageView trace = new ImageView("media/slash_trace.gif");
        ImageView splash = new ImageView("media/splash.png");

        splash.setFitWidth(100);
        splash.setFitHeight(100);

        // Set rotate angle we calculated, 45 hard-coded value
        trace.setRotate(45 - angle);
        splash.setRotate(45-angle);

        // Firstly make the slash invisible
        trace.setVisible(false);
        splash.setVisible(false);

        // Tie the trace with our full red-app layout x, y
        trace.setLayoutX(this.imgFullOrange.getLayoutX() - 30);
        trace.setLayoutY(this.imgFullOrange.getLayoutY() - 30);

        splash.setLayoutX(this.imgFullOrange.getLayoutX()+35);
        splash.setLayoutY(this.imgFullOrange.getLayoutY()+25);

        // Add the trace to the pane
        pane.getChildren().add(trace);
        pane.getChildren().add(splash);

        // Make animation to show the trace just for 300 millisecond
        Timeline showSlash = new Timeline(
                new KeyFrame(Duration.ZERO, e ->
                {
                    trace.setVisible(true);

                }
                ),
                new KeyFrame(Duration.millis(500), e->
                {
                    trace.setVisible(false);

                }
                )
        );

        Timeline showSplash = new Timeline(
                new KeyFrame(Duration.ZERO, e ->
                {

                    splash.setVisible(true);
                }
                ),
                new KeyFrame(Duration.millis(300), e->
                {

                    splash.setVisible(false);
                }
                )
        );

        showSlash.setCycleCount(1);
        showSlash.play();
        showSplash.setCycleCount(1);
        showSplash.play();
    }

    /* Function to move fruit */
    @Override
    public void createFruit(AnchorPane pane)
    {
        // Random number have range this good in game physics
        this.radiusX = ((pane.getPrefWidth() / 2) - 200) * ThreadLocalRandom.current().nextDouble(0.7, 1.5);
        this.radiusY = (pane.getPrefHeight() - 200) * ThreadLocalRandom.current().nextDouble(0.7, 1.5);
        
        this.X = pane.getPrefWidth()/2 - this.radiusX; // Starting point for fruit
        this.imgFullOrange.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Orange.centerX - this.radiusX && this.X < Orange.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Orange.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Orange.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Orange
            this.imgFullOrange.setLayoutX(this.X);
            this.imgFullOrange.setLayoutY(this.Y);
            this.imgFullOrange.setRotate(this.imgFullOrange.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}