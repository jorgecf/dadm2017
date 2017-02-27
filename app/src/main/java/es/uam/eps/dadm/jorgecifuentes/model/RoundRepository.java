package es.uam.eps.dadm.jorgecifuentes.model;

/**
 * Created by jorgecf on 26/02/17.
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class RoundRepository {
    // private static final int SIZE = 3;
    private static RoundRepository repository; // singleton
    private List<Round> rounds;

    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }

    private RoundRepository(Context context) {
        rounds = new ArrayList<Round>();

        for (int i = 0; i < 50; i++) {
            Round round = new Round();
            rounds.add(round);
        }
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Round getRound(String id) {
        for (Round round : rounds) {
            if (round.getId().equals(id))
                return round;
        }
        return null;
    }
}