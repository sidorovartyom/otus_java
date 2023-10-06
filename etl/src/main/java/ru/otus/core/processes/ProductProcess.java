package ru.otus.core.processes;

import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.loader.CsvEtlProcess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ProductProcess {
    private static final Logger log = LoggerFactory.getLogger(ProductProcess.class);
    private static final String filePath = "C:/Temp/product.csv";
    private static final String tableStage = "product";
    private static final String tableCore = "client";
    private static final List<String> param = Arrays.asList("code", "name");

    public void etl() throws IOException {
        log.info("file path: {}, table core: {}, table stage: {}, param: {}", filePath, tableCore, tableStage, param);
        CsvEtlProcess etl = new CsvEtlProcess();
        etl.loadCsvFile(filePath, tableStage, param);
        etl.merge(tableCore, tableStage, param);
    }
}
