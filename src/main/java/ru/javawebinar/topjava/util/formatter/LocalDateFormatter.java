package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return parseLocalDate(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }
}
