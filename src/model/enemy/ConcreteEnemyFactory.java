package model.enemy;

import java.awt.image.BufferedImage;



public class ConcreteEnemyFactory implements IEnemyFactory{
    public Enemy createEnemy(String type,double x,double y,BufferedImage style){
          switch (type) {
            case "goomba":
                return new Goomba(x,y,style);
            case "koopatroopa":
                return new KoopaTroopa(x,y,style);
           
            // Thêm các case khác cho các loại Brick khác
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

    }

}
