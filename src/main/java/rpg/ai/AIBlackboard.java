package rpg.ai;

import java.util.HashMap;
import java.util.Map;

public class AIBlackboard {
    private final Map<String, Object> memory = new HashMap<>();

    public void set(String key, Object value) {
        memory.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(memory.get(key));
    }

    public boolean contains(String key) {
        return memory.containsKey(key);
    }

    public void remove(String key) {
        memory.remove(key);
    }

    public void clear() {
        memory.clear();
    }
}
