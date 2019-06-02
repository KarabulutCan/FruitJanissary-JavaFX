package FruitJanissary;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Lemon extends Fruit implements Sliceable
{
    // Data Fields - The watermelon image and the two halves
    public ImageView imgLemon;
    public ImageView imgHalfLemon1;
    public ImageView imgHalfLemon2;
    public ImageView scoreLemon;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y; 
    
    // Constructors
    public Lemon(AnchorPane anchorPane)
    {
        // Default values
        this.imgLemon = new ImageView("media/lemon.png");

        this.imgLemon.setFitWidth(150);
        this.imgLemon.setFitHeight(100);
        
        // Place the watermelon on the pane
        anchorPane.getChildren().add(this.imgLemon);
    }

    public Lemon(String filePath, double x, double y, double fitWidth, double fitHeight, AnchorPane anchorPane)
    {
        // Set the watermelon data from parametars above
        this.imgLemon = new ImageView(filePath);
        
        this.imgLemon.setLayoutX(x);
        this.imgLemon.setLayoutY(y);
        this.imgLemon.setFitWidth(fitWidth);
        this.imgLemon.setFitHeight(fitHeight);
        
        anchorPane.getChildren().add(this.imgLemon);
    }

    @Override
    public void slice(AnchorPane anchorPane) {

            // Create the two halves - Set image for every half
            imgHalfLemon1 = new ImageView("media/halflemon.png");
            imgHalfLemon2 = new ImageView("media/halflemon.png");
            scoreLemon = new ImageView("media/scoreWatermelon.png");

            // Fix them at a hard-coded position - They will start moving/rotating from it

            imgHalfLemon1.setRotate(-45);
            imgHalfLemon2.setRotate(45);

            imgHalfLemon1.setLayoutX(this.imgLemon.getLayoutX()+45);
            imgHalfLemon1.setLayoutY(this.imgLemon.getLayoutY()+10);

            imgHalfLemon2.setLayoutX(this.imgLemon.getLayoutX()-25);
            imgHalfLemon2.setLayoutY(this.imgLemon.getLayoutY()+32);

            imgHalfLemon1.setFitWidth(90);
            imgHalfLemon1.setFitHeight(80);

            imgHalfLemon2.setFitWidth(90);
            imgHalfLemon2.setFitHeight(80);

             //score gorselinin boyutlarinin ve koordinatlarinin ayarlanmasi

                scoreLemon.setLayoutX(this.imgLemon.getLayoutX()+50);
                scoreLemon.setLayoutY(this.imgLemon.getLayoutY());

                scoreLemon.setFitWidth(70);
                scoreLemon.setFitHeight(100);

            // Add the two halves to the pane
            anchorPane.getChildren().add(imgHalfLemon1);
            anchorPane.getChildren().add(imgHalfLemon2);
            anchorPane.getChildren().add(scoreLemon);

            // Remove the watermelon and make the two halves of watermelon visible
            this.imgLemon.setVisible(false);
            imgHalfLemon1.setVisible(true);
            imgHalfLemon2.setVisible(true);
            scoreLemon.setVisible(true);

            // Start animation
            Timeline animation = new Timeline(new KeyFrame(Duration.millis(3), e ->
            {
                gravity(this.imgHalfLemon1, this.imgHalfLemon2,this.scoreLemon);
            }));

            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play();
        }


    @Override
    public void gravity(ImageView imgHalfLemon1, ImageView imgHalfLemon2, ImageView scoreLemon)
    {
        // Rotate and move in X, Y
        imgHalfLemon1.setRotate(imgHalfLemon1.getRotate() + 1);
        imgHalfLemon2.setRotate(imgHalfLemon2.getRotate() + 1);
        
        imgHalfLemon1.setX(imgHalfLemon1.getX() + 0.5);
        imgHalfLemon1.setY(imgHalfLemon1.getY() + 1);
        
        imgHalfLemon2.setX(imgHalfLemon2.getX() - 0.5);
        imgHalfLemon2.setY(imgHalfLemon2.getY() + 1);

        scoreLemon.setY(scoreLemon.getY()-1);
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
        trace.setLayoutX(this.imgLemon.getLayoutX() - 30);
        trace.setLayoutY(this.imgLemon.getLayoutY() - 30);

        splash.setLayoutX(this.imgLemon.getLayoutX()+35);
        splash.setLayoutY(this.imgLemon.getLayoutY()+25);

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
        this.radiusX = ((pane.getPrefWidth() / 2) - 200) * ThreadLocalRandom.current().nextDouble(0.7, 1.3);
        this.radiusY = (pane.getPrefHeight() - 200) * ThreadLocalRandom.current().nextDouble(0.7, 1.2);
        
        this.X = pane.getPrefWidth()/2 - this.radiusX; // Starting point for fruit
        this.imgLemon.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(4), makeAnimationAppear ->
        {
            if (this.X >= Lemon.centerX - this.radiusX && this.X < Lemon.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Lemon.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Lemon.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Lemon
            this.imgLemon.setLayoutX(this.X);
            this.imgLemon.setLayoutY(this.Y);
            this.imgLemon.setRotate(this.imgLemon.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}