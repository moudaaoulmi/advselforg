public class coordinate {
    int x, y;

    public coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distance(coordinate other){
        int otherX, otherY;
        int a, b;
        otherX = other.getX();
        otherY = other.getY();

        a = Math.abs(x - otherX);
        b = Math.abs(y - otherY);
        return Math.sqrt(a*a + b*b);
    }
}
