package pong;

public class Bat {

    public int position;
    public int top;
    public int bottom;

    public Bat(int position, int top, int bottom) {
        this.position = position;
        this.top = top;
        this.bottom = bottom;
    }

    // Set up methods that can be used to get bat information
    public int getPosition() {
        return position;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

}
