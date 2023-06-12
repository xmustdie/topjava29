package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 12.06.2023
 */

public abstract class AbstractInMemoryRepository<T extends AbstractBaseEntity> {
    protected static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    protected final Map<Integer, T> repository = new ConcurrentHashMap<>();
    protected final AtomicInteger counter = new AtomicInteger(0);

    public T get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    public T save(T entity) {
        log.info("save {}", entity);
        if (entity.isNew()) {
            entity.setId(counter.incrementAndGet());
            repository.put(entity.getId(), entity);
            return entity;
        }
        return repository.computeIfPresent(entity.getId(), (id, oldEntity) -> entity);
    }
}
