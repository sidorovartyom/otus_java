package ru.otus.core.processes;

import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.loader.CsvEtlProcess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SalesProcess {
    private static final Logger log = LoggerFactory.getLogger(SalesProcess.class);
    private static final String filePath = "C:/Temp/sales.csv";
    private static final String tableStage = "stg_sales";
    private static final String tableCore = "sales";
    private static final List<String> param = Arrays.asList("code", "client_code", "product_code", "amount_rub", "date_sale");
    public void etl() throws IOException {
        log.info("file path: {}, table core: {}, table stage: {}, param: {}", filePath, tableCore, tableStage, param);
        CsvEtlProcess etl = new CsvEtlProcess();
        etl.loadCsvFile(filePath, tableStage, param);
        etl.merge(tableCore, tableStage, param);
    }
}
