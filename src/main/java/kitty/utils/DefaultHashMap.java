package kitty.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultHashMap<K, V> extends HashMap<K, V> {
    private final V defaultValue;

    public DefaultHashMap(V defaultValue, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.defaultValue = defaultValue;
    }

    public DefaultHashMap(V defaultValue, int initialCapacity) {
        super(initialCapacity);
        this.defaultValue = defaultValue;
    }

    public DefaultHashMap(V defaultValue) {
        super();
        this.defaultValue = defaultValue;
    }

    public DefaultHashMap(V defaultValue, Map<? extends K, ? extends V> m) {
        super(m);
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object key) {
        return getOrDefault(key, defaultValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;
        if (!super.equals(o)) return false;
        if (!(o instanceof DefaultHashMap)) return true;
        DefaultHashMap<?, ?> that = (DefaultHashMap<?, ?>) o;
        return Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defaultValue);
    }
}
