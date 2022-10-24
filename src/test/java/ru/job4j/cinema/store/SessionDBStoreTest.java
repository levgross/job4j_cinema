package ru.job4j.cinema.store;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;

import static org.assertj.core.api.Assertions.*;

class SessionDBStoreTest {
    @Test
    void whenFindAll() {
        SessionDBStore store = new SessionDBStore(new Main().loadPool());
        Session session1 = new Session(1, "film_1");
        store.add(session1);
        Session session2 = new Session(2, "film_2");
        store.add(session2);
        assertThat(store.findAll()).contains(session1, session2);
    }

    @Test
    void whenAdd() {
        SessionDBStore store = new SessionDBStore(new Main().loadPool());
        Session session = new Session(3, "film_3");
        store.add(session);
        Session sessionInDb = store.findById(session.getId());
        assertThat(sessionInDb.getName()).isEqualTo(session.getName());
    }

    @Test
    void whenUpdate() {
        SessionDBStore store = new SessionDBStore(new Main().loadPool());
        Session session1 = new Session(4, "film_4");
        store.add(session1);
        Session session2 = new Session(session1.getId(), "film_changed");
        store.update(session2);
        Session sessionInDb = store.findById(session1.getId());
        assertThat(sessionInDb.getName()).isEqualTo(session2.getName());
    }
}