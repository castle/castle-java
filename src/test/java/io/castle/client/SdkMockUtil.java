package io.castle.client;

import io.castle.client.internal.backend.RestApiFactory;
import io.castle.client.internal.config.CastleSdkInternalConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SdkMockUtil {

    public static void modifyInternalBackendFactory(Castle sdkInstance, RestApiFactory restApiFactoryToReplace) throws NoSuchFieldException, IllegalAccessException {
        CastleSdkInternalConfiguration internalConfiguration = sdkInstance.getInternalConfiguration();
        Field restApiFactoryField = CastleSdkInternalConfiguration.class.getDeclaredField("restApiFactory");
        restApiFactoryField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(restApiFactoryField, restApiFactoryField.getModifiers() & ~Modifier.FINAL);
        restApiFactoryField.set(internalConfiguration, restApiFactoryToReplace);
    }
}
