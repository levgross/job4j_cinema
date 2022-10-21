package ru.job4j.cinema.util;

import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;

public class Utility {
    private Utility() {
    }

    public static User check(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Guest");
        }
        return user;
    }
}
