package es.uam.eps.dadm.jorgecifuentes;

/**
 * Clase que representa una coordenada en el eje cartesiano para el reversi. Es decir,
 * las x van de (1 a 8, representadas como A a H) y las y de (1 a 8).
 *
 * @author Jorge Cifuentes
 */
public class CoordenadaReversi {

    private int x;
    private int y;

    public CoordenadaReversi(int x, int y) {
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

        CoordenadaReversi c = (CoordenadaReversi) o;

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
        return "Coordenada {" +
                (char) (x + 'A') +
                ", " + (y + 1) +
                '}';
    }
}

