package model.brick;

import java.awt.image.BufferedImage;

import model.prize.Prize;

public class ConcreteBrickFactory implements IBirckFactory{
    public Brick createBrick(String type,double x, double y, BufferedImage style,Prize prize) {
        switch (type) {
            case "ground":
                return new GroundBrick(x,y,style);
            case "surprise":
                return new SurpriseBrick(x,y,style,prize);
            case "ordinary":
                return new OrdinaryBrick(x, y, style);
            case "pipe":
                return new Pipe(x,y,style);
            // Thêm các case khác cho các loại Brick khác
            default:
                throw new IllegalArgumentException("Unknown brick type: " + type);
        }
    }

}
