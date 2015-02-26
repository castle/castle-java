package io.castle.client.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import io.castle.client.Castle;
import io.castle.client.exceptions.CastleException;
import io.castle.client.objects.Session;
import io.castle.client.objects.UserInfoHeader;
import io.castle.client.objects.Error;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private static final Logger logger = Logger.getLogger(HttpClient.class);

    private static final String CLIENT_AGENT_DETAILS = clientAgentDetails();

    private static final String USER_AGENT = Castle.USER_AGENT;

    private static final String UTF_8 = "UTF-8";

    private static final String APPLICATION_JSON = "application/json";

    private static String clientAgentDetails() {
	HashMap<String, String> map = Maps.newHashMap();
	ArrayList<String> propKeys = Lists.newArrayList(
		"os.arch", "os.name", "os.version",
		"user.language", "user.timezone",
		"java.class.version", "java.runtime.version", "java.version",
		"java.vm.name", "java.vm.vendor", "java.vm.version");
	for (String propKey : propKeys) {
	    map.put(propKey, System.getProperty(propKey));
	}
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	try {

	    MapperSupport.objectMapper().disable(SerializationFeature.INDENT_OUTPUT).writeValue(baos, map);
	} catch (IOException e) {
	    logger.warn(String.format("could not serialize client agent details [%s]", e.getMessage()), e);
	}
	return baos.toString();
    }

    private final ObjectMapper objectMapper;

    private final URI uri;

    private final Map<String, String> headers;

    private final HttpConnectorSupplier connection = Castle.getHttpConnectorSupplier();

    private final Base64 base64 = new Base64(1024);

    private final UserInfoHeader info;

    private Session session;

    public HttpClient(URI uri) {
	this(uri, Maps.<String, String>newHashMap());
    }

    public HttpClient(URI uri, Map<String, String> headers) {
	this.uri = uri;
	this.headers = headers;
	this.objectMapper = MapperSupport.objectMapper();
	this.info = null;
    }

    public HttpClient(URI uri, UserInfoHeader info, Session session) {
	this.uri = uri;
	this.headers = Maps.newHashMap();
	this.objectMapper = MapperSupport.objectMapper();
	this.info = info;
	if(session != null) {
	    this.session = session;
	}
    }

    public <T> T get(TypeReference<T> response) {
	HttpURLConnection conn = null;
	try {
	    conn = initializeConnection(uri, "GET");
	    return runRequest(uri, response, conn);
	} catch (IOException e) {
	    return throwLocalException(e);
	} finally {
	    IOUtils.disconnectQuietly(conn);
	}
    }

    public <T, E> T post(TypeReference<T> response, E entity) {
	headers.put("Content-Type", APPLICATION_JSON);
	HttpURLConnection conn = null;
	try {
	    conn = initializeConnection(uri, "POST");
	    prepareRequestEntity(entity, conn);
	    return runRequest(uri, response, conn);
	} catch (IOException e) {
	    return throwLocalException(e);
	} finally {
	    IOUtils.disconnectQuietly(conn);
	}
    }

    public <T, E> T put(TypeReference<T> response, E entity) {
	headers.put("Content-Type", APPLICATION_JSON);
	HttpURLConnection conn = null;
	try {
	    conn = initializeConnection(uri, "PUT");
	    prepareRequestEntity(entity, conn);
	    return runRequest(uri, response , conn);
	} catch (IOException e) {
	    return throwLocalException(e);
	} finally {
	    IOUtils.disconnectQuietly(conn);
	}
    }

    public <T> T delete(TypeReference<T> response) {
	HttpURLConnection conn = null;
	try {
	    conn = initializeConnection(uri, "DELETE");
	    return runRequest(uri, response, conn);
	} catch (IOException e) {
	    return throwLocalException(e);
	} finally {
	    IOUtils.disconnectQuietly(conn);
	}
    }

    // trick java with a dummy return
    private <T> T throwLocalException(IOException e) {
	throw new CastleException(String.format("Local exception calling [%s]. Check connectivity and settings. [%s]", uri.toASCIIString(), e.getMessage()), e);
    }

    private void prepareRequestEntity(Object entity, HttpURLConnection conn) throws IOException {
	conn.setDoOutput(true);
	OutputStream stream = null;
	try {
	    stream = conn.getOutputStream();
	    if (logger.isDebugEnabled()) {
		logger.trace(String.format("api server request --\n%s\n-- ", objectMapper.writeValueAsString(entity)));
	    }
	    objectMapper.writeValue(stream, entity);
	} finally {
	    IOUtils.closeQuietly(stream);
	}
    }

    private HttpURLConnection initializeConnection(URI uri, String method) throws IOException {
	HttpURLConnection conn = connection.connect(uri);
	conn.setRequestMethod(method);
	conn = prepareConnection(conn);
	conn = applyHeaders(conn);
	return conn;
    }

    private <T> T runRequest(URI uri, TypeReference<T> response, HttpURLConnection conn) throws IOException {
	conn.connect();
	int responseCode = conn.getResponseCode();
	if (responseCode >= 200 && responseCode < 300) {
	    return handleSuccess(response, conn, responseCode);
	} else {
	    // errors are redirects for now
	    return handleError(uri, conn, responseCode);
	}
    }

    private <T> T handleError(URI uri, HttpURLConnection conn, int responseCode) throws IOException {
	return throwException(readEntity(conn, responseCode, new TypeReference<io.castle.client.objects.Error>() {}),responseCode, uri);
    }

    private <T> T handleSuccess(TypeReference<T> response, HttpURLConnection conn, int responseCode) throws IOException {
	String updatedToken = conn.getHeaderField("X-Userbin-Set-Session-Token");
	if(updatedToken != null && this.session != null) {
	    this.session.setToken(updatedToken);
	}
	if (responseCode == 202 || responseCode == 204 || response.getType().equals(Void.class)) {
	    return null;
	} else {
	    return readEntity(conn, responseCode, response);
	}
    }
    private <T> T readEntity(HttpURLConnection conn, int responseCode, TypeReference<T> entityType) throws IOException {
	InputStream entityStream = conn.getErrorStream();
	if ((responseCode >= 200 && responseCode < 300)) {
	    entityStream = conn.getInputStream();
	}
	try {
	    if (logger.isDebugEnabled()) {
		String text = CharStreams.toString(new InputStreamReader(entityStream));
		if (responseCode == 200) {
		    logger.trace("api server response status[" + responseCode + "] --\n{" + text + "}\n-- ");
		} else {
		    logger.debug("api server response status[" + responseCode + "] --\n{" + text + "}\n-- ");
		}
		return objectMapper.readValue(text, entityType);
	    } else {
		return objectMapper.readValue(entityStream, entityType);
	    }
	} finally {
	    IOUtils.closeQuietly(entityStream);
	}
    }

    private <T> T throwException(Error error, int responseCode, URI uri) {
	/*
		400	bad_request
		401	unauthorized
		401	user_locked
		402	plan_limit
		403	forbidden
		404	not_found
		404	route_not_found
		410	resource_removed
		419	user_unauthorized
		422	invalid_parameters
		500	server_error
        */
	throw new CastleException(responseCode, error, uri);
    }

    private HttpURLConnection applyHeaders(HttpURLConnection conn) {
	for (Map.Entry<String, String> entry : createHeaders().entrySet()) {
	    conn.setRequestProperty(entry.getKey(), entry.getValue());
	}
	for (Map.Entry<String, String> entry : createAuthorizationHeaders().entrySet()) {
	    conn.setRequestProperty(entry.getKey(), entry.getValue());
	}
	return conn;
    }

    private HttpURLConnection prepareConnection(HttpURLConnection conn) {
	conn.setConnectTimeout(Castle.getConnectionTimeout());
	conn.setReadTimeout(Castle.getRequestTimeout());
	conn.setUseCaches(false);
	return conn;
    }

    private Map<String, String> createAuthorizationHeaders() {
	String authString = ":" + Castle.getSecret();
	headers.put("Authorization", "Basic " + StringUtils.newStringUtf8(base64.encode(authString.getBytes())).trim());
	return headers;
    }

    private Map<String, String> createHeaders() {
	headers.put("User-Agent", USER_AGENT);
	headers.put("X-Client-Platform-Details", CLIENT_AGENT_DETAILS);
	headers.put("Accept-Charset", UTF_8);
	headers.put("Accept", APPLICATION_JSON);
	if(Castle.isDisableTracking()) {
	    headers.put("X-Userbin-Do-Not-Track", "1");
	}
	if(this.session != null) {
	    headers.put("X-Userbin-Session-Token", this.session.getToken());
	}
	if(this.info != null) {
	    headers.put("X-Userbin-Ip", this.info.getIp());
	    headers.put("X-Userbin-User-Agent", this.info.getUserAgent());
	}
	return headers;
    }
}
