package ru.otus.core.loader;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dataTaplate.DataTemplateJdbc;
import ru.otus.core.datasource.DriverManagerDataSource;
import ru.otus.core.executor.DbExecutorImpl;
import ru.otus.core.service.DbServiceImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;
import java.io.*;

import java.util.Iterator;
import java.util.List;

public class CsvLoader  {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";
    public static final String CSV_FILE_DELIMITER = ";";
    private static final Logger log = LoggerFactory.getLogger(CsvLoader.class);
    public void load(String csvFileName, String table, List<String> param) throws IOException {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var dataTemplateJdbc = new DataTemplateJdbc(dbExecutor);
        var dbService = new DbServiceImpl(transactionRunner, dataTemplateJdbc);

        ParamTable paramTable = new ParamTable(param);

        FileReader reader = new FileReader(csvFileName);
        BufferedReader buffer = new BufferedReader(reader);

        dbService.executeStatement("truncate table " + table); //чистка таблицы

        String line = "";
        String[] tempArr;
        String query = "";

        buffer.readLine();//пропускам первую строку - название столбцов
        while ((line = buffer.readLine()) != null) {
            tempArr = line.split(CSV_FILE_DELIMITER);
            query = "INSERT INTO " + table + " " + paramTable.getParamName() + " VALUES ";
            query = query + "('" + StringUtils.join(tempArr, "', '") + "')";
            dbService.executeStatement(query);
        }
    }
    public void merge(String table_target, String table_source, List<String> param) throws IOException {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var dataTemplateJdbc = new DataTemplateJdbc(dbExecutor);
        var dbService = new DbServiceImpl(transactionRunner, dataTemplateJdbc);
        String paramSet = "";
        String paramInsertTarget = "";
        String paramInsertSource = "";
        Iterator<String> iterator = param.iterator();
        while (iterator.hasNext()) {
            var p = iterator.next();
            paramSet = paramSet + p + " = s." + p;
            paramInsertTarget = paramInsertTarget + p;
            paramInsertSource = paramInsertSource + "s." + p;
            if (iterator.hasNext()) {
                paramSet = paramSet + ", ";
                paramInsertTarget = paramInsertTarget + ", ";
                paramInsertSource = paramInsertSource + ", ";
            }
        }
        String queryUpdate =
                "UPDATE " + table_target + " t \n" +
        "        SET " + paramSet + "\n" +
        "        FROM " + table_source + " s \n" +
        "        WHERE t.code = s.code; ";

        String queryInsert =
        "        INSERT INTO " + table_target + " ( " + paramInsertTarget + " )\n" +
        "        SELECT " + paramInsertSource + "\n" +
        "        FROM " + table_source + " s \n" +
        "        LEFT OUTER JOIN " + table_target + " t ON (t.code = s.code)\n" +
        "        WHERE t.code IS NULL;";
        log.info("run update");
        dbService.executeStatement(queryUpdate);
        log.info("run insert");
        dbService.executeStatement(queryInsert);

    }
    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
