package ru.otus.core.dataTaplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DataTemplate {
    void executeStatement(Connection connection, String sql) throws SQLException;
    void update(Connection connection);
}
