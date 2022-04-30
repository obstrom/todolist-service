package com.obstrom.todolistservice.utility;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppUtility {

    public static <T> List<T> convertIterableToList(Iterable<T> iterable) {
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}
