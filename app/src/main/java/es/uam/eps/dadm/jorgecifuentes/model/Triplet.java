package es.uam.eps.dadm.jorgecifuentes.model;

/**
 * Triplete de datos interrelacionados.
 *
 * @param <F> Tipo del primer dato.
 * @param <S> Tipo del segundo dato.
 * @param <T> Tipo del tercer dato.
 * @author Jorge Cifuentes
 */
public class Triplet<F, S, T> {

    private final F first;
    private final S second;
    private final T third;

    public Triplet(F f, S s, T t) {
        this.first = f;
        this.second = s;
        this.third = t;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }
}