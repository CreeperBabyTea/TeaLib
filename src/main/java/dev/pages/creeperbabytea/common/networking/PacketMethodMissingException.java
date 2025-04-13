package dev.pages.creeperbabytea.common.networking;

public class PacketMethodMissingException extends RuntimeException{
    final Class<?> brokenPacket;

    public PacketMethodMissingException(Class<?> brokenPacket) {
        this.brokenPacket = brokenPacket;
    }
}
