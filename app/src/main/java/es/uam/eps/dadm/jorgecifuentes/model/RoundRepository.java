package es.uam.eps.dadm.jorgecifuentes.model;

/**
 * Created by jorgecf on 26/02/17.
 */

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RoundRepository {

    private final int default_rounds = 4;
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

        for (int i = 0; i < this.default_rounds; i++) {
            Round round = new Round();
            rounds.add(round);
        }
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public List<Round> getRounds() {
        return rounds;
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
}