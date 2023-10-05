package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dataTaplate.DataTemplateJdbc;
import ru.otus.core.sessionmanager.TransactionRunner;

import java.sql.SQLException;

public class DbServiceImpl implements DBService {
    private static final Logger log = LoggerFactory.getLogger(DbServiceImpl.class);

    private final DataTemplateJdbc dataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceImpl(TransactionRunner transactionRunner, DataTemplateJdbc dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public void executeStatement(String sql) {
        transactionRunner.doInTransaction(connection -> {
            try {
                dataTemplate.executeStatement(connection, sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            log.info("insert: {}", sql);
            return null;
        });
    }
}