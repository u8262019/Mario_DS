package manager;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import model.hero.Mario;

import view.ImageLoader;
import view.StartScreenSelection;
import view.UIManager;

public class GameEngine implements Runnable {
	public Dimension screenSize;

	private MapManager mapManager;
	private UIManager uiManager;
	private SoundManager soundManager;
	private GameStatus gameStatus;
	private boolean isRunning;
	private Camera camera;
	private ImageLoader imageLoader;
	private Thread thread;
	private StartScreenSelection startScreenSelection = StartScreenSelection.START_GAME;
	private int selectedMap = 0;

	private GameEngine() {
		init();
	}

	private void init() {
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

		imageLoader = new ImageLoader();
		// InputManager inputManager = new InputManager(this);
		gameStatus = GameStatus.getInstance();
		camera = Camera.getInstance();
		uiManager = UIManager.getInstance(this, screenWidth, screenHeight);
		soundManager = SoundManager.getInstance();
		mapManager = MapManager.getInstance();

		JFrame frame = new JFrame("Super Mario Bros.");
		frame.add(uiManager);

		// frame.addKeyListener(inputManager);
		// frame.addMouseListener(inputManager);

		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		KeyBindingManager inputManager = new KeyBindingManager(this, frame);

		start();
	}

	private synchronized void start() {
		if (isRunning) return;

		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	private void reset() {
		resetCamera();
		setGameStatus(GameStatus.START_SCREEN);
	}

	public void resetCamera() {
		camera = Camera.getInstance();
		soundManager.restartBackground();
	}

	public void selectMapViaMouse() {
		String path = uiManager.selectMapViaMouse(uiManager.getMousePosition());
		if (path != null) {
			createMap(path);
		}
	}

	public void selectMapViaKeyboard() {
		String path = uiManager.selectMapViaKeyboard(selectedMap);
		if (path != null) {
			createMap(path);
		}
	}

	public void changeSelectedMap(boolean up) {
		selectedMap = uiManager.changeSelectedMap(selectedMap, up);
	}

	private void createMap(String path) {
		boolean loaded = mapManager.createMap(imageLoader, path);
		if (loaded) {
			setGameStatus(GameStatus.RUNNING);
			soundManager.restartBackground();
		}

		else
			setGameStatus(GameStatus.START_SCREEN);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();

		while (isRunning && !thread.isInterrupted()) {

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1) {
				if (gameStatus == GameStatus.RUNNING) gameLoop();
				delta--;
			}
			render();

			if (gameStatus != GameStatus.RUNNING) {
				timer = System.currentTimeMillis();
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				mapManager.updateTime();
			}
		}
	}

	private void render() {
		uiManager.repaint();
	}

	private void gameLoop() {
		updateLocations();
		checkCollisions();
		updateCamera();

		if (isGameOver()) {
			setGameStatus(GameStatus.GAME_OVER);
		}

		int missionPassed = passMission();
		if (missionPassed > -1) {
			mapManager.acquirePoints(missionPassed,"mario");
			setGameStatus(GameStatus.MISSION_PASSED);
		} else if (mapManager.endLevel()) {
			setGameStatus(GameStatus.MISSION_PASSED);
		}
	}

	private void updateCamera() {
		Mario mario = mapManager.getMario("mario");
		Mario mario2 = mapManager.getMario("mario2");

		double marioX = mario.getX();
		double mario2X = mario2.getX();

		double minX = Math.min(marioX, mario2X);
		double maxX = Math.max(marioX, mario2X);
		double midpoint = (maxX + minX) / 2;

		double cameraX = camera.getX();
		double cameraXMax = cameraX + this.screenSize.width - 48;
		double midCam = (cameraX + cameraXMax) / 2;

		// Testing purpose
		// System.out.println(
		// 	"Midcam: " + midCam + " Camera X (Max): " + cameraXMax + " Camera X: " + cameraX + 
		// 	" Midpoint: " + midpoint + " Mario X: " + marioX + " Mario2 X: " + mario2X
		// );

		camera.moveCam(midpoint - midCam);

		// Keep camera X bounded
		camera.setX(Math.max(0, camera.getX()));
	}

	private void updateLocations() {
		mapManager.updateLocations();
	}

	private void checkCollisions() {
		mapManager.checkCollisions(this);
	}

	public void receiveInputMario(ButtonAction input) {
		if (gameStatus != GameStatus.RUNNING)
			return;

		Mario mario = mapManager.getMario("mario");

		if (input == ButtonAction.M_JUMP) {
			mario.jump(this);
		} else if (input == ButtonAction.M_RIGHT) {
			mario.move(true, camera);
		} else if (input == ButtonAction.M_LEFT) {
			mario.move(false, camera);
		} else if (input == ButtonAction.ACTION_COMPLETED) {
			mario.setVelX(0);
		} else if (input == ButtonAction.M_FIRE) {
			mapManager.fire(this);
		}
	}

