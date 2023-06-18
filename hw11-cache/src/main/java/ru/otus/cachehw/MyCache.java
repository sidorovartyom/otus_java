package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> store = new WeakHashMap<>();
    private final List<HwListener<K,V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        store.put(key, value);
        notifyListener(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        store.remove(key);
        notifyListener(key, null, "GET");
    }

    @Override
    public V get(K key) {
        V result = store.get(key);
        notifyListener(key, result, "REMOVE");
        return result;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListener(K key, V value, String action) {
        for (var listener: listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception ignored) { }
        }
    }
}
