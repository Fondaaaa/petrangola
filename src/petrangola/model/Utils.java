package petrangola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface Utils {

    public static <T> List<T> copy(List<T> list) {
        // shallow copy
        var newList = new ArrayList<T>();
        for (T item : list) {
            newList.add(item);
        }

        return newList;
    }


    public static <T> void shuffle(List<T> list) {
        Random random = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(list, i, j);
        }
    }
}
