package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;

public class DbServicePhoneImpl implements DBServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DbServicePhoneImpl.class);

    private final DataTemplate<Phone> phoneDataTemplate;
    private final TransactionManager transactionManager;

    public DbServicePhoneImpl(TransactionManager transactionManager, DataTemplate<Phone> phoneDataTemplate) {
        this.transactionManager = transactionManager;
        this.phoneDataTemplate = phoneDataTemplate;
    }

    @Override
    public Phone savePhone(Phone phone) {
        return transactionManager.doInTransaction(session -> {
            var phoneCloned = phone.clone();
            if (phone.getId() == null) {
                var savedPhone = phoneDataTemplate.insert(session, phoneCloned);
                log.info("created phone: {}", phoneCloned);
                return savedPhone;
            }
            var savedPhone = phoneDataTemplate.update(session, phoneCloned);
            log.info("updated phone: {}", savedPhone);
            return savedPhone;
        });
    }

    @Override
    public Optional<Phone> getPhone(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var phoneOptional = phoneDataTemplate.findById(session, id);
            log.info("phone: {}", phoneOptional);
            return phoneOptional;
        });
    }

    @Override
    public List<Phone> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var phoneList = phoneDataTemplate.findAll(session);
            log.info("phoneList:{}", phoneList);
            return phoneList;
       });
    }
}
