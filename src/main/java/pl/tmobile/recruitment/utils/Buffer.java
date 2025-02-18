package pl.tmobile.recruitment.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Buffer {
    private static final Logger logger = LogManager.getLogger(Buffer.class);

        private static final ThreadLocal<Map<String, Object>> buffer = ThreadLocal.withInitial(HashMap::new);

        public static void set(String key, Object value) {
            buffer.get().put(key, value);
            logger.info("Buffer: Zapisano [" + key + "] = " + value);
        }

        public static Object get(String key) {
            Object value = buffer.get().get(key);
            logger.info("Buffer: Odczytano [" + key + "] = " + value);
            return value;
        }

        public static String getString(String key) {
            Object value = buffer.get().get(key);
            String stringValue = value != null ? value.toString() : null;
            logger.info("Buffer: Odczytano (String) [" + key + "] = " + stringValue);
            return stringValue;
        }

        public static void clear() {
            buffer.get().clear();
            logger.info("Buffer: Wyczyściliśmy wszystkie wartości.");
        }
    }
