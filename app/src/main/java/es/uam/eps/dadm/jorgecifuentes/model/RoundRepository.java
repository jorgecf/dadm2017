package es.uam.eps.dadm.jorgecifuentes.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un repositorio de diferentes rondas como un singleton.
 *
 * @author Jorge Cifuentes
 */
public class RoundRepository {

    private static final int DEFAULT_ROUNDS = 4;
    public static final int SIZE = 8;
    private static RoundRepository repository;
    private List<Round> rounds;

    /**
     * Devuelve "el" RoundRepository. Solo existe uno al estar implementado como Singleton.
     *
     * @param context contexto
     * @return el repositorio creado o ya existente
     */
    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }

    /**
     * Crea un nuevo RoundRepository. Solo se llama una vez en cada ejecucion.
     *
     * @param context contexto
     */
    private RoundRepository(Context context) {
        rounds = new ArrayList<>();

        for (int i = 0; i < this.DEFAULT_ROUNDS; i++) {
            Round round = new Round(RoundRepository.SIZE);
            rounds.add(round);
        }
    }

    /**
     * Obtiene una ronda asociada al id pasado.
     *
     * @param id id de la ronda a buscar
     * @return ronda devuelta o Null
     */
    public Round getRound(String id) {

        for (Round round : rounds) {
            if (round.getId().compareTo(id) == 0) {
                return round;
            }
        }

        return null;
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public List<Round> getRounds() {
        return rounds;
    }

}