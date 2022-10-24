package ru.job4j.cinema.store;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserDBStoreTest {

    @Test
    void whenAdd() {
        UserDBStore store = new UserDBStore(new Main().loadPool());
        User user = new User(1, "name", "email", "phone");
        Optional<User> userOpt = store.add(user);
        assertThat(userOpt.isPresent()).isTrue();
        User userFromDb = userOpt.get();
        assertThat(userFromDb.getUsername()).isEqualTo(user.getUsername());
        assertThat(userFromDb.getEmail()).isEqualTo(user.getEmail());
        assertThat(userFromDb.getPhone()).isEqualTo(user.getPhone());
    }

    @Test
    void whenFindUserByEmailAndPhone() {
        UserDBStore store = new UserDBStore(new Main().loadPool());
        User user = new User(1, "name", "email", "phone");
        store.add(user);
        Optional<User> userOpt1 = store.findUserByEmailAndPhone("email", "wrong");
        assertThat(userOpt1.isEmpty()).isTrue();
        Optional<User> userOpt2 = store.findUserByEmailAndPhone("email", "phone");
        assertThat(userOpt2.isPresent()).isTrue();
        User userFromDb = userOpt2.get();
        assertThat(userFromDb.getUsername()).isEqualTo(user.getUsername());
        assertThat(userFromDb.getEmail()).isEqualTo(user.getEmail());
        assertThat(userFromDb.getPhone()).isEqualTo(user.getPhone());
    }
}