	public void receiveInputMario2(ButtonAction input) {
		if (gameStatus != GameStatus.RUNNING)
			return;

		Mario mario2 = mapManager.getMario("mario2");

		if (input == ButtonAction.M2_JUMP) {
			mario2.jump(this);
		} else if (input == ButtonAction.M2_RIGHT) {
			mario2.move(true, camera);
		} else if (input == ButtonAction.M2_LEFT) {
			mario2.move(false, camera);
		} else if (input == ButtonAction.ACTION_COMPLETED2) {
			mario2.setVelX(0);
		} else if (input == ButtonAction.M2_FIRE) {
			mapManager.fire2(this);
		}
	}

	public void receiveInput(ButtonAction input) {
		if (gameStatus == GameStatus.START_SCREEN) {
			if (input == ButtonAction.SELECT && startScreenSelection == StartScreenSelection.START_GAME) {
				startGame();
			} else if (input == ButtonAction.SELECT && startScreenSelection == StartScreenSelection.VIEW_ABOUT) {
				setGameStatus(GameStatus.ABOUT_SCREEN);
			} else if (input == ButtonAction.SELECT && startScreenSelection == StartScreenSelection.VIEW_HELP) {
				setGameStatus(GameStatus.HELP_SCREEN);
			} else if (input == ButtonAction.GO_UP) {
				selectOption(true);
			} else if (input == ButtonAction.GO_DOWN) {
				selectOption(false);
			}
		} else if (gameStatus == GameStatus.MAP_SELECTION) {
			if (input == ButtonAction.SELECT) {
				selectMapViaKeyboard();
			} else if (input == ButtonAction.GO_UP) {
				changeSelectedMap(true);
			} else if (input == ButtonAction.GO_DOWN) {
				changeSelectedMap(false);
			}
		} else if (gameStatus == GameStatus.RUNNING) {
			if (input == ButtonAction.PAUSE_RESUME) {
				pauseGame();
			}
		} else if (gameStatus == GameStatus.PAUSED) {
			if (input == ButtonAction.PAUSE_RESUME) {
				pauseGame();
			}

		} else if (gameStatus == GameStatus.GAME_OVER && input == ButtonAction.GO_TO_START_SCREEN) {
			reset();
		} else if (gameStatus == GameStatus.MISSION_PASSED && input == ButtonAction.GO_TO_START_SCREEN) {
			reset();
		}

		if (input == ButtonAction.GO_TO_START_SCREEN) {
			setGameStatus(GameStatus.START_SCREEN);
		}
	}

	private void selectOption(boolean selectUp) {
		startScreenSelection = startScreenSelection.select(selectUp);
	}

	private void startGame() {
		if (gameStatus != GameStatus.GAME_OVER) {
			setGameStatus(GameStatus.MAP_SELECTION);
		}
	}

	private void pauseGame() {
		if (gameStatus == GameStatus.RUNNING) {
			setGameStatus(GameStatus.PAUSED);
			soundManager.pauseBackground();
		} else if (gameStatus == GameStatus.PAUSED) {
			setGameStatus(GameStatus.RUNNING);
			soundManager.resumeBackground();
		}
	}

	public void shakeCamera() {
		camera.shakeCamera();
	}

	private boolean isGameOver() {
		if (gameStatus == GameStatus.RUNNING)
			return mapManager.isGameOver();
		return false;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public StartScreenSelection getStartScreenSelection() {
		return startScreenSelection;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public int getScore() {
		return mapManager.getScore("mario");
	}

	public int getScore2() {
		return mapManager.getScore("mario2");
	}

	public int getRemainingLives() {
		return mapManager.getRemainingLives("mario") + mapManager.getRemainingLives("mario2");
	}

	public int getCoins() {
		return mapManager.getCoins("mario") + mapManager.getCoins("mario2");
	}

	public int getSelectedMap() {
		return selectedMap;
	}

	public void drawMap(Graphics2D g2) {
		mapManager.drawMap(g2);
	}

	public Point getCameraLocation() {
		return new Point((int) camera.getX(), (int) camera.getY());
	}

	private int passMission() {
		return mapManager.passMission();
	}

	public void playCoin() {
		soundManager.playCoin();
	}

	public void playOneUp() {
		soundManager.playOneUp();
	}

	public void playSuperMushroom() {
		soundManager.playSuperMushroom();
	}

	public void playMarioDies() {
		soundManager.playMarioDies();
	}

	public void playJump() {
		soundManager.playJump();
	}

	public void playFireFlower() {
		soundManager.playFireFlower();
	}

	public void playFireball() {
		soundManager.playFireball();
	}

	public void playStomp() {
		soundManager.playStomp();
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public static void main(String... args) {
		new GameEngine();
	}

	public int getRemainingTime() {
		return mapManager.getRemainingTime();
	}
}
