package modele;

import java.util.Objects;

/**
 * La classe {@code Cases} représente une case avec des coordonnées x et y.
 * Elle fournit des méthodes pour accéder et modifier les coordonnées, ainsi
 * que des méthodes pour comparer des instances de {@code Cases}.
 * 
 * @author Gaël Dierynck
 */
public class Cases {
    private int x;
    private int y;

    public Cases(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Cases cases = (Cases) o;
        return x == cases.getX() && y == cases.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
