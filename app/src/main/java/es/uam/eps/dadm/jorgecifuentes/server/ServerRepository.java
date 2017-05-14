package es.uam.eps.dadm.jorgecifuentes.server;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.uam.eps.dadm.jorgecifuentes.activities.RoundPreferenceActivity;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;
import es.uam.eps.dadm.jorgecifuentes.model.Triplet;
import es.uam.eps.multij.ExcepcionJuego;

/**
 * Repositorio de rondas para servidor remoto.
 *
 * @author Jorge Cifuentes
 */
public class ServerRepository implements RoundRepository {

    private static final String DEBUG = "ServerRepository";

    private static ServerRepository repository;
    private final Context context;
    private ServerInterface is;

    public static ServerRepository getInstance(Context context) {
        if (repository == null)
            repository = new ServerRepository(context.getApplicationContext());

        return repository;
    }

    private ServerRepository(Context context) {
        this.context = context.getApplicationContext();
        this.is = ServerInterface.getServer(context);
    }


    public void loginOrRegister(final String playerName, String password, boolean register, final RoundRepository.LoginRegisterCallback callback) {

        this.is.login(playerName, password, RoundPreferenceActivity.getFirebaseToken(this.context), register,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {

                        String uuid = result.trim();
                        if (uuid.equals("-1") || uuid.length() < 10) {
                            callback.onError("Error loggin in user " + playerName);
                        } else {
                            callback.onLogin(uuid);
                            Log.d(DEBUG, "Logged in: " + result.trim());
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getLocalizedMessage());
                    }
                });
    }


    @Override
    public void open() throws Exception {
    }

    @Override
    public void close() {
    }

    @Override
    public void login(String playername, String password, LoginRegisterCallback callback) {
        loginOrRegister(playername, password, false, callback);
    }

    @Override
    public void register(String playername, String password, LoginRegisterCallback callback) {
        loginOrRegister(playername, password, true, callback);
    }

    /**
     * Convierte una lista de rondas codificada como JSon a una lista de Round.
     *
     * @param response lista en JSon.
     * @return lista de Round.
     */
    private List<Round> roundsFromJSONArray(JSONArray response) {

        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject o = response.getJSONObject(i);

                String codedboard = o.getString("codedboard");
                String uuid = o.getString("roundid");
                String date = o.getString("dateevent");
                String ops = o.getString("playernames");

                String[] players = ops.split(",");
                String op = players[0];

                Round round = new Round(op, uuid, date, RoundPreferenceActivity.getPlayerUUID(context), uuid);
                round.getBoard().stringToTablero(codedboard);

                if (players.length >= 2) {
                    round.setRivalUUID(players[1]);
                }

                rounds.add(round);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExcepcionJuego excepcionJuego) {
                excepcionJuego.printStackTrace();
                Log.d(DEBUG, "Error turning string into TableroReversi");
            }
        }

        return rounds;
    }

    @Override
    public void getRounds(@Nullable final String playeruuid, @Nullable String orderByField, @Nullable String group, final RoundsCallback<Round> callback) {

        final List<Round> roundsT = new ArrayList<>();

        // Callback de respuesta
        Response.Listener<JSONArray> responseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Round> rounds = roundsFromJSONArray(response);
                roundsT.addAll(rounds);
                Log.d(DEBUG, "Rounds downloaded from server");


                is.getActiveRounds(playeruuid, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(DEBUG, "Rounds TWO downloaded from server");
                        List<Round> rounds = roundsFromJSONArray(response);
                        roundsT.addAll(rounds);

                        // Ordenamos la lista
                        Collections.sort(roundsT, new Comparator<Round>() {
                            @Override
                            public int compare(Round o1, Round o2) {
                                return new BigInteger(o1.getRoundUUID()).compareTo(new BigInteger(o2.getRoundUUID()));
                            }
                        });


                        callback.onResponse(roundsT);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(DEBUG, "Rounds TWO ERROR!! NOT downloaded from server");
                    }
                });

            }
        };

        // Callback de error en la llamada
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error downloading rounds from server");
                Log.d(DEBUG, "Error downloading rounds from server");
            }
        };

        this.is.getOpenRounds(playeruuid, responseCallback, errorCallback);
    }

    @Override
    public void getScores(final RoundsCallback<Triplet<String, String, String>> callback) {

        final List<Triplet<String, String, String>> rounds2 = new ArrayList<>();

        RoundRepository repository = RoundRepositoryFactory.createRepository(this.context);
        RoundRepository.RoundsCallback<Round> roundsCallback = new RoundRepository.RoundsCallback<Round>() {

            @Override
            public void onResponse(List<Round> rounds) {
                Collections.sort(rounds, new Comparator<Round>() {
                    @Override
                    public int compare(Round o1, Round o2) {
                        return o2.getBlackScore() - o1.getBlackScore();
                    }
                });

                for (Round r : rounds) {
                    rounds2.add(new Triplet<>(r.getTitle(), String.valueOf(r.getBlackScore()), String.valueOf(r.getWhiteScore())));
                }

                if (rounds.size() > 0)
                    callback.onResponse(rounds2);
                else
                    callback.onError("");

            }

            @Override
            public void onError(String error) {
            }
        };


        String playeruuid = RoundPreferenceActivity.getPlayerUUID(this.context);
        repository.getRounds(playeruuid, null, null, roundsCallback);
    }

    @Override
    public void addRound(Round round, final BooleanCallback callback) {

        this.is.newRound(round.getPlayerUUID(), round.getBoard().tableroToString(),

                // Callback de respuesta
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {
                        callback.onResponse(result != "-1"); // nos devuelve roundid
                    }
                },

                // Callback de error
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onResponse(false);
                        Log.d("debug", "onErrorResponse: VolleyError addround");
                    }
                });

    }


    @Override
    public void updateRound(Round round, final BooleanCallback callback) {

    }

    @Override
    public void updateUser(String userUUID, String name, String password, BooleanCallback callback) {
        callback.onResponse(true);
    }

    @Override
    public void removeRound(Round round, BooleanCallback callback) {

    }

    @Override
    public void sendMessage(String playerId, String playerDest, String message, final BooleanCallback callback) {

        this.is.sendMsg(playerId, playerDest, message,

                // Callback de respuesta
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {
                        Log.d("debug", "onResponse: ok " + result);
                    }
                },

                // Callback de error
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("debug", "onErrorResponse: VolleyError addround");
                    }
                });
    }
}