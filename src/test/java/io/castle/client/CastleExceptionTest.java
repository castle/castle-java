package io.castle.client;

import io.castle.client.internal.utils.OkHttpExceptionUtil;
import io.castle.client.model.*;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class CastleExceptionTest {

    private final MediaType JsonMediaType = MediaType.parse("application/json; charset=utf-8");

    @Test(expected = CastleApiInternalServerErrorException.class)
    public void serverError() {
        //Given
        Response response = new Response.Builder()
                .code(500)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .build();

        OkHttpExceptionUtil.handle(response);
    }

    @Test
    public void timeout() {
        //Given
        IOException exception = new SocketTimeoutException("timeout");

        CastleRuntimeException castleException = OkHttpExceptionUtil.handle(exception);

        Assert.assertTrue(castleException instanceof CastleApiTimeoutException);
    }

    @Test
    public void generic() {
        //Given
        IOException exception = new IOException("generic");

        CastleRuntimeException castleException = OkHttpExceptionUtil.handle(exception);

        Assert.assertFalse(castleException instanceof CastleApiTimeoutException);
        Assert.assertFalse(castleException instanceof CastleApiInternalServerErrorException);
    }

    @Test(expected = CastleApiInvalidRequestTokenException.class)
    public void invalidRequestTokenError() {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create("{\"type\": \"invalid_request_token\"}", JsonMediaType))
                .build();

        OkHttpExceptionUtil.handle(response);
    }

    @Test(expected = CastleApiInvalidRequestTokenException.class)
    public void invalidRequestTokenCasleResponseResponseError() throws IOException {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create("{\"type\": \"invalid_request_token\"}", JsonMediaType))
                .build();

        new CastleResponse(response);
    }

    @Test(expected = CastleApiInvalidParametersException.class)
    public void invalidParametersError() {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create("{\"type\": \"unknown\"}", JsonMediaType))
                .build();

        OkHttpExceptionUtil.handle(response);
    }

    @Test(expected = CastleApiInvalidParametersException.class)
    public void closedBodyInvalidParamsError() {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create("{\"type\": \"unknown\"}", JsonMediaType))
                .build();

        // Close the response
        response.close();

        OkHttpExceptionUtil.handle(response);
    }

    @Test(expected = CastleApiInvalidParametersException.class)
    public void invalidJsonError() {
        //Given
        Response response = new Response.Builder()
                .code(422)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .body(ResponseBody.create("invalid json", JsonMediaType))
                .build();

        OkHttpExceptionUtil.handle(response);
    }

    @Test(expected = CastleApiNotFoundException.class)
    public void notFoundError() {
        //Given
        Response response = new Response.Builder()
                .code(404)
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Message")
                .build();

        OkHttpExceptionUtil.handle(response);
    }
}
