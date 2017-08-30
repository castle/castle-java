package io.castle.client.internal.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class HeadNormalizerTest {

    @Test
    public void nullHeaderName() {
        //given
        String header = null;
        HeaderNormalizer normalizer = new HeaderNormalizer();
        //when
        String normalized = normalizer.normalize(header);
        //then
        Assert.assertNull(normalized);
    }

    @Test
    public void nullListOfHeaders() {
        //given
        List<String> header = null;
        HeaderNormalizer normalizer = new HeaderNormalizer();
        //when
        List<String> normalized = normalizer.normalizeList(header);
        //then
        Assert.assertNull(normalized);
    }
}
