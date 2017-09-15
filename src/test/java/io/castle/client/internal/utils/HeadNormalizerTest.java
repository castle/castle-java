package io.castle.client.internal.utils;

import org.assertj.core.api.Assertions;
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

    @Test
    public void normalizationContractTest(){
        //given
        HeaderNormalizer normalizer = new HeaderNormalizer();
        //when
        //then
        Assertions.assertThat(normalizer.normalize("UPPER_TO_LOWER")).isEqualTo("upper-to-lower");
        Assertions.assertThat(normalizer.normalize("header-name_with_underscores")).isEqualTo("header-name-with-underscores");

    }
}
