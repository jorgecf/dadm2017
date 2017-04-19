package es.uam.eps.dadm.jorgecifuentes.model;

import android.content.Context;

import es.uam.eps.dadm.jorgecifuentes.activities.RoundPreferenceActivity;
import es.uam.eps.dadm.jorgecifuentes.database.RoundDataBase;

/**
 * Factoria de creacion de RoundRepository. Tiene en cuenta la preferencia de juego online para
 * decidir que Repositorio instanciar.
 *
 * @author Jorge Cifuentes
 */
public class RoundRepositoryFactory {

    private static final boolean LOCAL = true;

    public static RoundRepository createRepository(Context context) {
        RoundRepository repository;

        // Preferencia de juego online.
        Boolean online = RoundPreferenceActivity.getPlayOnline(context);

        repository = online ? ServerRepository.getInstance(context) : new RoundDataBase(context);

        try {
            repository.open();
        } catch (Exception e) {
            return null;
        }

        return repository;
    }
}