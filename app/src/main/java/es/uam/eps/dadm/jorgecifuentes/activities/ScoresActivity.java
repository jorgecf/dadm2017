package es.uam.eps.dadm.jorgecifuentes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.uam.eps.dadm.jorgecifuentes.R;
import es.uam.eps.dadm.jorgecifuentes.model.Round;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepository;
import es.uam.eps.dadm.jorgecifuentes.model.RoundRepositoryFactory;

/**
 * Actividad que muestra en la pantalla informacion sobre las partidas alojadas en la base de datos.
 *
 * @author Jorge Cifuentes
 */
public class ScoresActivity extends AppCompatActivity {

    private static final int NUM_TOP_PLAYERS=5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_scores);


        final TextView playername = (TextView) this.findViewById(R.id.playername);
        final TextView playername_subtitle = (TextView) this.findViewById(R.id.playername_subtitle);
        final ListView top_player_list = (ListView) this.findViewById(R.id.top_player_list);


        RoundRepository repository = RoundRepositoryFactory.createRepository(this);

        RoundRepository.RoundsCallback rc = new RoundRepository.RoundsCallback() {

            @Override
            public void onResponse(List<Round> rounds) {

                Set<String> numPlayers = new HashSet<>();
                Map<String, Integer> gamesPlayed = new HashMap<>();

                for (Round r : rounds) {
                    numPlayers.add(r.getPlayerUUID());

                    String p = r.getPlayername();
                    if (gamesPlayed.containsKey(p))
                        gamesPlayed.put(p, 1 + gamesPlayed.get(p));
                    else gamesPlayed.put(p, 1);
                }


                // Ordenamos por numero de partidas jugadas descendentemente.
                gamesPlayed = inverseValueSort(gamesPlayed);

                // Metemos los datos en la ListView.
                String[] from = new String[]{"playername", "gamesPlayed"}; //TODO cte
                int[] to = new int[]{R.id.playername_item, R.id.gamesPlayed_item};

                // Cada linea de la ListView es una mapa.
                List<Map<String, String>> adapterMaps = new ArrayList<>();

                int i = 0;
                for (Map.Entry<String, Integer> e : gamesPlayed.entrySet()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("playername", "jugador " + e.getKey());
                    map.put("gamesPlayed", e.getValue() + " rounds");
                    adapterMaps.add(map);

                    if (++i == NUM_TOP_PLAYERS) break;
                }


                top_player_list.setAdapter(new SimpleAdapter(ScoresActivity.this, adapterMaps, R.layout.list_scores_item, from, to));

                // Jugador actual.
                Integer gp = gamesPlayed.get(RoundPreferenceActivity.getPlayerName(ScoresActivity.this));
                if (gp == null) gp = 0;
                playername_subtitle.setText(gp + " rounds played"); //TODO string


                Log.d("TEST", "onResponse: ");


            }

            @Override
            public void onError(String error) {
                playername_subtitle.setText("No" + " rounds played");
            }
        };


        playername.setText(RoundPreferenceActivity.getPlayerName(this));
        repository.getRounds(null, null, null, rc);

        repository.close();
    }


    private static <K, V> Map<K, V> inverseValueSort(Map<K, V> map) {

        // Comparador invertido para la lista de Entries.
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
                V v1 = ((Map.Entry<K, V>) (o2)).getValue();
                V v2 = ((Map.Entry<K, V>) (o1)).getValue();

                return ((Comparable<V>) v1).compareTo(v2);
            }
        });

        // Creacion de nuevo mapa.
        Map<K, V> ret = new LinkedHashMap<>();

        Iterator<Map.Entry<K, V>> it = list.iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> entry = it.next();
            ret.put(entry.getKey(), entry.getValue());
        }

        return ret;
    }
}