package com.example.ultramemorycore.memory;

import net.minecraft.block.properties.IProperty;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CompactPropertyState {

    private final Map<IProperty<?>, Comparable<?>> values;
    private final int compactHash;

    private CompactPropertyState(Map<IProperty<?>, Comparable<?>> values, int compactHash) {
        this.values = Collections.unmodifiableMap(values);
        this.compactHash = compactHash;
    }

    public static CompactPropertyState from(Map<IProperty<?>, Comparable<?>> map) {

        LinkedHashMap<IProperty<?>, Comparable<?>> copy = new LinkedHashMap<>(map.size());

        int hash = 1;

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : map.entrySet()) {

            IProperty<?> property = entry.getKey();
            Comparable<?> value = entry.getValue();

            copy.put(property, value);

            hash = 31 * hash + System.identityHashCode(property);
            hash = 31 * hash + value.hashCode();
        }

        return new CompactPropertyState(copy, hash);
    }

    public Map<IProperty<?>, Comparable<?>> values() {
        return values;
    }

    @Override
    public int hashCode() {
        return compactHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CompactPropertyState)) {
            return false;
        }
        CompactPropertyState other = (CompactPropertyState) obj;
        return compactHash == other.compactHash && values.equals(other.values);
    }
}
