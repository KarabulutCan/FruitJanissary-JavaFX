package FruitJanissary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Home implements Initializable
{
    // Some general variables
    AudioClip backgroundSound, startSound;
    boolean soundWorking = true;
    
    // Variables related to FXML file


    
    @FXML
    private ImageView imgBlockSound;

    
    @FXML
    private ImageView imgBackground;
    
    @FXML
    private ImageView imgLogo;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Play starting background sound
        backgroundSound = new AudioClip(this.getClass().getResource("/sounds/starting_background_music.mp3").toExternalForm());
        backgroundSound.setCycleCount(Timeline.INDEFINITE);
        backgroundSound.play();

        /* Register out events */

        // Start full drag on any of these
        imgBackground.setOnDragDetected(e -> imgBackground.startFullDrag());
        imgLogo.setOnDragDetected(e -> imgLogo.startFullDrag());

    }

    @FXML
    private void onImgStartPressed(MouseEvent event)
    {
        startGame();
    }

    public void startGame ()
    {

        Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(0.5), eventDelay1 ->
                {
                    // This is just delay before we open our new game scene
                    startSound = new AudioClip(this.getClass().getResource("/sounds/starting_sound.mp3").toExternalForm());
                    backgroundSound.stop();
                    startSound.play();
                    startSound.setCycleCount(1);
                }
                ),
                new KeyFrame(Duration.seconds(0.5), eventDelay2 ->
                {
                    Parent root;
                    try
                    {
                        root = FXMLLoader.load(this.getClass().getResource("GameScene.fxml"));
                        Scene gameScene = new Scene(root);
                        FruitJanissary.primaryStage.setScene(gameScene);
                    }
                    catch (IOException ex)
                    {
                        System.out.println("GameScene.fxml not found..");
                    }
                }
                )
        );

        delay.setCycleCount(1);
        delay.play();
    }


    @FXML
    private void onImgSoundPressed(MouseEvent event)
    {
        // Pause/Start background sound
        pausePlaySound(backgroundSound);
    }
    
    @FXML
    private void onImgBlockSoundPressed(MouseEvent event)
    {
        // Pause/Start background sound
        pausePlaySound(backgroundSound);
    }
    
    private void pausePlaySound(AudioClip backgroundSound)
    {
        if (soundWorking)
        {
            // Sound working? Then stop it and make block icon visible
            backgroundSound.stop();
            soundWorking = false;
            imgBlockSound.setVisible(true);
        }
        else
        {
            // Sound not working? Then play it and remove block icon
            backgroundSound.play();
            backgroundSound.setCycleCount(Timeline.INDEFINITE);
            imgBlockSound.setVisible(false);
            soundWorking = true;
        }
    }



    @FXML
    private void onImgStartPressed1(MouseEvent event)
    {
        openHighScoreScene();
    }
    
    public void openHighScoreScene()
    {
        // Make delay before opening the high score new fxml file
        Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(20), eventDelay -> 
                {
                    try
                    {
                        Parent root = FXMLLoader.load(this.getClass().getResource("HighScore.fxml"));
                        Scene highScoreScene = new Scene(root);
                        FruitJanissary.primaryStage.setScene(highScoreScene);
                        backgroundSound.stop();
                    } 
                    catch (IOException ex)
                    {
                        System.out.println("HighScore.fxml not found..");
                    }
                })
        );
        
        delay.setCycleCount(1);
        delay.play();
    }

    @FXML
    private void onImgStartPressed2(MouseEvent event)
    {
        openSignupScene();
    }

    public void openSignupScene()
    {
        // Make delay before opening the high score new fxml file
        Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(20), eventDelay ->
                {
                    try
                    {
                        Parent root = FXMLLoader.load(this.getClass().getResource("Login1.fxml"));
                        Scene SignupScene = new Scene(root);
                        FruitJanissary.primaryStage.setScene(SignupScene);
                        backgroundSound.stop();
                    }
                    catch (IOException ex)
                    {
                        System.out.println("Login1.fxml not found..");
                    }
                })
        );

        delay.setCycleCount(1);
        delay.play();
    }
}