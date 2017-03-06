package es.uam.eps.dadm.jorgecifuentes.model;

/**
 * Created by jorgecf on 26/02/17.
 */

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RoundRepository {

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

        for (int i = 0; i < 4; i++) {
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

    public Round getRound(String id) {

        for (Round round : rounds) {

            if (round.getId().compareTo(id) == 0) {
                return round;
            }
        }


        return null;
    }
}