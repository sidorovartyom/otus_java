package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceAddressImpl;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServicePhoneImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var addressTemplate = new DataTemplateHibernate<>(Address.class);
        var phoneTemplate = new DataTemplateHibernate<>(Phone.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var dbServiceAddress = new DbServiceAddressImpl(transactionManager, addressTemplate);
        var dbServicePhone = new DbServicePhoneImpl(transactionManager, phoneTemplate);

        dbServiceAddress.saveAddress(new Address("street1"));
        dbServicePhone.savePhone(new Phone("11-111"));
        dbServicePhone.savePhone(new Phone("22-222"));
        //dbServiceClient.saveClient(new Client("dbServiceFirst"));
        dbServiceClient.saveClient(new Client("dbServiceFirst", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333"))));

        var addressSecond = dbServiceAddress.saveAddress(new Address("street2"));
        var phoneSecond = dbServicePhone.savePhone(new Phone("phone2"));
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));

        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        var addressSecondSelected = dbServiceAddress.getAddress(addressSecond.getId())
                .orElseThrow(() -> new RuntimeException("Address not found, id:" + addressSecond.getId()));
        log.info("addressSecondSelected:{}", addressSecondSelected);

        var phoneSecondSelected = dbServicePhone.getPhone(phoneSecond.getId())
                .orElseThrow(() -> new RuntimeException("Phone not found, id:" + phoneSecond.getId()));
        log.info("phoneSecondSelected:{}", phoneSecondSelected);
///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        dbServiceAddress.saveAddress(new Address(addressSecondSelected.getId(), "addressUpdate"));
        var addressUpdated = dbServiceAddress.getAddress(addressSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Address not found, id:" + addressSecondSelected.getId()));
        log.info("addressUpdated:{}", addressUpdated);

        dbServicePhone.savePhone(new Phone(phoneSecondSelected.getId(), "phoneUpdated"));
        var phoneUpdated = dbServicePhone.getPhone(phoneSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Phone not found, id:" + phoneSecondSelected.getId()));
        log.info("phoneUpdated:{}", phoneUpdated);

//        log.info("All clients");
//        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
//
//        log.info("All address");
//        dbServiceAddress.findAll().forEach(address -> log.info("address:{}", address));
//
//        log.info("All phone");
//        dbServicePhone.findAll().forEach(phone -> log.info("phone:{}", phone));
    }
}
