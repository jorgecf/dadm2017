package es.uam.eps.dadm.jorgecifuentes.server;

import android.support.annotation.Nullable;

import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.Triplet;

/**
 * Created by jorgecf on 19/04/17.
 */

public class ServerRepository implements RoundRepository {

    @Override
    public void open() throws Exception {

    }

    @Override
    public void close() {

    }

    @Override
    public void login(String playername, String password, LoginRegisterCallback callback) {

    }

    @Override
    public void register(String playername, String password, LoginRegisterCallback callback) {

    }

    @Override
    public void getRounds(@Nullable String playeruuid, @Nullable String orderByField, @Nullable String group, RoundsCallback<Round> callback) {

    }

    @Override
    public void getScores(RoundsCallback<Triplet<String, String, String>> callback) {

    }

    @Override
    public void addRound(Round round, BooleanCallback callback) {

    }

    @Override
    public void removeRound(Round round, BooleanCallback callback) {

    }

    @Override
    public void updateRound(Round round, BooleanCallback callback) {

    }

    @Override
    public void updateUser(String userUUID, String name, String password, BooleanCallback callback) {

    }
}
