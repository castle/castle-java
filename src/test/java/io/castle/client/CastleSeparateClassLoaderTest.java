package io.castle.client;

import org.junit.Test;

public class CastleSeparateClassLoaderTest {

    @Test(expected = IllegalStateException.class)
    public void sdkWithoutInitializationThrowAIllegalStateException() {

        //Given

        //When the sdk is called without initialization
        Castle.instance();

        //Then exception is thrown
    }

}