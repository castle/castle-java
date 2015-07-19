package io.castle.client.objects;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import io.castle.client.http.MapperSupport;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;


public class UserInfoHeader {

    private static final Logger logger = Logger.getLogger(UserInfoHeader.class);

    private String ip;
    private String userAgent;
    private String cookieId;
    private String headers;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfoHeader)) return false;

        UserInfoHeader that = (UserInfoHeader) o;

        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (userAgent != null ? !userAgent.equals(that.userAgent) : that.userAgent != null) return false;
        if (cookieId != null ? !cookieId.equals(that.cookieId) : that.cookieId != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (cookieId != null ? cookieId.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserInfoData{" +
                "ip='" + ip + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

    public static UserInfoHeader fromRequest(HttpServletRequest request) {
        UserInfoHeader infoHeader = new UserInfoHeader();
        infoHeader.setIpFromRequest(request);
        infoHeader.setCookieIdFromRequest(request);
        infoHeader.setHeadersFromRequest(request);
        return infoHeader;
    }

    public void setIpFromRequest(HttpServletRequest request) {
        // TODO: to add support for proxy headers see info in: http://stackoverflow.com/questions/11452938/how-to-use-http-x-forwarded-for-properly
        // Example from rack: https://github.com/rack/rack/blob/master/lib/rack/request.rb#L347
        // HTTP_X_FORWARDED_FOR should be read, note that it may have multiple parts separate by commas
        setIp(request.getRemoteAddr());
    }

    public void setCookieIdFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            setCookieId(null);
            return;
        }
        Optional<Cookie> cookie = Iterators.tryFind(Iterators.forArray(request.getCookies()), new Predicate<Cookie>() {
            @Override
            public boolean apply(Cookie cookie) {
                return cookie.getName().equals("__cid");
            }
        });
        if (cookie.isPresent()) {
            setCookieId(cookie.get().getValue());
        } else {
            setCookieId(null);
        }
    }

    public void setHeadersFromRequest(HttpServletRequest request) {
        List<String> scrubHeaders = Arrays.asList("cookie");
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (scrubHeaders.contains(headerName.toLowerCase())) {
                continue;
            }
            // TODO: see if we need to normalize header names like we do in ruby
            // for now we split on '-', capitalize and join again
            // https://github.com/castle/castle-ruby/blob/master/lib/castle-rb/client.rb#L40
            String normalizeHeaderName = Joiner.on("-").join(Lists.transform(Arrays.asList(headerName.split("-")),
                    new Function<String, String>() {
                        @Override
                        public String apply(String part) {
                            return WordUtils.capitalize(part);
                        }
                    }));
            headersMap.put(normalizeHeaderName, request.getHeader(headerName));
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            MapperSupport.objectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .disable(SerializationFeature.INDENT_OUTPUT).writeValue(baos, headersMap);
            setHeaders(baos.toString());
        } catch (IOException e) {
            logger.warn(String.format("could not serialize headers [%s]", e.getMessage()), e);
        }

    }

}
