package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    Map<Integer, User> repository = new ConcurrentHashMap<>();
    AtomicInteger counter = new AtomicInteger();

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.keySet().removeIf(u -> u == id);
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            if (repository.entrySet().stream().anyMatch( k -> k.getValue().getEmail().equals(user.getEmail())))
                throw new NotFoundException("Emali alredy exist ");
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(),(id, u) -> user);


    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<>(repository.values()).stream()
                .sorted(Comparator.comparing(AbstractNamedEntity::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.entrySet().stream()
                .filter(user -> user.getValue().getEmail().equals(email)).findFirst().get().getValue();
    }
}
