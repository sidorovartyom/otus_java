package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = requireNonNull(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (Writer writer = new FileWriter(fileName)){
            var gson = new GsonBuilder().create();
            gson.toJson(data, writer);
        }
        catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
