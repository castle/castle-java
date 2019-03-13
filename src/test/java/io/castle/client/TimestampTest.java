package io.castle.client;

import io.castle.client.internal.utils.Timestamp;
import io.castle.client.model.CastleSdkConfigurationException;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.DateUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimestampTest {

    @Test
    public void dateToTimestamp() {
        //Given
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), new Locale("en", "US", "POSIX"));
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND, 5);
        calendar.set(Calendar.MILLISECOND, 678);

        String timestamp = Timestamp.timestamp(calendar.getTime());

        Assertions.assertThat(timestamp).isEqualTo("2019-01-02T03:04:05.678Z");
    }
}
