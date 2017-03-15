package es.uam.eps.dadm.jorgecifuentes.model;

import android.content.Context;

import es.uam.eps.dadm.jorgecifuentes.database.RoundDataBase;

/**
 * Created by jorgecf on 15/03/17.
 */

public class RoundRepositoryFactory {
    private static final boolean LOCAL = true;

    public static RoundRepository createRepository(Context context) {
        RoundRepository repository;

        repository = LOCAL ? new RoundDataBase(context) : new RoundDataBase(context);

        try {
            repository.open();
        } catch (Exception e) {
            return null;
        }

        return repository;
    }
}

