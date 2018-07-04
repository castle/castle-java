package io.castle.client;

import io.castle.client.utils.SeparateClassloaderTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class CastleSeparateClassLoaderTest {

    @Test(expected = IllegalStateException.class)
    public void sdkWithoutInitializationThrowAIllegalStateException() {

        //Given

        //When the sdk is called without initialization
        Castle.instance();

        //Then exception is throw
    }

}