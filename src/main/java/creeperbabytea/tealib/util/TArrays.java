package creeperbabytea.tealib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TArrays {
    public static <O> O[] append(O[]... arraysIn) {
        int size = Arrays.stream(arraysIn).mapToInt(array -> array.length).sum();
        O[] result = Arrays.copyOf(arraysIn[0], size);
        int currentIndex = arraysIn[0].length;
        for (int i = 1; i < arraysIn.length; i++) {
            System.arraycopy(arraysIn[i], 0, result, currentIndex, arraysIn[i].length);
            currentIndex += arraysIn[i].length;
        }
        return result;
    }
}
