package model.brick;

import java.awt.image.BufferedImage;

import model.prize.Prize;

public interface IBirckFactory {

    public Brick createBrick(String type,double x, double y, BufferedImage style,Prize prize);
    
    

}
