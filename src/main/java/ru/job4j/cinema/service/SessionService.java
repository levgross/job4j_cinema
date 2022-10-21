package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.store.SessionDBStore;

import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class SessionService {
    private final SessionDBStore store;

    public SessionService(SessionDBStore store) {
        this.store = store;
        init();
    }

    public void init() {
        final List<String> names = List.of(
                "avengers",
                "brazil",
                "cloudAtlas",
                "gamer",
                "insurgent",
                "portal",
                "rush",
                "titanic"
        );
        for (String name : names) {
            store.add(new Session(0, name));
        }
    }

    public void add(Session session) {
        store.add(session);
    }

    public void update(Session session) {
        store.update(session);
    }

    public Session findById(int id) {
        return store.findById(id);
    }

    public Collection<Session> findAll() {
        return store.findAll();
    }
}
