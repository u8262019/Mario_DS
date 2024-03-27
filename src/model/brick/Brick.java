package model.brick;

import manager.GameEngine;
import model.GameObject;
import model.Map;
import model.hero.Mario;

import model.prize.Prize;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObject {
	private boolean breakable;
	private boolean empty;

	public Brick(double x, double y, BufferedImage style) {
		super(x, y, style);
		setDimension(48, 48);
	}
	

	public boolean isBreakable() {
		return breakable;
	}

	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Prize reveal(GameEngine engine, String whichMario) {
		return null;
	}

	public void breakBrick(GameEngine engine, Mario mario) {
		return;
	}

	public void breakBrick2(GameEngine engine, Mario mario2) {
		return;
	}

	public Prize getPrize() {
		return null;
	}
}
