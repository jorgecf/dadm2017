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
    public String toString() {
        return "Coordenada{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

