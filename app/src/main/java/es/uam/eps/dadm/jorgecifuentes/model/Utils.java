package es.uam.eps.dadm.jorgecifuentes.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jorgecf on 5/04/17.
 */

public class Utils {


    public static <K, V> Map<K, V> inverseValueSort(Map<K, V> map) {

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
