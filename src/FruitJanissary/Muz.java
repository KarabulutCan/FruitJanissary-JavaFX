package FruitJanissary;

import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;

public class Muz extends Fruit implements Sliceable {
    // Data Fields - The banana image and the two halves
    public ImageView imgFullBanana;
    public ImageView imgHalfBanana1;
    public ImageView imgHalfBanana2;
    public ImageView scoreBanana;

    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    public boolean isSlice = false;


    // Static data field to be used in pause purpose
    public static Timeline bnaAnimation;

    // Constructors
    public Muz(AnchorPane anchorPane) {
        // Default values
        this.imgFullBanana = new ImageView("media/banana.png");

        // Set any X, Y, and the banana dimensions

        this.imgFullBanana.setFitWidth(150);
        this.imgFullBanana.setFitHeight(100);

        // Place the banana on the pane
        anchorPane.getChildren().add(this.imgFullBanana);
    }

    public Muz(String filePath, double x, double y, AnchorPane anchorPane) {
        // Set the banana data from parametars above
        this.imgFullBanana = new ImageView(filePath);

        this.imgFullBanana.setX(x);
        this.imgFullBanana.setX(y);
        this.imgFullBanana.setFitWidth(150);
        this.imgFullBanana.setFitHeight(100);

        anchorPane.getChildren().add(this.imgFullBanana);
    }

    // Methods
    @Override
    public void slice(AnchorPane anchorPane) {
        // Create the two halves - Set image for every half
        imgHalfBanana1 = new ImageView("media/halfbanana.png");
        imgHalfBanana2 = new ImageView("media/halfbanana.png");
        scoreBanana = new ImageView("media/scoreBanana.png");
        //set slice true
        this.isSlice=true;

        // Fix them at a hard-coded position - They will start moving/rotating from it
        imgHalfBanana1.setRotate(-180);
        imgHalfBanana2.setRotate(45);

        imgHalfBanana1.setLayoutX(this.imgFullBanana.getLayoutX());
        imgHalfBanana1.setLayoutY(this.imgFullBanana.getLayoutY());

        imgHalfBanana2.setLayoutX(this.imgFullBanana.getLayoutX());
        imgHalfBanana2.setLayoutY(this.imgFullBanana.getLayoutY());

        imgHalfBanana1.setFitWidth(100);
        imgHalfBanana1.setFitHeight(150);

        imgHalfBanana2.setFitWidth(100);
        imgHalfBanana2.setFitHeight(150);

        //score gorselinin boyutlarinin ve koordinatlarinin ayarlanmasi

        scoreBanana.setLayoutX(this.imgFullBanana.getLayoutX() + 50);
        scoreBanana.setLayoutY(this.imgFullBanana.getLayoutY());

        scoreBanana.setFitWidth(70);
        scoreBanana.setFitHeight(100);


        // Add the two halves to the pane
        anchorPane.getChildren().add(imgHalfBanana1);
        anchorPane.getChildren().add(imgHalfBanana2);
        anchorPane.getChildren().add(scoreBanana);

        // Remove the banana and make the two halves of banana visible
        imgFullBanana.setVisible(false);
        imgHalfBanana1.setVisible(true);
        imgHalfBanana2.setVisible(true);
        scoreBanana.setVisible(true);


    // Start animation
    Timeline animation = new Timeline(new KeyFrame(Duration.millis(3), e ->
    {
        gravity(this.imgHalfBanana1, this.imgHalfBanana2, this.scoreBanana);

    }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
}



    @Override
    public void gravity(ImageView imgHalfBanana1, ImageView imgHalfBanana2, ImageView scoreBanana)
    {
        // Rotate and move in X, Y
        imgHalfBanana1.setRotate(imgHalfBanana1.getRotate() + 1);
        imgHalfBanana2.setRotate(imgHalfBanana2.getRotate() + 1);
        
        imgHalfBanana1.setX(imgHalfBanana1.getX() + 0.5);
        imgHalfBanana1.setY(imgHalfBanana1.getY() + 1);
        
        imgHalfBanana2.setX(imgHalfBanana2.getX() - 0.5);
        imgHalfBanana2.setY(imgHalfBanana2.getY() + 1);

        scoreBanana.setY(scoreBanana.getY()-1);

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
        trace.setLayoutX(this.imgFullBanana.getLayoutX() - 30);
        trace.setLayoutY(this.imgFullBanana.getLayoutY() - 30);

        splash.setLayoutX(this.imgFullBanana.getLayoutX()+35);
        splash.setLayoutY(this.imgFullBanana.getLayoutY()+25);

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
        this.imgFullBanana.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Muz.centerX - this.radiusX && this.X < Muz.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Muz.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Muz.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Muz
            this.imgFullBanana.setLayoutX(this.X);
            this.imgFullBanana.setLayoutY(this.Y);
            this.imgFullBanana.setRotate(this.imgFullBanana.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
        bnaAnimation = makeAnimation;
    }
}