package FruitJanissary;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Elma extends Fruit implements Sliceable
{
    // Data Fields - The Elma image and the two halves
    public ImageView imgFullApple;
    public ImageView imgHalfRedApple1;
    public ImageView imgHalfRedApple2;
    public ImageView scoreApple;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    
    // Constructors
    public Elma(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullApple = new ImageView("media/apple.png");
        
        // Elmanin boyutlari belirlendi.
        this.imgFullApple.setFitWidth(80);
        this.imgFullApple.setFitHeight(80);
        
        // Place the orange on the pane
        anchorPane.getChildren().add(this.imgFullApple);
    }
    
    public Elma(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the orange data from parametars above
        this.imgFullApple = new ImageView(filePath);
        
        this.imgFullApple.setX(x);
        this.imgFullApple.setX(y);
        this.imgFullApple.setFitWidth(80);
        this.imgFullApple.setFitHeight(80);
        
        anchorPane.getChildren().add(this.imgFullApple);
    }
    
    // Methods
    @Override
    public void slice(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        this.imgHalfRedApple1 = new ImageView("media/apple_half1.png");
        this.imgHalfRedApple2 = new ImageView("media/apple_half2.png");
        scoreApple = new ImageView("media/scoreApple.png");
        
        // Tum elmadan 2 yarim elma olustugunda olusan parcalarin , elmanin X Y koordinatlarinindan 2 ye bolunmesini sagliyor.
        this.imgHalfRedApple1.setLayoutX(this.imgFullApple.getLayoutX());
        this.imgHalfRedApple1.setLayoutY(this.imgFullApple.getLayoutY());
        
        this.imgHalfRedApple2.setLayoutX(this.imgFullApple.getLayoutX());
        this.imgHalfRedApple2.setLayoutY(this.imgFullApple.getLayoutY());
        
        this.imgHalfRedApple1.setFitWidth(90);
        this.imgHalfRedApple1.setFitHeight(80);
        
        this.imgHalfRedApple2.setFitWidth(90);
        this.imgHalfRedApple2.setFitHeight(80);

        //score gorselinin boyutlarinin ve koordinatlarinin ayarlanmasi
        scoreApple.setLayoutX(this.imgFullApple.getLayoutX()+50);
        scoreApple.setLayoutY(this.imgFullApple.getLayoutY());

        scoreApple.setFitWidth(70);
        scoreApple.setFitHeight(100);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(this.imgHalfRedApple1);
        anchorPane.getChildren().add(this.imgHalfRedApple2);
        anchorPane.getChildren().add(scoreApple);
        
        // Remove the red app and make the two halves of red app visible
        this.imgFullApple.setVisible(false);
        this.imgHalfRedApple1.setVisible(true);
        this.imgHalfRedApple2.setVisible(true);
        scoreApple.setVisible(true);

        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(3), e ->
        {
            gravity(this.imgHalfRedApple1, this.imgHalfRedApple2,this.scoreApple);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }



    @Override
    public void gravity(ImageView imgHalfOrange1, ImageView imgHalfOrange2, ImageView scoreApple)
    {
        // Kesilen Meyvenin iki parcasinin dusus yonu ve sacilma yonunu belirliyor. X sacilma acisini olusturuyor. Y yonu belirliyor.
        this.imgHalfRedApple1.setRotate(imgHalfRedApple1.getRotate() + 1);
        this.imgHalfRedApple2.setRotate(imgHalfRedApple2.getRotate() + 1);
        
        this.imgHalfRedApple1.setX(imgHalfRedApple1.getX() + 0.5);
        this.imgHalfRedApple1.setY(imgHalfRedApple1.getY() + 1);
        
        this.imgHalfRedApple2.setX(imgHalfRedApple2.getX() - 0.5);
        this.imgHalfRedApple2.setY(imgHalfRedApple2.getY() + 1);

        scoreApple.setY(scoreApple.getY()-1);
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
        trace.setLayoutX(this.imgFullApple.getLayoutX() - 30);
        trace.setLayoutY(this.imgFullApple.getLayoutY() - 30);

        splash.setLayoutX(this.imgFullApple.getLayoutX()+35);
        splash.setLayoutY(this.imgFullApple.getLayoutY()+25);
        
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
        this.radiusX = ((pane.getPrefWidth() / 3) - 200) * ThreadLocalRandom.current().nextDouble(0.7, 1.3);
        this.radiusY = (pane.getPrefHeight() - 200) * ThreadLocalRandom.current().nextDouble(0.7, 1.3);

        // Starting point for fruit
        this.X = pane.getPrefWidth()/2 - this.radiusX;
        this.imgFullApple.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline da meyvelerin parabolik hareket etmesi icin asagidaki kodu kullandik.
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(4), makeAnimationAppear ->
        {
            if (this.X >= Elma.centerX - this.radiusX && this.X < Elma.centerX + this.radiusX)
            {
                //X i + yonde ilerletirken, Y ekseninde Tepe noktasi olusturacak sekilde yukari yonlu parabolik haraket yapmasi icin alttaki kodu kullandik.
                this.X++;
                this.Y = Elma.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Elma.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }

            this.imgFullApple.setLayoutX(this.X);
            this.imgFullApple.setLayoutY(this.Y);
            this.imgFullApple.setRotate(this.imgFullApple.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}