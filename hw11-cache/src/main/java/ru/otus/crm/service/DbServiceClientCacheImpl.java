package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.valueOf;

public class DbServiceClientCacheImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientCacheImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> cache = new MyCache<>();;

    public DbServiceClientCacheImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                //log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            //log.info("updated client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(valueOf(id));
        if (Objects.nonNull(cachedClient)) {
            return Optional.of(cachedClient);
        }

        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            //log.info("client: {}", clientOptional);
            clientOptional.ifPresent(c -> cache.put(Long.toString(id), c));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            //log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
