package model.brick;

import java.awt.image.BufferedImage;

import manager.GameEngine;
import model.prize.Coin;
import model.prize.Prize;

public class SurpriseBrick extends Brick{

    private Prize prize;

    public SurpriseBrick(double x, double y, BufferedImage style, Prize prize) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(false);
        this.prize = prize;
    }

    @Override
    public Prize reveal(GameEngine engine, String whichMario) {
        BufferedImage newStyle = engine.getImageLoader().loadImage("/sprite.png");
        newStyle = engine.getImageLoader().getSubImage(newStyle, 1, 2, 48, 48);

        if (prize != null) {
            prize.reveal();
        }

        if (prize instanceof Coin) {
            if (whichMario == "mario") prize.onTouch(engine.getMapManager().getMario("mario"), engine);
            else if (whichMario == "mario2") prize.onTouch2(engine.getMapManager().getMario("mario2"), engine);
            
        }

        setEmpty(true);
        setStyle(newStyle);

        Prize toReturn = this.prize;
        this.prize = null;

        return toReturn;
    }

    @Override
    public Prize getPrize(){
        return prize;
    }
}
