package Utils;

import java.util.Objects;

// Represents a Point on a 2D plane, has some "point math" methods
public class Point {
    public final float x;
    public final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point point) {
        return new Point(x + point.x, y + point.y);
    }

    public Point addX(int x) {
        return new Point(this.x + x, this.y);
    }

    public Point addY(int y) {
        return new Point(this.x, this.y + y);
    }

    public Point subtract(Point point) {
        return new Point(x - point.x, y - point.y);
    }

    public Point subtractX(int x) {
        return new Point(this.x - x, this.y);
    }

    public Point subtractY(int y) {
        return new Point(this.x, this.y - y);
    }

    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    public Point toUnit() {
        double magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        return new Point((float) (x / magnitude), (float) (y / magnitude));
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
