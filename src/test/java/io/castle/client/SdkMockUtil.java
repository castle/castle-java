package io.castle.client;

import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;

import java.lang.reflect.Field;

public class SdkMockUtil {

    public static void modifyInternalBackendFactory(Castle sdkInstance, RestApiFactory restApiFactoryToReplace) throws NoSuchFieldException, IllegalAccessException {
        CastleSdkInternalConfiguration internalConfiguration = sdkInstance.getInternalConfiguration();
        Field restApiFactoryField = CastleSdkInternalConfiguration.class.getDeclaredField("restApiFactory");
        restApiFactoryField.setAccessible(true);
        restApiFactoryField.set(internalConfiguration, restApiFactoryToReplace);
    }
}
