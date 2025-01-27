package pl.tmobile.recruitment.utils;

import java.util.HashMap;
import java.util.Map;

public class Buffer {
    // Przechowuje dane w pamięci dla danego wątku testowego
    private static final ThreadLocal<Map<String, Object>> buffer = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        buffer.get().put(key, value);
        System.out.println("📌 Buffer: Zapisano [" + key + "] = " + value);
    }

    public static Object get(String key) {
        Object value = buffer.get().get(key);
        System.out.println("🔍 Buffer: Odczytano [" + key + "] = " + value);
        return value;
    }

    public static String getString(String key) {
        Object value = buffer.get().get(key);
        String stringValue = value != null ? value.toString() : null;
        System.out.println("🔍 Buffer: Odczytano (String) [" + key + "] = " + stringValue);
        return stringValue;
    }

    public static void clear() {
        buffer.get().clear();
        System.out.println("🧹 Buffer: Wyczyściliśmy wszystkie wartości.");
    }
}
