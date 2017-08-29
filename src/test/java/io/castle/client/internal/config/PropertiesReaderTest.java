package io.castle.client.internal.config;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReaderTest {

    @Test(expected = NullPointerException.class)
    public void notAllowNullInput() throws IOException {

        //Given a provided properties object
        Properties original = new Properties();

        //When properties are loaded
        new PropertiesReader().loadPropertiesFromStream(original, null);

        //Then throw exception
    }
    @Test
    public void ignoreIOExceptionsOnReadAndReturnANewPropertiesObject() throws IOException {

        //Given a provided properties object
        Properties original = new Properties();
        //And a inputStream that throw IOException
        InputStream throwingExceptionStream = Mockito.mock(InputStream.class);
        when(throwingExceptionStream.read()).thenThrow(new IOException());
        when(throwingExceptionStream.read(argThat(new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(byte[] argument) {
                return true;
            }
        }))).thenThrow(new IOException());
        when(throwingExceptionStream.read(argThat(new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(byte[] argument) {
                return true;
            }
        }),anyInt(),anyInt())).thenThrow(new IOException());


        //When properties are loaded
        Properties loaded = new PropertiesReader().loadPropertiesFromStream(original, throwingExceptionStream);

        //Then a new empty Properties object is created
        Assertions.assertThat(loaded).isNotSameAs(original);
    }

    @Test
    public void ignoreIOExceptionsOnCloseAndReturnANewPropertiesObject() throws IOException {

        //Given a provided properties object
        Properties original = new Properties();
        //And a inputStream that throw IOException
        InputStream throwingExceptionStream = Mockito.mock(InputStream.class);
        doThrow(new IOException()).when(throwingExceptionStream).close();

        //When properties are loaded
        Properties loaded = new PropertiesReader().loadPropertiesFromStream(original, throwingExceptionStream);

        //Then a new empty Properties object is created
        Assertions.assertThat(loaded).isNotSameAs(original);
    }

}