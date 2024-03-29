package es.uam.eps.dadm.jorgecifuentes.model;

import android.support.annotation.Nullable;

import java.util.List;


/**
 * Interfaz que representa un repositorio de diferentes rondas, y su acceso a ellas.
 *
 * @author Jorge Cifuentes
 */
public interface RoundRepository {

    /* Callback para la obtencion de informacion de tipo T. */
    interface RoundsCallback<T> {

        /**
         * Metodo a ejecutar despues de obtener las rondas.
         *
         * @param rounds Rondas nuevas.
         */
        void onResponse(List<T> rounds);

        /**
         * Metodo a ejecutar en caso de error.
         *
         * @param error Mensaje de error.
         */
        void onError(String error);
    }

    /**
     * Abre el repositorio.
     *
     * @throws Exception
     */
    void open() throws Exception;

    /**
     * Cierra el repositorio.
     */
    void close();

    /**
     * Callback a ejecutar despues de hacer login o registro.
     */
    interface LoginRegisterCallback {
        /**
         * Metodo a ejecutar si fue correcto.
         *
         * @param playerUuid Id del jugador.
         */
        void onLogin(String playerUuid);

        /**
         * Metodo a ejecutar en caso de error.
         *
         * @param error Mensaje de error.
         */
        void onError(String error);
    }

    /**
     * Efectua el login.
     *
     * @param playername Nombre del jugador.
     * @param password   Clave del jugador.
     * @param callback   Callback a ejecutar despues del login.
     */
    void login(String playername, String password, LoginRegisterCallback callback);

    /**
     * Efectua el registro.
     *
     * @param playername Nombre del jugador.
     * @param password   Clave del jugador.
     * @param callback   Callback a ejecutar despues del registro.
     */
    void register(String playername, String password, LoginRegisterCallback callback);

    /**
     * Callback booleano.
     */
    interface BooleanCallback {

        /**
         * Metodo de vuelta.
         *
         * @param ok Resultado de la operacion.
         */
        void onResponse(boolean ok);
    }

    /**
     * Obtiene las rondas del repositorio.
     *
     * @param playeruuid   Id del jugador.
     * @param orderByField Modo de ordenado o null.
     * @param group        Modo de agrupado o null.
     * @param callback     Callback a ejecutar despues de obtener las rondas.
     */
    void getRounds(@Nullable String playeruuid, @Nullable String orderByField, @Nullable String group, RoundsCallback<Round> callback);

    /**
     * Obtiene los scores de la base de datos.
     *
     * @param callback Callback a ejecutar una vez obtenido el triplete de scores (Title, Score1, Score2).
     */
    void getScores(RoundsCallback<Triplet<String, String, String>> callback);

    /**
     * Agrega una nueva ronda.
     *
     * @param round    Ronda a agregar.
     * @param callback Callback a ejecutar despues de agregar la ronda.
     */
    void addRound(Round round, BooleanCallback callback);

    /**
     * Borra una ronda de la base de datos.
     *
     * @param round Ronda a borrar.
     */
    void removeRound(Round round, BooleanCallback callback);

    /**
     * Actualiza una ronda.
     *
     * @param round    Ronda a actualizar.
     * @param callback Callback a ejecutar despues de actualizar la ronda.
     */
    void updateRound(Round round, BooleanCallback callback);

    /**
     * Actualiza un usuario.
     *
     * @param userUUID ID del usuario.
     * @param name     Nombre nuevo.
     * @param password Clave nueva.
     * @param callback Callback de respuesta.
     */
    void updateUser(String userUUID, String name, String password, BooleanCallback callback);

    /**
     * Envia un mensaje de texto a otro usuario.
     * @param playerId id del enviador.
     * @param playerDest nombre del destinatario.
     * @param message mensaje.
     * @param callback callback booleano.
     */
    void sendMessage(String playerId, String playerDest, String message, BooleanCallback callback);
}