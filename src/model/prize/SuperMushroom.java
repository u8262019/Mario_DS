package model.prize;

import manager.GameEngine;
import model.hero.Mario;

import model.hero.MarioForm;
import view.Animation;
import view.ImageLoader;

import java.awt.image.BufferedImage;

public class SuperMushroom extends BoostItem {

	public SuperMushroom(double x, double y, BufferedImage style) {
		super(x, y, style);
		setPoint(125);
	}

	@Override
	public void onTouch(Mario mario, GameEngine engine) {
		mario.acquirePoints(getPoint());
		ImageLoader imageLoader = new ImageLoader();

		if (!mario.getMarioForm().isSuper()) {
			BufferedImage[] leftFrames = imageLoader.getLeftFrames(MarioForm.SUPER);
			BufferedImage[] rightFrames = imageLoader.getRightFrames(MarioForm.SUPER);

			Animation animation = new Animation(leftFrames, rightFrames);
			MarioForm newForm = new MarioForm(animation, true, false, "mario");
			mario.setMarioForm(newForm);
			mario.setDimension(48, 96);

			engine.playSuperMushroom();
		}
	}

	@Override
	public void onTouch2(Mario mario2, GameEngine engine) {
		mario2.acquirePoints(getPoint());
		ImageLoader imageLoader = new ImageLoader();

		if (!mario2.getMarioForm().isSuper()) {
			BufferedImage[] leftFrames = imageLoader.getLeftFrames2(MarioForm.SUPER);
			BufferedImage[] rightFrames = imageLoader.getRightFrames2(MarioForm.SUPER);

			Animation animation = new Animation(leftFrames, rightFrames);
			MarioForm newForm = new MarioForm(animation, true, false, "mario2");
			mario2.setMarioForm(newForm);
			mario2.setDimension(48, 96);

			engine.playSuperMushroom();
		}

	}
}
