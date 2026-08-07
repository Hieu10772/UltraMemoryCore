package com.example.ultramemorycore.memory;

import net.minecraft.state.property.Property;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CompactPropertyState {

    private final Map<Property<?>, Comparable<?>> values;
    private final int compactHash;

    private CompactPropertyState(Map<Property<?>, Comparable<?>> values, int compactHash) {
        this.values = Collections.unmodifiableMap(values);
        this.compactHash = compactHash;
    }

    public static CompactPropertyState from(Map<Property<?>, Comparable<?>> map) {

        LinkedHashMap<Property<?>, Comparable<?>> copy = new LinkedHashMap<>(map.size());

        int hash = 1;

        for (Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {

            Property<?> property = entry.getKey();
            Comparable<?> value = entry.getValue();

            copy.put(property, value);

            hash = 31 * hash + System.identityHashCode(property);
            hash = 31 * hash + value.hashCode();
        }

        return new CompactPropertyState(copy, hash);
    }

    public Map<Property<?>, Comparable<?>> values() {
        return values;
    }

    @Override
    public int hashCode() {
        return compactHash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompactPropertyState other
                && compactHash == other.compactHash
                && values.equals(other.values);
    }
}
