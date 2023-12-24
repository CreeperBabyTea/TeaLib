package creeperbabytea.tealib.client.operation;

import creeperbabytea.tealib.common.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Supplier;

public class HotKey {
    private final KeyBinding key;
    private HotKeyListener parent;
    private int index;

    private ClientPlayerEntity clientPlayer;
    private ClientWorld clientWorld;

    private TriConsumer<ClientPlayerEntity, ClientWorld, KeyPressedEventInfo> clientAction;
    private TriConsumer<ServerPlayerEntity, ServerWorld, KeyPressedEventInfo> serverAction;

    public HotKey(KeyBinding key) {
        this.key = key;
    }

    public HotKey(String desc, int code, String category) {
        this(new KeyBinding(desc, code, category));
    }

    HotKey parent(HotKeyListener parent, int index) {
        this.parent = parent;
        this.index = index;
        return this;
    }

    public KeyBinding getKey() {
        return key;
    }

    public void onKeyPress(InputEvent.KeyInputEvent event) {
        clientPlayer = Minecraft.getInstance().player;
        clientWorld = Minecraft.getInstance().world;
        KeyPressedEventInfo info = new KeyPressedEventInfo(event);
        if (this.key.isKeyDown()) {
            if (clientAction != null)
                clientAction.accept(clientPlayer, clientWorld, info);
            Networking.INSTANCE.sendToServer(new KeyPressedPack(parent.modid, index, info));
        }
    }

    public HotKey clientAction(TriConsumer<ClientPlayerEntity, ClientWorld, KeyPressedEventInfo> action) {
        this.clientAction = action;
        return this;
    }

    public HotKey serverAction(TriConsumer<ServerPlayerEntity, ServerWorld, KeyPressedEventInfo> action) {
        this.serverAction = action;
        return this;
    }

    public static class KeyPressedPack {
        private final String modid;
        private final int index;
        private final KeyPressedEventInfo info;

        public KeyPressedPack(String id, int index, KeyPressedEventInfo info) {
            this.modid = id;
            this.index = index;
            this.info = info;
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeString(modid);
            buffer.writeInt(index);
            buffer.writeInt(info.key);
            buffer.writeInt(info.scanCode);
            buffer.writeInt(info.action);
            buffer.writeInt(info.modifiers);
        }

        public static KeyPressedPack decode(PacketBuffer buffer) {
            String modid = buffer.readString();
            int index = buffer.readInt();
            int key = buffer.readInt();
            int scanCode = buffer.readInt();
            int action = buffer.readInt();
            int modifiers = buffer.readInt();
            KeyPressedEventInfo info = new KeyPressedEventInfo(key, scanCode, action, modifiers);
            return new KeyPressedPack(modid, index, info);
        }

        public void handler(Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                ServerPlayerEntity player = context.get().getSender();
                HotKey key = HotKeyListener.create(modid).getByIndex(index);
                if (player != null && key != null && key.serverAction != null)
                    key.serverAction.accept(player, (ServerWorld) player.world, info);
            });
            context.get().setPacketHandled(true);
        }
    }

    public static class KeyPressedEventInfo {
        private final int key;
        private final int scanCode;
        private final int action;
        private final int modifiers;

        private KeyPressedEventInfo(int key, int scanCode, int action, int modifiers) {
            this.key = key;
            this.scanCode = scanCode;
            this.action = action;
            this.modifiers = modifiers;
        }

        private KeyPressedEventInfo(InputEvent.KeyInputEvent event) {
            this(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers());
        }

        public int getKey() {
            return key;
        }

        public int getScanCode() {
            return scanCode;
        }

        public int getAction() {
            return action;
        }

        public int getModifiers() {
            return modifiers;
        }

        @Override
        public String toString() {
            return "KeyPressedEventInfo{" +
                    "key=" + key +
                    ", scanCode=" + scanCode +
                    ", action=" + action +
                    ", modifiers=" + modifiers +
                    '}';
        }
    }
}
