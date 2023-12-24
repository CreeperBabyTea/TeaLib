package creeperbabytea.tealib.client.operation;

import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class HotKeyListener {
    protected static final Map<String, HotKeyListener> INSTANCE = new HashMap<>();
    final String modid;

    private HotKeyListener() {
        this("minecraft");
    }

    private HotKeyListener(String id) {
        INSTANCE.put(id, this);
        this.modid = id;
    }

    public static HotKeyListener create(String modid) {
        if (INSTANCE.containsKey(modid))
            return INSTANCE.get(modid);
        else {
            return new HotKeyListener(modid);
        }
    }

    private final Map<Integer, HotKey> keys = new HashMap<>();
    private boolean locked = false;

    private int index = 0;

    private int nextIndex() {
        return index++;
    }

    public void add(HotKey key) {
        if (!locked) {
            int index = nextIndex();
            keys.put(index, key.parent(this,index));
        }
    }

    @Nullable
    public HotKey getByIndex(int index) {
        return keys.get(index);
    }

    public void addListener(IEventBus forge) {
        locked = true;
        keys.forEach((index, key) -> {
            forge.addListener(key::onKeyPress);
        });
    }
}
