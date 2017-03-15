package es.uam.eps.dadm.jorgecifuentes.model;

import java.util.List;

/**
 * Interfaz que representa un repositorio de diferentes rondas, y su acceso a ellas.
 *
 * @author Jorge Cifuentes
 */
public interface RoundRepository {

    //
    interface RoundsCallback {
        void onResponse(List<Round> rounds);

        void onError(String error);
    }


    void open() throws Exception;

    void close();

    //
    interface LoginRegisterCallback {
        void onLogin(String playerUuid);

        void onError(String error);
    }

    void login(String playername, String password, LoginRegisterCallback callback);

    void register(String playername, String password, LoginRegisterCallback callback);

    //
    interface BooleanCallback {
        void onResponse(boolean ok);
    }

    void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback);

    void addRound(Round round, BooleanCallback callback);

    void updateRound(Round round, BooleanCallback callback);
}


