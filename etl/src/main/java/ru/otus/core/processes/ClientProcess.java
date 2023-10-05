package ru.otus.core.processes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.loader.CsvEtlProcess;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClientProcess {
    private static final Logger log = LoggerFactory.getLogger(ClientProcess.class);
    private static final String filePath = "C:/Temp/client.csv";
    private static final String tableStage = "stg_client";
    private static final String tableCore = "client";
    private static final List<String> param = Arrays.asList("code", "name", "sex", "birth_date");
    public void etl() throws IOException {
        log.info("file path: {}, table: {}, param: {}", filePath, tableStage, param);
        CsvEtlProcess etl = new CsvEtlProcess();
        etl.loadCsvFile(filePath, tableStage, param);
        etl.merge(tableCore, tableStage, param);
    }
}
