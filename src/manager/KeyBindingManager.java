package manager;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class KeyBindingManager {
	private GameEngine engine;

	private InputMap inputMap;
	private ActionMap actionMap;

	public KeyBindingManager(GameEngine engine, JFrame frame) {
		this.engine = engine;

		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		this.inputMap = frame.getRootPane().getInputMap(condition);
		this.actionMap = frame.getRootPane().getActionMap();

		init();
	}

	private void init() {

		this.bindKey(KeyEvent.VK_W, "VK_W_Action", () -> {
			GameStatus status = this.engine.getGameStatus();

			if (status == GameStatus.START_SCREEN || status == GameStatus.MAP_SELECTION)
				return ButtonAction.GO_UP;
			else {
				engine.receiveInputMario(ButtonAction.M_JUMP);
				return null;
			}
		});

		this.bindKey(KeyEvent.VK_S, "VK_S_Action", () -> {
			GameStatus status = this.engine.getGameStatus();

			if (status == GameStatus.START_SCREEN || status == GameStatus.MAP_SELECTION)
				return ButtonAction.GO_DOWN;

			return null;
		});

		this.bindKey(KeyEvent.VK_D, "VK_D_Action", () -> {
			engine.receiveInputMario(ButtonAction.M_RIGHT);
			return null;
		});

		this.bindKey(KeyEvent.VK_A, "VK_A_Action", () -> {
			engine.receiveInputMario(ButtonAction.M_LEFT);
			return null;
		});

		this.bindKey(KeyEvent.VK_ENTER, "VK_ENTER_Action", () -> {
			return ButtonAction.SELECT;
		});

		this.bindKey(KeyEvent.VK_ESCAPE, "VK_ESCAPE_Action", () -> {
			GameStatus status = this.engine.getGameStatus();

			if (status == GameStatus.RUNNING || status == GameStatus.PAUSED)
				return ButtonAction.PAUSE_RESUME;
			else
				return ButtonAction.GO_TO_START_SCREEN;
		});

		this.bindKey(KeyEvent.VK_SPACE, "VK_SPACE_Action", () -> {
			engine.receiveInputMario(ButtonAction.M_FIRE);
			return null;
		});

		this.bindKey(KeyEvent.VK_UP, "VK_UP_Action", () -> {
			engine.receiveInputMario2(ButtonAction.M2_JUMP);
			return null;
		});

		this.bindKey(KeyEvent.VK_RIGHT, "VK_RIGHT_Action", () -> {
			engine.receiveInputMario2(ButtonAction.M2_RIGHT);
			return null;
		});

		this.bindKey(KeyEvent.VK_LEFT, "VK_LEFT_Action", () -> {
			engine.receiveInputMario2(ButtonAction.M2_LEFT);
			return null;
		});

		this.bindKey(KeyEvent.VK_P, "VK_P_Action", () -> {
			engine.receiveInputMario2(ButtonAction.M2_FIRE);
			return null;
		});
	}

	private void bindKey(int keyEvent, String actionName, Supplier<ButtonAction> action) {
		this.inputMap.put(KeyStroke.getKeyStroke(keyEvent, 0, false), actionName + "_Press");
		this.inputMap.put(KeyStroke.getKeyStroke(keyEvent, 0, true), actionName + "_Release");

		this.actionMap.put(actionName + "_Press", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonAction currentAction = ButtonAction.NO_ACTION;

				currentAction = action.get();

				if (currentAction != null) {
					notifyInput(currentAction);
				}
			};
		});

		this.actionMap.put(actionName + "_Release", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (keyEvent == KeyEvent.VK_D || keyEvent == KeyEvent.VK_A) {
					engine.receiveInputMario(ButtonAction.ACTION_COMPLETED);
				}

				if (keyEvent == KeyEvent.VK_RIGHT || keyEvent == KeyEvent.VK_LEFT) {
					engine.receiveInputMario2(ButtonAction.ACTION_COMPLETED2);
				}
			};
		});
	}

	private void notifyInput(ButtonAction action) {
		if (action != ButtonAction.NO_ACTION)
			engine.receiveInput(action);
	}
}
