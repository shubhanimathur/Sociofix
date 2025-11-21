package com.example.project31.Dto.TimeSerialize;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @ToJson
    public String toJson(LocalDateTime value) {
        return value.format(FORMATTER);
    }

    @FromJson
    public LocalDateTime fromJson(String string) {
        return LocalDateTime.parse(string, FORMATTER);
    }
}