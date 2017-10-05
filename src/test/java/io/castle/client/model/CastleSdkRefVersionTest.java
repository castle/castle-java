package io.castle.client.model;


import org.junit.Assert;
import org.junit.Test;
import java.util.regex.Pattern;


public class CastleSdkRefVersionTest {

    @Test
    public void useCorrectSdkVersionInRef() {
        CastleSdkRef version = new CastleSdkRef();

        //Numeric versions with the possibility of SNAPSHOT versions.
        Assert.assertTrue(Pattern.matches("\\d+(\\.\\d+)+(-SNAPSHOT)?", version.getVersion()));
    }

}
