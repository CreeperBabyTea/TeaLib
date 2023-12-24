package creeperbabytea.tealib.util.math;

import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class BasicMath extends MathHelper {
    public static int avg(int... val) {
        return Arrays.stream(val).sum() / val.length;
    }

    public static long avg(long... val) {
        return Arrays.stream(val).sum() / val.length;
    }

    public static float avg(float... val) {
        double sum = 0.0;
        for (float v : val)
            sum += v;
        return (float) (sum / val.length);
    }

    public static double avg(double... val) {
        return Arrays.stream(val).sum() / val.length;
    }
}
