package com.example.openmarket.utility;

import java.time.LocalDate;
import java.time.format.*;
public class LocalDateParser {
    public static LocalDate parseDate(String text) {
        String[] formats = {
                "dd-MM-yyyy",
                "dd/MM/yyyy",
                "dd.MM.yyyy"
        };

        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(text, formatter);
            } catch (DateTimeParseException ignore) {}
        }

        return null;
    }
}
