package model.prize;

import manager.GameEngine;
import model.hero.Mario;

import model.hero.MarioForm;
import view.Animation;
import view.ImageLoader;

import java.awt.image.BufferedImage;

public class FireFlower extends BoostItem {

	public FireFlower(double x, double y, BufferedImage style) {
		super(x, y, style);
		setPoint(150);
	}

	@Override
	public void onTouch(Mario mario, GameEngine engine) {
		mario.acquirePoints(getPoint());
		ImageLoader imageLoader = new ImageLoader();

		if (!mario.getMarioForm().isFire()) {
			BufferedImage[] leftFrames = imageLoader.getLeftFrames(MarioForm.FIRE);
			BufferedImage[] rightFrames = imageLoader.getRightFrames(MarioForm.FIRE);

			Animation animation = new Animation(leftFrames, rightFrames);
			MarioForm newForm = new MarioForm(animation, true, true, "mario");
			mario.setMarioForm(newForm);
			mario.setDimension(48, 96);

			engine.playFireFlower();
		}
	}

	@Override
	public void updateLocation() {
	}

	@Override
	public void onTouch2(Mario mario2, GameEngine engine) {
		mario2.acquirePoints(getPoint());
		ImageLoader imageLoader = new ImageLoader();

		if (!mario2.getMarioForm().isFire()) {
			BufferedImage[] leftFrames = imageLoader.getLeftFrames2(MarioForm.FIRE);
			BufferedImage[] rightFrames = imageLoader.getRightFrames2(MarioForm.FIRE);

			Animation animation = new Animation(leftFrames, rightFrames);
			MarioForm newForm = new MarioForm(animation, true, true, "mario2");
			mario2.setMarioForm(newForm);
			mario2.setDimension(48, 96);

			engine.playFireFlower();
		}
	}
}
