package ru.otus.core.loader;


import java.io.IOException;
import java.util.List;


public class CsvEtlProcess {
    private CsvLoader csvLoader;

    public CsvEtlProcess() {
        csvLoader = new CsvLoader();
    }

    public int loadCsvFile(String filePath, String table, List<String> param) throws IOException {
        int res = 0;
        csvLoader.load(filePath, table, param);
        return res;
    }
    public int merge(String table_target, String table_source, List<String> param) throws IOException {
        int res = 0;
        csvLoader.merge(table_target, table_source, param);
        return res;
    }
}
