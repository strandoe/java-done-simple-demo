package com.oysteinstrand.javadonesimple.common;

import java.util.HashMap;

public class Record<T> extends HashMap<Key, Object> {

    @SuppressWarnings("unchecked")
    public <C> C get(Key<C> key) {
        return (C) super.get(key);
    }

    public <C> Record<T> with(Key<C> key, C value) {
        Record<T> record = new Record<T>();
        for (Key iKey : keySet()) {
            record.putInternal(iKey, get(iKey));
        }
        record.putInternal(key, value);
        return record;
    }

    private <C> void putInternal(Key<C> key, C value) {
        super.put(key, value);
    }

    @Override
    public Object put(Key key, Object value) {
        throw new UnsupportedOperationException("Use with(Key, Value) instead");
    }
}
