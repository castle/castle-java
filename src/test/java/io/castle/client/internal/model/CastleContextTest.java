package io.castle.client.internal.model;

import com.google.common.collect.ImmutableList;
import io.castle.client.internal.model.json.CastleGsonModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CastleContextTest {

    private CastleGsonModel model = new CastleGsonModel();

    @Test
    public void minimalContextAsJson() {

        //given
        CastleContext aContext = new CastleContext();

        //when
        String contextJson = model.getGson().toJson(aContext);

        //Then
        Assertions.assertThat(contextJson).isEqualTo("{\"active\":true,\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}}");

    }

    @Test
    public void completeContextJsonSpec() {

        //given
        CastleContext aContext = new CastleContext();
        aContext.setClientId("clientIDX");

        CastleDevice device = new CastleDevice();
        device.setId("d_id");
        device.setName("d_name");
        device.setToken("d_token");
        device.setType("d_type");
        device.setManufacturer("d_manufacturer");
        device.setModel("d_model");
        aContext.setDevice(device);

        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(ImmutableList.of(
                new CastleHeader("key1", "value1"),
                new CastleHeader("key2", "value2")
        ));
        aContext.setHeaders(headers);

        aContext.setIp("ip");
        aContext.setLocale("locale");

        CastleNetwork network = new CastleNetwork();
        network.setBluetooth(true);
        network.setCarrier("n_carrier");
        network.setCellular(true);
        network.setWifi(true);
        aContext.setNetwork(network);

        CastleLocation location = new CastleLocation();
        location.setCity("l_city");
        location.setCountry("l_country");
        location.setLatitude(10L);
        location.setLongitude(10L);
        location.setSpeed(0L);
        aContext.setLocation(location);

        CastleOS os = new CastleOS();
        os.setName("o_name");
        os.setVersion("0_version");
        aContext.setOs(os);

        CastlePage page = new CastlePage();
        page.setPath("p_path");
        page.setReferrer("p_referrer");
        page.setSearch("p_search");
        page.setTitle("p_title");
        page.setUrl("p_url");
        aContext.setPage(page);

        CastleScreen screen = new CastleScreen();
        screen.setWidth(10);
        screen.setHeight(20);
        screen.setDensity(2);
        aContext.setScreen(screen);

        CastleReferrer referrer = new CastleReferrer();
        referrer.setId("r_id");
        referrer.setType("r_type");
        aContext.setReferrer(referrer);

        aContext.setTimezone("timezone");
        aContext.setUserAgent("userAgent");
        //when
        String contextJson = model.getGson().toJson(aContext);

        //Then
        String expectedJson = "{\"active\":true," +
                "\"device\":{\"id\":\"d_id\",\"manufacturer\":\"d_manufacturer\",\"model\":\"d_model\",\"name\":\"d_name\",\"type\":\"d_type\",\"token\":\"d_token\"}," +
                "\"client_id\":\"clientIDX\"," +
                "\"page\":{\"path\":\"p_path\",\"referrer\":\"p_referrer\",\"search\":\"p_search\",\"title\":\"p_title\",\"url\":\"p_url\"}," +
                "\"referrer\":{\"id\":\"r_id\",\"type\":\"r_type\"}," +
                "\"headers\":{\"key1\":\"value1\",\"key2\":\"value2\"}," +
                "\"ip\":\"ip\"," +
                "\"library\":{\"name\":\"Castle\",\"version\":\"0.6.0-SNAPSHOT\"}," +
                "\"locale\":\"locale\"," +
                "\"location\":{\"city\":\"l_city\",\"country\":\"l_country\",\"latitude\":10,\"longitude\":10,\"speed\":0}," +
                "\"network\":{\"bluetooth\":true,\"cellular\":true,\"carrier\":\"n_carrier\",\"wifi\":true}," +
                "\"os\":{\"name\":\"o_name\",\"version\":\"0_version\"}," +
                "\"screen\":{\"width\":10,\"height\":20,\"density\":2}," +
                "\"timezone\":\"timezone\"," +
                "\"userAgent\":\"userAgent\"}";
        Assertions.assertThat(contextJson).isEqualTo(expectedJson);

        // And json to object create the same structure
        CastleContext fromJson = model.getGson().fromJson(expectedJson, CastleContext.class);
        Assertions.assertThat(fromJson).isEqualToComparingFieldByFieldRecursively(aContext);
    }

}