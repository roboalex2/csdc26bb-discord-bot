package at.csdc26bb.discord.bot.service;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OffsetDateTimeConversionService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public OffsetDateTime fromCharSequence(CharSequence value) {
        OffsetDateTime timestamp = null;
        try {
            timestamp = OffsetDateTime.parse(value, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            try {
                timestamp = OffsetDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            } catch (Exception e1) {
                try {
                    timestamp = OffsetDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
                } catch (Exception e2) {
                    timestamp = OffsetDateTime.parse(value);
                }
            }
        }

        return timestamp;
    }

    public CharSequence toCharSequence(OffsetDateTime value) {
        return value.format(DATE_TIME_FORMATTER);
    }
}