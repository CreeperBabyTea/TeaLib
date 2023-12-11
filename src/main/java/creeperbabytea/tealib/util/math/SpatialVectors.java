package creeperbabytea.tealib.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

public class SpatialVectors {
    /**
     * Rotate a vector according to the axis of rotation of the entity, which is probably what it means...
     * @param in       unrotated vectors
     * @param yawRad   yaw in radian format
     * @param pitchRad pitch in radian format
     * @return Vector after rotation
     */
    public static Vector3d rotate(Vector3d in, float yawRad, float pitchRad) {
        return in.rotateRoll(pitchRad).rotateYaw(yawRad);
    }

    /** Rotate the vector according to the rotation of a given entity. */
    public static Vector3d rotate(Vector3d in, Entity entity) {
        return rotate(in, -(float) Math.toRadians(entity.getRotationYawHead() + 90), (float) Math.toRadians(entity.rotationPitch));
    }
}
