```java
package com.example.ultramemorycore.memory;

import net.minecraft.state.property.Property;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chia sẻ các danh sách Property giống nhau giữa nhiều BlockState.
 */
public final class SharedPropertyMap {

    private static final ConcurrentHashMap<String, List<Property<?>>> CACHE =
            new ConcurrentHashMap<>();

    private SharedPropertyMap() {}

    public static List<Property<?>> canonicalize(List<Property<?>> properties) {
        if (properties == null || properties.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder keyBuilder = new StringBuilder();

        for (Property<?> property : properties) {
            keyBuilder.append(property.getName())
                    .append(':')
                    .append(property.getType().getName())
                    .append(';');
        }

        String key = keyBuilder.toString();

        return CACHE.computeIfAbsent(key, k -> List.copyOf(properties));
    }

    public static int size() {
        return CACHE.size();
    }

    public static void clear() {
        CACHE.clear();
    }
}
```
