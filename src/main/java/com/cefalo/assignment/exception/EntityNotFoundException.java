package com.cefalo.assignment.exception;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, Object... searchParamsMap) {

        super(
                EntityNotFoundException
                        .generateMessage(clazz.getSimpleName(), toMap(searchParamsMap))
        );
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " was not found for parameters " +
                searchParams;
    }

     private static Map<String, String> toMap(Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(entries[i].toString(), entries[i + 1].toString()),
                        Map::putAll);
    }

    /** In the commented out code 'key type' and 'valueType' is always String just to make it dynamic used generic which is not necessary*/
   /* private static <K, V> Map<K, V> toMap( Class<K> keyType, Class<V> valueType, String... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }*/

}
