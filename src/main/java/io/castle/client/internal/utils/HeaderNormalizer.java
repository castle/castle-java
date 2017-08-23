package io.castle.client.internal.utils;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class HeaderNormalizer {

    public static String normalize(String headerName) {
        if (headerName == null) {
            return null;
        }
        return headerName.toLowerCase().replaceAll("_", "-");
    }


    public static List<String> normalizeList(List<String> headers) {
        if (headers == null) {
            return null;
        }
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (Iterator<String> iterator = headers.iterator(); iterator.hasNext(); ) {
            String value = iterator.next();
            builder.add(normalize(value));
        }
        return builder.build();
    }
}
