/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.otus;

import com.opencsv.exceptions.CsvException;
import ru.otus.core.processes.ClientProcess;
import ru.otus.core.processes.ProductProcess;
import ru.otus.core.processes.SalesProcess;

import java.io.IOException;
import java.sql.SQLException;

public class etl_run {
    public static void main(String[] args) throws IOException {
        new ClientProcess().etl();
        new ProductProcess().etl();
        new SalesProcess().etl();
    }
}
