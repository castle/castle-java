package io.castle.client.model;

import com.google.common.collect.ImmutableList;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.utils.SDKVersion;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Comparator;
import java.util.List;

public class CastleContextTest {

    private CastleGsonModel model = new CastleGsonModel();

    @Test
    public void minimalContextAsJson() {

        //given
        CastleContext aContext = new CastleContext();

        //when
        String contextJson = model.getGson().toJson(aContext);

        //Then
        Assertions.assertThat(contextJson).isEqualTo("{\"active\":true," + SDKVersion.getLibraryString() +"}");

    }

    @Test
    public void booleanContextValues() throws JSONException {
        //given
        CastleContext aContext = new CastleContext();

        //when
        String contextJson = model.getGson().toJson(aContext);

        //Then generated json match the api contract
        String expectedJson = "{\"active\":true," +
                "" + SDKVersion.getLibraryString() +"}";

        JSONAssert.assertEquals(expectedJson, contextJson, true);
    }

    @Test
    public void completeContextJsonSpec() throws JSONException {

        //given
        CastleContext aContext = new CastleContext();

        CastleDevice device = new CastleDevice();
        device.setId("d_id");
        device.setName("d_name");
        device.setType("d_type");
        device.setManufacturer("d_manufacturer");
        device.setModel("d_model");
        aContext.setDevice(device);

        CastleHeaders headers = new CastleHeaders();
        headers.setHeaders(ImmutableList.of(
                new CastleHeader("key1", "value1"),
                new CastleHeader("key2", "value2")
        ));

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
        //when
        String contextJson = model.getGson().toJson(aContext);

        //Then generated json match the api contract
        String expectedJson = "{\"active\":true," +
                "\"device\":{\"id\":\"d_id\",\"manufacturer\":\"d_manufacturer\",\"model\":\"d_model\",\"name\":\"d_name\",\"type\":\"d_type\"}," +
                "\"page\":{\"path\":\"p_path\",\"referrer\":\"p_referrer\",\"search\":\"p_search\",\"title\":\"p_title\",\"url\":\"p_url\"}," +
                "\"referrer\":{\"id\":\"r_id\",\"type\":\"r_type\"}," +
                "" + SDKVersion.getLibraryString() +"," +
                "\"location\":{\"city\":\"l_city\",\"country\":\"l_country\",\"latitude\":10,\"longitude\":10,\"speed\":0}," +
                "\"network\":{\"bluetooth\":true,\"cellular\":true,\"carrier\":\"n_carrier\",\"wifi\":true}," +
                "\"os\":{\"name\":\"o_name\",\"version\":\"0_version\"}," +
                "\"screen\":{\"width\":10,\"height\":20,\"density\":2}," +
                "\"timezone\":\"timezone\"}";
        JSONAssert.assertEquals(expectedJson, contextJson, true);

        // And json to object create the same structure
        CastleContext fromJson = model.getGson().fromJson(expectedJson, CastleContext.class);
        Assertions.assertThat(fromJson).isEqualToComparingFieldByFieldRecursively(aContext);
    }

    @Test
    public void toStringMethodCreatesAWellFormedStringFromAnEmptyContextInstance() {

        // given
        String expected = "CastleContext{active=true, device=null, timezone='null', page=null, referrer=null, library=CastleSdkRef{name='castle-java', version='" + SDKVersion.getVersion() + "', platform='" + SDKVersion.getJavaPlatform() + "', platformVersion='" + SDKVersion.getJavaVersion() + "'}, location=null, network=null, os=null, screen=null'}";
        CastleContext context = new CastleContext();

        //when
        String toString = context.toString();

        //then it should return a String with null everywhere except for active and library fields, which have defaults
        Assert.assertEquals(expected, toString);

    }

    @Test
    public void toStringMethodCreatesAWellFormedStringFromAFullExample() throws JSONException {
        //given
        CastleContext aContext = new CastleContext();

        aContext.setActive(false);

        CastleDevice device = new CastleDevice();
        device.setId("d_id");
        device.setName("d_name");
        device.setType("d_type");
        device.setManufacturer("d_manufacturer");
        device.setModel("d_model");
        aContext.setDevice(device);

        aContext.setTimezone("timezone");

        CastlePage page = new CastlePage();
        page.setPath("p_path");
        page.setReferrer("p_referrer");
        page.setSearch("p_search");
        page.setTitle("p_title");
        page.setUrl("p_url");
        aContext.setPage(page);

        CastleReferrer referrer = new CastleReferrer();
        referrer.setId("r_id");
        referrer.setType("r_type");
        aContext.setReferrer(referrer);

        CastleLocation location = new CastleLocation();
        location.setCity("l_city");
        location.setCountry("l_country");
        location.setLatitude(10L);
        location.setLongitude(10L);
        location.setSpeed(0L);
        aContext.setLocation(location);

        CastleNetwork network = new CastleNetwork();
        network.setBluetooth(true);
        network.setCarrier("n_carrier");
        network.setCellular(true);
        network.setWifi(true);
        aContext.setNetwork(network);

        CastleOS os = new CastleOS();
        os.setName("o_name");
        os.setVersion("0_version");
        aContext.setOs(os);

        CastleScreen screen = new CastleScreen();
        screen.setWidth(10);
        screen.setHeight(20);
        screen.setDensity(2);
        aContext.setScreen(screen);


        String expected = "CastleContext{active=false, " +
                "device=CastleDevice{id='d_id', manufacturer='d_manufacturer', model='d_model', name='d_name', type='d_type'}, " +
                "timezone='timezone', " +
                "page=CastlePage{path='p_path', referrer='p_referrer', search='p_search', title='p_title', url='p_url'}, " +
                "referrer=CastleReferrer{id='r_id', type='r_type'}, " +
                "library=CastleSdkRef{name='castle-java', version='" + SDKVersion.getVersion() + "', platform='" + SDKVersion.getJavaPlatform() + "', platformVersion='" + SDKVersion.getJavaVersion() + "'}, " +
                "location=CastleLocation{city='l_city', country='l_country', latitude=10, longitude=10, speed=0}, " +
                "network=CastleNetwork{bluetooth=true, cellular=true, carrier='n_carrier', wifi=true}, " +
                "os=CastleOS{name='o_name', version='0_version'}, " +
                "screen=CastleScreen{width=10, height=20, density=2}'}";


        //when
        String toString = aContext.toString();

        //then a String should be generated in the usual format (i.e., in the format generated automatically by the IDE.)
        Assert.assertEquals(expected, toString);

    }
}