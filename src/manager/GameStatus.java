package manager;

public enum GameStatus {
	GAME_OVER,
	PAUSED,
	RUNNING,
	START_SCREEN,
	MAP_SELECTION,
	HELP_SCREEN,
	MISSION_PASSED,
	ABOUT_SCREEN,
	GAME_SCORE;

	private static GameStatus instance;

    public static GameStatus getInstance() {
        if (instance == null) {
            instance = GameStatus.START_SCREEN; 
        }
        return instance;
    }

    public static void setInstance(GameStatus status) {
        instance = status;
    }
}
