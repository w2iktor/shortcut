package pl.symentis.shorturl.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class RandomDateTimeFactory {
    public static LocalDateTime generateRandomDateTime() {
        return generateLocalDateTimeBetween(getMinDate(), getMaxDate());
    }

    public static LocalDateTime generateFutureDateTime() {
        return generateLocalDateTimeBetween(now(), getMaxDate());
    }

    public static LocalDateTime generatePastDateTime() {
        return generateLocalDateTimeBetween(getMinDate(), now());
    }

    private static LocalDateTime generateLocalDateTimeBetween(Instant minDate, Instant maxDate) {
        Instant instant = Instant.ofEpochSecond(getRandomTimeBetweenTwoDates(minDate, maxDate));
        return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }

    private static Instant now(){
        return Instant.now();
    }

    private static Instant getMaxDate() {
        return Instant.parse("2100-01-01T00:00:00.00Z");
    }

    private static Instant getMinDate() {
        return Instant.parse("2000-01-01T00:00:00.00Z");
    }

    private static long getRandomTimeBetweenTwoDates (Instant startTime, Instant endTime) {
        long startTimeEpochSecond = startTime.getEpochSecond();
        long diff = endTime.getEpochSecond() - startTimeEpochSecond + 1;
        return startTimeEpochSecond + (long) (Math.random() * diff) - 1;
    }
}
