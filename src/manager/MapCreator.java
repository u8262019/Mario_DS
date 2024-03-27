package manager;

import model.EndFlag;
import model.brick.*;
import model.prize.*;
import view.ImageLoader;
import model.Map;
import model.enemy.ConcreteEnemyFactory;
import model.enemy.Enemy;
import model.enemy.Goomba;
import model.enemy.IEnemyFactory;
import model.enemy.KoopaTroopa;
import model.hero.Mario;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

class MapCreator {

	private ImageLoader imageLoader;
	private Random random;

	private BufferedImage backgroundImage;
	private BufferedImage superMushroom, oneUpMushroom, fireFlower, coin;
	private BufferedImage ordinaryBrick, surpriseBrick, groundBrick, pipe;
	private BufferedImage goombaLeft, goombaRight, koopaLeft, koopaRight, endFlag;
	private IBirckFactory brickfatory= new ConcreteBrickFactory();
	private IEnemyFactory enemyfactory= new ConcreteEnemyFactory();
	MapCreator(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		this.random = new Random();

		BufferedImage sprite = imageLoader.loadImage("/sprite.png");

		this.backgroundImage = imageLoader.loadImage("/background.png");
		this.superMushroom = imageLoader.getSubImage(sprite, 2, 5, 48, 48);
		this.oneUpMushroom = imageLoader.getSubImage(sprite, 3, 5, 48, 48);
		this.fireFlower = imageLoader.getSubImage(sprite, 4, 5, 48, 48);
		this.coin = imageLoader.getSubImage(sprite, 1, 5, 48, 48);
		this.ordinaryBrick = imageLoader.getSubImage(sprite, 1, 1, 48, 48);//cục gạch phá đc
		this.surpriseBrick = imageLoader.getSubImage(sprite, 2, 1, 48, 48);
		this.groundBrick = imageLoader.getSubImage(sprite, 2, 2, 48, 48);//bậc thang
		this.pipe = imageLoader.getSubImage(sprite, 3, 1, 96, 96);//ống
		this.goombaLeft = imageLoader.getSubImage(sprite, 2, 4, 48, 48);
		this.goombaRight = imageLoader.getSubImage(sprite, 5, 4, 48, 48);
		this.koopaLeft = imageLoader.getSubImage(sprite, 1, 3, 48, 64);
		this.koopaRight = imageLoader.getSubImage(sprite, 4, 3, 48, 64);
		this.endFlag = imageLoader.getSubImage(sprite, 5, 1, 48, 48);
	}

	Map createMap(String mapPath, double timeLimit) {
		BufferedImage mapImage = imageLoader.loadImage(mapPath);

		if (mapImage == null) {
			System.out.println("Given path is invalid...");
			return null;
		}

		Map createdMap = new Map(timeLimit, backgroundImage);
		String[] paths = mapPath.split("/");
		createdMap.setPath(paths[paths.length - 1]);

		int pixelMultiplier = 48;

		int mario = new Color(160, 160, 160).getRGB();
		int mario2 = new Color(169, 54, 179).getRGB();
		int ordinaryBrick = new Color(0, 0, 255).getRGB();
		int surpriseBrick = new Color(255, 255, 0).getRGB();
		int groundBrick = new Color(255, 0, 0).getRGB();
		int pipe = new Color(0, 255, 0).getRGB();
		int goomba = new Color(0, 255, 255).getRGB();
		int koopa = new Color(255, 0, 255).getRGB();
		int end = new Color(160, 0, 160).getRGB();

		for (int x = 0; x < mapImage.getWidth(); x++) {
			for (int y = 0; y < mapImage.getHeight(); y++) {

				int currentPixel = mapImage.getRGB(x, y);
				int xLocation = x * pixelMultiplier;
				int yLocation = y * pixelMultiplier;

				if (currentPixel == ordinaryBrick) {
					Brick brick = brickfatory.createBrick("ordinary",xLocation, yLocation, this.ordinaryBrick,null);
					createdMap.addBrick(brick);
				} else if (currentPixel == surpriseBrick) {
					Prize prize = generateRandomPrize(xLocation, yLocation);
					Brick brick = brickfatory.createBrick("surprise",xLocation, yLocation, this.surpriseBrick, prize);
					createdMap.addBrick(brick);
				} else if (currentPixel == pipe) {
					Brick brick = brickfatory.createBrick("pipe",xLocation, yLocation, this.pipe,null);
					createdMap.addGroundBrick(brick);
				} else if (currentPixel == groundBrick) {
					Brick brick = brickfatory.createBrick("ground",xLocation, yLocation, this.groundBrick,null);
					createdMap.addGroundBrick(brick);
				} else if (currentPixel == goomba) {
					Enemy enemy = enemyfactory.createEnemy("goomba",xLocation, yLocation, this.goombaLeft);
					((Goomba) enemy).setRightImage(goombaRight);
					createdMap.addEnemy(enemy);
				} else if (currentPixel == koopa) {
					Enemy enemy =  enemyfactory.createEnemy("koopatroopa",xLocation, yLocation, this.koopaLeft);
					((KoopaTroopa) enemy).setRightImage(koopaRight);
					createdMap.addEnemy(enemy);
				} else if (currentPixel == mario) {
					Mario marioObject = new Mario(xLocation, yLocation,"mario");
					createdMap.setMario(marioObject,"mario");

				} else if (currentPixel == mario2) {
					Mario mario2Object = new Mario(xLocation, yLocation,"mario2");
					createdMap.setMario(mario2Object,"mario2");
				} else if (currentPixel == end) {
					EndFlag endPoint = new EndFlag(xLocation + 24, yLocation, endFlag);
					createdMap.setEndPoint(endPoint);
				}
			}
		}

		System.out.println("Map is created..");
		return createdMap;
	}

	private Prize generateRandomPrize(double x, double y) {
		Prize generated;
		int random = this.random.nextInt(6);

		if (random == 0) { // super mushroom
			generated = new SuperMushroom(x, y, this.superMushroom);
		} else if (random == 1) { // fire flower
			generated = new FireFlower(x, y, this.fireFlower);
		} else if (random == 2) { // one up mushroom
			generated = new OneUpMushroom(x, y, this.oneUpMushroom);
		} else { // coin
			generated = new Coin(x, y, this.coin, 50);
		}

		return generated;
	}

}
