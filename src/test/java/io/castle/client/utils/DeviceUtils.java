package io.castle.client.utils;

import io.castle.client.model.CastleUserDevice;
import io.castle.client.model.CastleUserDeviceContext;
import io.castle.client.model.CastleUserDevices;
import io.castle.client.model.DeviceUserAgent;

import java.util.Collections;

public class DeviceUtils {

    public static final String DEVICE_TOKEN = "abcdefg12345";
    public static final double DEVICE_RISK = 0.000154;
    public static final String DEVICE_CREATED_AT = "2018-06-15T16:36:22.916Z";
    public static final String DEVICE_LAST_SEEN_AT = "2018-07-19T23:09:29.681Z";
    public static final String DEVICE_APPROVED_AT = null;
    public static final String DEVICE_ESCALATED_AT = null;
    public static final String DEVICE_MITIGATED_AT = null;

    public static final String CONTEXT_TYPE = "desktop";
    public static final String CONTEXT_IP = "1.1.1.1";

    public static final String USER_AGENT_FAMILY = "Opera";
    public static final String USER_AGENT_DEVICE = "Unknown";
    public static final String USER_AGENT_PLATFORM = "Mac OS X";
    public static final String USER_AGENT_OS = "Mac OS X 10.13.6";
    public static final String USER_AGENT_VERSION = "54.0.2952";
    public static final String USER_AGENT_BROWSER = "Opera";
    public static final String USER_AGENT_RAW = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.51";
    public static final boolean USER_AGENT_MOBILE = false;

    public static CastleUserDevice createExpectedDevice() {
        CastleUserDevice device = new CastleUserDevice();
        device.setToken(DEVICE_TOKEN);
        device.setRisk(DEVICE_RISK);
        device.setCreatedAt(DEVICE_CREATED_AT);
        device.setLastSeenAt(DEVICE_LAST_SEEN_AT);
        device.setApprovedAt(null);
        device.setEscalatedAt(null);
        device.setMitigatedAt(null);

        CastleUserDeviceContext deviceContext = new CastleUserDeviceContext();
        deviceContext.setIp(CONTEXT_IP);
        deviceContext.setType(CONTEXT_TYPE);

        DeviceUserAgent userAgent = new DeviceUserAgent();
        userAgent.setFamily(USER_AGENT_FAMILY);
        userAgent.setDevice(USER_AGENT_DEVICE);
        userAgent.setPlatform(USER_AGENT_PLATFORM);
        userAgent.setOs(USER_AGENT_OS);
        userAgent.setVersion(USER_AGENT_VERSION);
        userAgent.setBrowser(USER_AGENT_BROWSER);
        userAgent.setRaw(USER_AGENT_RAW);
        userAgent.setMobile(USER_AGENT_MOBILE);

        deviceContext.setUserAgent(userAgent);

        device.setContext(deviceContext);

        return device;
    }

    public static CastleUserDevices createExpectedDevices() {
        CastleUserDevices devices = new CastleUserDevices();
        devices.setDevices(Collections.singletonList(createExpectedDevice()));
        return devices;
    }

    public static String getDefaultDeviceJSON() {
        return "{\"token\":\"abcdefg12345\",\"risk\":0.000154,\"created_at\":\"2018-06-15T16:36:22.916Z\",\"last_seen_at\":\"2018-07-19T23:09:29.681Z\",\"approved_at\":null,\"escalated_at\":null,\"mitigated_at\":null,\"context\":{\"ip\":\"1.1.1.1\",\"location\":{\"country_code\":\"US\",\"country\":\"United States\",\"region\":\"New Jersey\",\"region_code\":\"NJ\",\"city\":\"Lyndhurst\",\"lat\":40.7923,\"lon\":-74.1001},\"user_agent\":{\"raw\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.51\",\"browser\":\"Opera\",\"version\":\"54.0.2952\",\"os\":\"Mac OS X 10.13.6\",\"mobile\":false,\"platform\":\"Mac OS X\",\"device\":\"Unknown\",\"family\":\"Opera\"},\"type\":\"desktop\"}}";
    }
}
