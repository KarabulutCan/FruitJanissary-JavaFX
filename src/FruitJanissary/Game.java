package FruitJanissary;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Game implements Initializable
{

    // FXML değişkenleri
    @FXML
    private ImageView imagePause;

    @FXML
    private ImageView imgResume;

    @FXML
    private Text txtCountupTimer;

    @FXML
    private Text txtCuttedFruits;

    @FXML
    private Text txtScore;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView imgBackground;

    @FXML
     private Text txtHealth;

    // Genel değişkenleri tanımliyoruz.

    AudioClip backgroundSound ;
    boolean gameWorking = true,  scoreSaved = false;
    Timeline countup;
    double startX, startY, endX, endY;
    public boolean isSlice=false;
    // Bombalar Bomb classindan turetiliyor.
    Random bomb;
    Bomb bmb;

    // Timeline
    Timeline spawnLemons, spawnBananas, spawnOranges,
            spawnApples, spawnBomb;

    // Meyverler
    Lemon lem;
    Muz ban;
    Orange org;
    Elma app;



    //Genel statik değişkenler, statik metotlar, başka bir sınıftan kullanılacak .
    public static Text staticTxtScore;
    public static int highestScore;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // background ses dosyamizi cagiriyoruz . setCycleCount Methodu ile sarki bittiginde tekrar baslamasini sagliyoruz . Timeline.INDEFINITE den de bu is icin faydalaniyoruz.
        //Bu yontemi ogrenmeden once
        //backgroundSound = new AudioClip(this.getClass().getResource("/sounds/game_background_sound.mp3").toExternalForm());
        //backgroundSound.play(0.5);
        //Timeline repeatSound = new Timeline(new KeyFrame(Duration.seconds(41), e -> {
        //backgroundSound.play(0.5);
        // Yukaridaki kod satirini kullanmistik.
        backgroundSound = new AudioClip(this.getClass().getResource("/sounds/game_background_sound.mp3").toExternalForm());
        backgroundSound.setCycleCount(Timeline.INDEFINITE);
        backgroundSound.play(0.5);

        //Duration.seconds i ses dosyasinin uzunluguna gore belirledim

        Timeline replaySound = new Timeline(new KeyFrame(Duration.seconds(243), e -> {
            backgroundSound.play(0.5);
        }));

        replaySound.setCycleCount(Timeline.INDEFINITE);
        replaySound.play();

        // Zaman sayacini sifirdan baslatir .
        startCountupTimer(0);

        // Constant related to path
        Lemon.centerX = anchorPane.getPrefWidth() / 2;
        Lemon.centerY = anchorPane.getPrefHeight();
        Muz.centerX = anchorPane.getPrefWidth() / 2;
        Muz.centerY = anchorPane.getPrefHeight();
        Orange.centerX = anchorPane.getPrefWidth() / 2;
        Orange.centerY = anchorPane.getPrefHeight();
        Elma.centerX = anchorPane.getPrefWidth() / 2;
        Elma.centerY = anchorPane.getPrefHeight();
        Bomb.centerX = anchorPane.getPrefWidth() / 2;
        Bomb.centerY = anchorPane.getPrefHeight();

        // Start Game
        startGame();

        // Save the txtScore refrence
        staticTxtScore = this.txtScore;

        // If player open the game scene, then close the stage --> save his score
        FruitJanissary.primaryStage.setOnCloseRequest(e ->
            {
                try
                {
                    if (!scoreSaved)
                    {
                        // Score not saved and he closed the stage, save it in this case
                        saveScore();
                    }
                }
                catch (IOException ex)
                {
                    System.out.println("Scores.txt not found..");
                }
            }
        );
    }

    @FXML
    private void onImgPausePressed(MouseEvent event)
    {
        pauseResumeGame();
    }

    @FXML
    private void omImgResumePressed(MouseEvent event)
    {
        pauseResumeGame();
    }

    public void pauseResumeGame()
    {
        if (gameWorking)
        {
            // Pause button clicked, Make pause icon disappear, appear resume one
            gameWorking = false;
            imagePause.setVisible(false);
            imgResume.setVisible(true);

            // Pause all fruit appear, and it's current animation
            pausecountup();
            spawnLemons.pause();
            spawnBananas.pause();
            spawnOranges.pause();
            spawnApples.pause();
            spawnBomb.pause();
            backgroundSound.stop();
        }
        else
        {
            // Resume button clicked, Make resume icon disappear, appear pause one
            gameWorking = true;
            imagePause.setVisible(true);
            imgResume.setVisible(false);

            // Resume all fruit appear, and it's current animation
            resumecountup();
            spawnLemons.play();
            spawnBananas.play();
            spawnOranges.play();
            spawnApples.play();
            spawnBomb.play();
            backgroundSound.play();
        }
    }

    @FXML
    private void onImgBackPress(MouseEvent event)
    {
        openHomeScene();
    }

    public void openHomeScene()
    {
        // Make delay before opening the high score new fxml file
        Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(20), eventDelay ->
                {
                    try
                    {
                        Parent root = FXMLLoader.load(this.getClass().getResource("Home.fxml"));
                        Scene highScoreScene = new Scene(root);
                        FruitJanissary.primaryStage.setScene(highScoreScene);
                        backgroundSound.stop();
                    }
                    catch (IOException ex)
                    {
                        System.out.println("Home.fxml not found..");
                    }
                })
        );

        delay.setCycleCount(1);
        delay.play();
    }


   public void HEALTH(int health){

        if(health > 3){ Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(10), eventDelay2 ->
                {
                    try
                    {
                        AnchorPane root = FXMLLoader.load(this.getClass().getResource("GameOver.fxml"));
                        anchorPane.getChildren().add(root);
                        root.setLayoutX(280);
                        root.setLayoutY(220);

                        // Meyvelerin cikmasini , muzikleri ve kronometreyi durdurur.
                        backgroundSound.stop();
                        spawnLemons.stop();
                        spawnBananas.stop();
                        spawnOranges.stop();
                        spawnApples.stop();
                        spawnBomb.stop();
                        countup.stop();
                    }
                    catch (IOException ex)
                    {
                        System.out.println("GameOver.fxml not found..");
                    }
                })
        );

            delay.play();

            // Oyun biter ve Oyuncu skoru kaydedilir.
            try
            {
                // Get high score in the file before saving
                highestScore = getHighestScore();

                // Save his score
                saveScore();

                // Score saved on end of timer
                scoreSaved = true;
            }
            catch (IOException ex)
            {
                System.out.println("Scores.txt not found!");
            }
        }

        }



    public void startCountupTimer(int time)
    {
        // Set initial value for game count up
        txtCountupTimer.setText(Integer.toString(time));

        // Make time line to decrease the time very one second
        countup = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            // Get the current countup time
            int countupTimer = getcountupTimer();

            txtCountupTimer.setText(Integer.toString(countupTimer + 1));


        }));

        //time-1 dememizin sebebi zamani sonsuza kadar saymasi icin.
        countup.setCycleCount(time-1);
        countup.play();

    }

    public void pausecountup()
    {
        countup.pause();
    }

    public void resumecountup()
    {
        countup.play();
    }

    public int getcountupTimer()
    {
        return Integer.parseInt(txtCountupTimer.getText());
    }

    // The main game functions
    public void startGame()
    {
        imgBackground.setOnDragDetected(dragDetected -> imgBackground.startFullDrag());

        spawnLemon();
        spawnBanana();
        spawnOrange();
        spawnApple();
        spawnBomb();
    }


    public void spawnApple()
    {
        spawnApples = new Timeline(new KeyFrame(Duration.seconds(3), e ->
        {
            app = new Elma(anchorPane);
            app.imgFullApple.setVisible(false);

            app.imgFullApple.setOnMouseDragEntered(dragEntered ->
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });

            app.imgFullApple.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the red app
                app.slice(anchorPane);





                // Make slash trace
                app.createSliceTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore(+5);

                // Increase cutted fruits
                increaseCuttedFruit();
            });

            app.createFruit(anchorPane);

            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                app.imgFullApple.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));

        spawnApples.setCycleCount(Timeline.INDEFINITE);
        spawnApples.play();
    }

    public void spawnOrange()
    {



        spawnOranges = new Timeline(new KeyFrame(Duration.seconds(3.8), e ->
        {
            org = new Orange(anchorPane);
            org.imgFullOrange.setVisible(false);

            org.imgFullOrange.setFitWidth(100);
            org.imgFullOrange.setFitHeight(100);

            org.imgFullOrange.setOnMouseDragEntered(dragEntered ->
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });

            org.imgFullOrange.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the orange
                org.slice(anchorPane);

                // Make slash trace
                org.createSliceTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore(+10);

                // Increase cutted fruits
                increaseCuttedFruit();
            });

            org.createFruit(anchorPane);



            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                org.imgFullOrange.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));

        spawnOranges.setCycleCount(Timeline.INDEFINITE);
        spawnOranges.play();


        }




    public void spawnLemon()
    {
        spawnLemons = new Timeline(new KeyFrame(Duration.seconds (2.7), e ->
        {
            lem = new Lemon(anchorPane);
            lem.imgLemon.setVisible(false);
            lem.imgLemon.setFitWidth(100);
            lem.imgLemon.setFitHeight(100);

            lem.imgLemon.setOnMouseDragEntered(dragEntered ->
            {

                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });

            lem.imgLemon.setOnMouseDragExited(dragExit ->
            {

                endX = dragExit.getX();
                endY = dragExit.getY();


                lem.slice(anchorPane);


                lem.createSliceTrace(startX, startY, endX, endY, anchorPane);

                //Score artiyor.

                increaseScore(+20);

                // Increase cutted fruits
                increaseCuttedFruit();
            });

            lem.createFruit(anchorPane);

            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                lem.imgLemon.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));

        spawnLemons.setCycleCount(Timeline.INDEFINITE);
        spawnLemons.play();
    }

    public void spawnBanana()
    {
        //Muzun kac saniyede atilacagi hesaplaniyor.
        spawnBananas = new Timeline(new KeyFrame(Duration.seconds(10), e ->
        {
            ban = new Muz(anchorPane);
            ban.imgFullBanana.setVisible(false);

            ban.imgFullBanana.setFitWidth(100);
            ban.imgFullBanana.setFitHeight(150);

            ban.imgFullBanana.setOnMouseDragEntered(dragEntered ->
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });

            ban.imgFullBanana.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the banana
                ban.slice(anchorPane);

                // Make slash trace
                ban.createSliceTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore(+25);

                // Increase cutted fruits
                increaseCuttedFruit();
            });

            ban.createFruit(anchorPane);

            Timeline delay = new Timeline(new KeyFrame(Duration.millis(3), delayVisible ->
            {
                ban.imgFullBanana.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));

        spawnBananas.setCycleCount(Timeline.INDEFINITE);
        spawnBananas.play();
    }


    public void spawnBomb()
    {

        bomb = new Random();

        spawnBomb = new Timeline(new KeyFrame(Duration.seconds(6), e ->
        {
            // Based on random boolean the bomb will appear or not
            if (bomb.nextBoolean())
            {

                bmb = new Bomb(anchorPane) {};

                bmb.imgBomb.setVisible(false);

                bmb.imgBomb.setFitWidth(160);
                bmb.imgBomb.setFitHeight(140);

                // In case of bomb, just if he entered bomb will explode
                bmb.imgBomb.setOnMouseDragEntered(dragEntered ->
                {

                    // Explosion sound
                    AudioClip explosionSound = new AudioClip(this.getClass().getResource("/sounds/cut_bomb.mp3").toExternalForm());
                    explosionSound.play(10);

                    // Make it invisible
                    bmb.imgBomb.setVisible(false);


                    bmb.bombEffect(anchorPane);

                        // Here load the end game pane
                        Timeline delay = new Timeline(
                                new KeyFrame(Duration.millis(100), eventDelay2 ->
                                {
                                    try
                                    {

                                        AnchorPane root = FXMLLoader.load(this.getClass().getResource("GameOver.fxml"));
                                        anchorPane.getChildren().add(root);
                                        root.setLayoutX(0);
                                        root.setLayoutY(0);

                                        // Meyvelerin cikmasini , muzikleri ve kronometreyi durdurur.
                                        backgroundSound.stop();
                                        spawnLemons.stop();
                                        spawnBananas.stop();
                                        spawnOranges.stop();
                                        spawnApples.stop();
                                        spawnBomb.stop();
                                        countup.stop();
                                    }
                                    catch (IOException ex)
                                    {
                                        System.out.println("GameOver.fxml not found..");
                                    }
                                })
                        );

                        delay.play();

                        // Oyun biter ve Oyuncu skoru kaydedilir.
                        try
                        {
                            // Get high score in the file before saving
                            highestScore = getHighestScore();

                            // Save his score
                            saveScore();

                            // Score saved on end of timer
                            scoreSaved = true;
                        }
                        catch (IOException ex)
                        {
                            System.out.println("Scores.txt not found!");
                        }
        });

                bmb.createBomb(anchorPane);

                Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
                {
                    bmb.imgBomb.setVisible(true);
                }));

                delay.setCycleCount(1);
                delay.play();
            }
        }));

        spawnBomb.setCycleCount(Timeline.INDEFINITE);
        spawnBomb.play();
    }


    //Skora meyvelerden gelen puani ekliyor. Eklenecek puan addScore parametresiyle meylerin create metotlarindan geliyor.

    public void increaseScore(int addScore)
    {
        int currentScore = Integer.parseInt(txtScore.getText());
        txtScore.setText(Integer.toString(currentScore + addScore));


        //ELma 5 puan
        //Orange 10 puan
        //WM 20 puan
        // Muz 25 puan

        //Meyvelerin olusma surelerine gore puanlandirdik.
    }



    public void increaseCuttedFruit()
    {
        int hisCuttedFruit = Integer.parseInt(txtCuttedFruits.getText());
        txtCuttedFruits.setText(Integer.toString(hisCuttedFruit + 1));
    }

    public static void saveScore() throws IOException
    {
        // Open file to save score to
        File scoreFile = new File("C:\\Scores.txt");

        // File wrtiter is uset to append text
        FileWriter scoreFileWriter = new FileWriter(scoreFile, true);

        // If player score = 0 then don't save, exit this function
        if (Integer.parseInt(staticTxtScore.getText()) == 0)
            return;

        // " " Space will be added befroe the score number
        scoreFileWriter.write(" " + staticTxtScore.getText());
        scoreFileWriter.close();
    }

    /* Function to just return first high score in Scores.txt */
    public static int getHighestScore()
    {
        try
        {
            // Open score file
            File scoresFile = new File("C:\\Scores.txt");
            Scanner scanScoreFile = new Scanner(scoresFile);

            // Create dynamic array, we don't know how much scores in our file
            ArrayList<Integer> scores = new ArrayList();

            // Loop and store all the scores in our array
            while(scanScoreFile.hasNext())
            {
                scores.add(scanScoreFile.nextInt());
            }

            // Now we ranking our scores array - Not efficient way, if file have many scores it will make lag
            for (int i = 0; i < scores.size(); i++)
            {
                for (int j = i + 1; j < scores.size(); j++)
                {
                    if (scores.get(i) < scores.get(j))
                    {
                        // Swap the two elements
                        int temp = scores.get(i);
                        scores.set(i, scores.get(j));
                        scores.set(j, temp);
                    }
                }
            }

            // En yuksek skor Scores.txt dosyasinda tutuluyor . kayit ediliyor .
            if (scores.size() == 0)
            {
                // Eger Scores.txt bossa o zaman score array listide bos olacak ve sifir donderecektir.
                return 0;
            }
            else
            {
                return scores.get(0);
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Scores.txt not found..");
        }

        // Dosya bulunamassa bu RETURN başarısız olur .
        return -1;
    }
}