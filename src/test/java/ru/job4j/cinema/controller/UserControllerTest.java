package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void whenLoginPageThenFailTrue() {
        Model model = mock(Model.class);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        String page = controller.loginPage(model, true);
        verify(model).addAttribute("fail", true);
        assertThat(page).isEqualTo("login");
    }

    @Test
    void whenLoginPageThenFailNull() {
        Model model = mock(Model.class);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        String page = controller.loginPage(model, null);
        verify(model).addAttribute("fail", false);
        assertThat(page).isEqualTo("login");
    }

    @Test
    void whenLoginSuccess() {
        User user = new User(1, "", "", "");
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(req.getSession()).thenReturn(httpSession);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        when(service.findUserByEmailAndPhone(user.getEmail(), user.getPhone()))
                .thenReturn(Optional.of(user));
        String page = controller.login(user, req);
        verify(httpSession).setAttribute("user", user);
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    void whenLoginFail() {
        User user = new User(1, "", "", "");
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(req.getSession()).thenReturn(httpSession);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        when(service.findUserByEmailAndPhone(user.getEmail(), user.getPhone()))
                .thenReturn(Optional.empty());
        String page = controller.login(user, req);
        assertThat(page).isEqualTo("redirect:/loginPage?fail=true");
    }

    @Test
    void whenLogout() {
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        String page = controller.logout(new MockHttpSession());
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    void whenFormAddUserThenFailTrue() {
        User user = new User(1, "", "", "");
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(Utility.check(httpSession)).thenReturn(user);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        String page = controller.formAddUser(model, true, httpSession);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("fail", true);
        assertThat(page).isEqualTo("addUser");
    }

    @Test
    void whenFormAddUserThenFailNull() {
        User user = new User(1, "", "", "");
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(Utility.check(httpSession)).thenReturn(user);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        String page = controller.formAddUser(model, null, httpSession);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("fail", false);
        assertThat(page).isEqualTo("addUser");
    }

    @Test
    void whenRegistrationThenSuccess() {
        User user = new User(1, "", "", "");
        Model model = mock(Model.class);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        when(service.add(user)).thenReturn(Optional.of(user));
        String page = controller.registration(model, user);
        assertThat(page).isEqualTo("redirect:/loginPage");
    }

    @Test
    void whenRegistrationThenFail() {
        User user = new User(1, "", "", "");
        Model model = mock(Model.class);
        UserService service = mock(UserService.class);
        UserController controller = new UserController(service);
        when(service.add(user)).thenReturn(Optional.empty());
        String page = controller.registration(model, user);
        assertThat(page).isEqualTo("redirect:/formAddUser?fail=true");
    }
}