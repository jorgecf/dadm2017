package es.uam.eps.dadm.jorgecifuentes;

/**
 * Created by jorgecf on 4/02/17.
 */

public class Coordenada {

    int x;
    int y;

    public Coordenada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordenada c = (Coordenada) o;

        if (x != c.x) return false;
        return y == c.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Coordenada{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

