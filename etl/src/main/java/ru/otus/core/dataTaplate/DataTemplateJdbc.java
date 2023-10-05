package ru.otus.core.dataTaplate;

import ru.otus.core.executor.DbExecutor;

import java.sql.Connection;
import java.sql.SQLException;

public class DataTemplateJdbc implements DataTemplate {

    private final DbExecutor dbExecutor;
    public DataTemplateJdbc(DbExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    @Override
    public void executeStatement(Connection connection, String sql) throws SQLException {
        dbExecutor.executeStatement(
                connection,
                sql
        );
    }

    @Override
    public void update(Connection connection) {

    }
}
