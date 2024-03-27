package model.hero;

import manager.Camera;
import manager.GameEngine;
import view.Animation;
import model.GameObject;
import view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mario extends GameObject implements Cloneable{

	private int remainingLives;
	private int coins;
	private int points;
	private double invincibilityTimer;
	private MarioForm marioForm;
	private boolean toRight = true;
	private String whichMario;

	private ImageLoader imageLoader;

	public Mario(double x, double y, String whichMario) {
		super(x, y, null);
		setDimension(48, 48);

		remainingLives = 1;
		points = 0;
		coins = 0;
		invincibilityTimer = 0;
		this.whichMario = whichMario;

		imageLoader = new ImageLoader();
		BufferedImage[] leftFrames;
		BufferedImage[] rightFrames;

		if (whichMario == "mario") {
			leftFrames = imageLoader.getLeftFrames(MarioForm.SMALL);
			rightFrames = imageLoader.getRightFrames(MarioForm.SMALL);
		} else {
			leftFrames = imageLoader.getLeftFrames2(MarioForm.SMALL);
			rightFrames = imageLoader.getRightFrames2(MarioForm.SMALL);
		}
		
		Animation animation = new Animation(leftFrames, rightFrames);
		marioForm = new MarioForm(animation, false, false, whichMario);
		setStyle(marioForm.getCurrentStyle(toRight, false, false));
	}

	public void setWhichMario(String whichMario) {
		this.whichMario=whichMario;
	}

	public String getWhichMario() {
		return this.whichMario;
	}

	@Override
	public void draw(Graphics g) {
		boolean movingInX = (getVelX() != 0);
		boolean movingInY = (getVelY() != 0);
		setStyle(marioForm.getCurrentStyle(toRight, movingInX, movingInY));

		super.draw(g);
	}

	public void jump(GameEngine engine) {
		if (!isJumping() && !isFalling()) {
			setJumping(true);
			setVelY(10);
			engine.playJump();
		}
	}

	public void move(boolean toRight, Camera camera) {
		if (toRight) {
			setVelX(5);
		} else {
			if (camera.getX() < getX()) setVelX(-5);
		}

		this.toRight = toRight;
	}

	public boolean onTouchEnemy(GameEngine engine) {
		if (!marioForm.isSuper() && !marioForm.isFire()) {
			remainingLives--;
			engine.playMarioDies();
			return true;
		} else {
			engine.shakeCamera();
			marioForm = marioForm.onTouchEnemy(engine.getImageLoader());
			setDimension(48, 48);
			return false;
		}
	}

	public Fireball fire() {
		return marioForm.fire(toRight, getX(), getY());
	}

	public void acquireCoin() {
		coins++;
	}

	public void resetPoint() {
		points = 0;
	}

	public void acquirePoints(int point) {
		points += point;
	}

	public int getRemainingLives() {
		return remainingLives;
	}

	public void setRemainingLives(int remainingLives) {
		this.remainingLives = remainingLives;
	}

	public int getPoints() {
		return points;
	}

	public int getCoins() {
		return coins;
	}

	public MarioForm getMarioForm() {
		return marioForm;
	}

	public void setMarioForm(MarioForm marioForm) {
		this.marioForm = marioForm;
	}

	public boolean isSuper() {
		return marioForm.isSuper();
	}

	public boolean getToRight() {
		return toRight;
	}

	public void resetLocation() {
		setVelX(0);
		setVelY(0);
		setX(60);
		setJumping(false);
		setFalling(true);

		BufferedImage[] leftFrames;
		BufferedImage[] rightFrames;

		if (whichMario == "mario") {
			leftFrames = imageLoader.getLeftFrames(MarioForm.SMALL);
			rightFrames = imageLoader.getRightFrames(MarioForm.SMALL);
		} else {
			leftFrames = imageLoader.getLeftFrames2(MarioForm.SMALL);
			rightFrames = imageLoader.getRightFrames2(MarioForm.SMALL);
		}

		Animation animation = new Animation(leftFrames, rightFrames);
		marioForm = new MarioForm(animation, false, false, whichMario);
		setStyle(marioForm.getCurrentStyle(toRight, false, false));

		setDimension(48, 48);
	}

	@Override
    public Mario clone() {
        try {
            // Gọi clone() từ lớp Object để tạo bản sao của đối tượng này
            return (Mario) super.clone();
        } catch (CloneNotSupportedException e) {
            // Xử lý ngoại lệ, ví dụ: trả về null hoặc tạo mới đối tượng
            return null;
        }
    }
}
