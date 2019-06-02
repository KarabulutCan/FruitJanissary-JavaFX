package FruitJanissary;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public abstract class Fruit implements Sliceable
{

  // Abstract methodlarimiz olusturuldu.

  //animasyonun linki https://www.deviantart.com/6tails6/art/Shovel-Knight-vs-Himura-Kenshin-Death-Battle-615382313
    public abstract void createSliceTrace(double startX, double startY, double endX, double endY, AnchorPane pane);
    public abstract void gravity(ImageView fruitHalf1, ImageView fruitHalf2, ImageView score);
    public abstract void createFruit(AnchorPane pane);

}



