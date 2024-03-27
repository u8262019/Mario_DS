package model.hero;

import model.GameObject;

import java.awt.image.BufferedImage;

public class Fireball extends GameObject {
	public String whichMario;

	public Fireball(double x, double y, BufferedImage style, boolean toRight, String whichMario) {
		super(x, y, style);
		setDimension(24, 24);
		setFalling(false);
		setJumping(false);
		setVelX(10);

		this.whichMario = whichMario;

		if (!toRight) setVelX(-5);
	}
}
