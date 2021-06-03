package io.castle.client.model;

import okhttp3.*;
import org.junit.Assert;
import org.junit.Test;

public class CastleServerErrorExceptionTest {

    @Test
    public void whenExceptionThrown() {
        Response response = new Response.Builder()
                .code(500)
                .body(ResponseBody.create(
                        MediaType.get("application/json; charset=utf-8"),
                        "{}"
                ))
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .build();
        CastleServerErrorException exception = new CastleServerErrorException(response);

        Assert.assertTrue(exception instanceof CastleServerErrorException);
        Assert.assertEquals(exception.getResponseCode(), 500);

    }
}
