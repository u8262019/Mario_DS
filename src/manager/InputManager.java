package manager;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputManager implements KeyListener, MouseListener {

	private GameEngine engine;

	InputManager(GameEngine engine) {
		this.engine = engine;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		GameStatus status = engine.getGameStatus();
		ButtonAction currentAction = ButtonAction.NO_ACTION;

		if (keyCode == KeyEvent.VK_W) {
			if (status == GameStatus.START_SCREEN || status == GameStatus.MAP_SELECTION)
				currentAction = ButtonAction.GO_UP;
			else
				currentAction = ButtonAction.M_JUMP;
		}

		else if (keyCode == KeyEvent.VK_S) {
			if (status == GameStatus.START_SCREEN || status == GameStatus.MAP_SELECTION)
				currentAction = ButtonAction.GO_DOWN;
		}

		else if (keyCode == KeyEvent.VK_D) {
			currentAction = ButtonAction.M_RIGHT;
		}

		else if (keyCode == KeyEvent.VK_A) {
			currentAction = ButtonAction.M_LEFT;
		}

		else if (keyCode == KeyEvent.VK_ENTER) {
			currentAction = ButtonAction.SELECT;
		}

		else if (keyCode == KeyEvent.VK_ESCAPE) {
			if (status == GameStatus.RUNNING || status == GameStatus.PAUSED)
				currentAction = ButtonAction.PAUSE_RESUME;
			else
				currentAction = ButtonAction.GO_TO_START_SCREEN;

		}

		else if (keyCode == KeyEvent.VK_SPACE) {
			currentAction = ButtonAction.M_FIRE;
		}

		else if (keyCode == KeyEvent.VK_UP) {
			currentAction = ButtonAction.M2_JUMP;
		}

		else if (keyCode == KeyEvent.VK_RIGHT) {
			currentAction = ButtonAction.M2_RIGHT;
		}

		else if (keyCode == KeyEvent.VK_LEFT) {
			currentAction = ButtonAction.M2_LEFT;
		}

		else if (keyCode == KeyEvent.VK_0) {
			currentAction = ButtonAction.M2_FIRE;
		}

		notifyInput(currentAction);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (engine.getGameStatus() == GameStatus.MAP_SELECTION) {
			engine.selectMapViaMouse();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_D || event.getKeyCode() == KeyEvent.VK_A
				|| event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_LEFT)
			notifyInput(ButtonAction.ACTION_COMPLETED);
	}

	private void notifyInput(ButtonAction action) {
		if (action != ButtonAction.NO_ACTION)
			engine.receiveInput(action);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
