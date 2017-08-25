package io.castle.client.internal.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class HeadNormalizerTest {

    @Test
    public void nullHeaderName() {
        //given
        String header = null;
        //when
        String normalized = HeaderNormalizer.normalize(header);
        //then
        Assert.assertNull(normalized);
    }

    @Test
    public void nullListOfHeaders() {
        //given
        List<String> header = null;
        //when
        List<String> normalized = HeaderNormalizer.normalizeList(header);
        //then
        Assert.assertNull(normalized);
    }
}
