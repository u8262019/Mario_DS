package manager;

public class Camera {

    private double x, y;
    private int frameNumber;
    private boolean shaking;

    private static Camera instance;

    private Camera() {
        this.x = 0;
        this.y = 0;
        this.frameNumber = 30;
        this.shaking = false;
    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void shakeCamera() {
        // shaking = true;
        // frameNumber = 60;
    }

    public void moveCam(double xAmount) {
        x = x + xAmount;
    }
}