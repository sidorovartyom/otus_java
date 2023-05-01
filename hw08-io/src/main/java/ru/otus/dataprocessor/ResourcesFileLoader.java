package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ResourcesFileLoader implements Loader {
    private record MeasurementData(String name, double value) { }
    private final String fileName;

    public ResourcesFileLoader(String fileName) throws IOException {
        this.fileName = requireNonNull(fileName);
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> measurements;
        try (
                var inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        ) {
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            var gson = new Gson();
            measurements = Arrays.stream(gson.fromJson(reader, Measurement[].class)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return measurements;
    }
}
