package creeperbabytea.tealib.util.math;

import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class BasicMath extends MathHelper {
    public static int avg(int... val) {
        return Arrays.stream(val).sum() / val.length;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(avg(114, 514));
    }
}
