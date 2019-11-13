package pong;

public class Ball {

    public int positionX;
    public int positionY;
    public double speedX;
    public double speedY;
    public String name;

    public Ball(int positionX, int positionY, double speedX, double speedY, String name) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.name = name;
    }

    // Set up methods that can be used to get ball information
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public String getName() {
        return name;
    }

}
