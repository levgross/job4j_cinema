package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.Utility;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {
    @Test
    public void whenIndex() {
        List<Session> sessions = Arrays.asList(
                new Session(1, "name_1"),
                new Session(2, "name_2")
        );
        User user = new User(1, "name", "email", "phone");
        HttpSession httpSession = mock(HttpSession.class);
        when(Utility.check(httpSession)).thenReturn(user);
        Model model = mock(Model.class);
        SessionService service = mock(SessionService.class);
        when(service.findAll()).thenReturn(sessions);
        SessionController controller = new SessionController(service);
        String page = controller.index(model, httpSession);
        verify(model).addAttribute("sessions", sessions);
        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("index");
    }

    @Test
    public void whenPhotoSession() {
        Session session = new Session(1, "name_1");
        SessionService service = mock(SessionService.class);
        SessionController controller = new SessionController(service);
        when(service.findById(1)).thenReturn(session);
        byte[] photo = new byte[1028];
        ResponseEntity<Resource> response = controller.download(1);
        ResponseEntity<Resource> result = ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(photo.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(photo));
        assertThat(response).isEqualTo(result);
    }

    @Test
    public void whenHall() {
        User user = new User(1, "name", "email", "phone");
        Model model = mock(Model.class);
        SessionService service = mock(SessionService.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(Utility.check(httpSession)).thenReturn(user);
        SessionController controller = new SessionController(service);
        String page = controller.hall(5, model, httpSession);
        verify(httpSession).setAttribute("sessionId", 5);
        verify(model).addAttribute("sessionId", 5);
        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("hallRow");
    }

    @Test
    public void whenHallRow() {
        User user = new User(1, "name", "email", "phone");
        Model model = mock(Model.class);
        SessionService service = mock(SessionService.class);
        SessionController controller = new SessionController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("sessionId")).thenReturn(1);
        when(Utility.check(httpSession)).thenReturn(user);
        String page = controller.hallRow(model, httpSession);
        verify(model).addAttribute("sessionId", 1);
        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("hallRow");
    }

    @Test
    public void whenChooseRow() {
        SessionService service = mock(SessionService.class);
        SessionController controller = new SessionController(service);
        HttpSession httpSession = mock(HttpSession.class);
        String page = controller.chooseRow(1, httpSession);
        verify(httpSession).setAttribute("row", 1);
        assertThat(page).isEqualTo("redirect:/ticketCell");
    }

    @Test
    public void whenTicketCell() {
        User user = new User(1, "name", "email", "phone");
        Model model = mock(Model.class);
        SessionService service = mock(SessionService.class);
        SessionController controller = new SessionController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("row")).thenReturn(3);
        when(httpSession.getAttribute("sessionId")).thenReturn(2);
        when(Utility.check(httpSession)).thenReturn(user);
        String page = controller.chooseCell(model, httpSession);
        verify(model).addAttribute("row", 3);
        verify(model).addAttribute("sessionId", 2);
        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("hallCell");
    }

    @Test
    public void whenChooseCell() {
        SessionService service = mock(SessionService.class);
        SessionController controller = new SessionController(service);
        HttpSession httpSession = mock(HttpSession.class);
        String page = controller.chooseCell(1, httpSession);
        verify(httpSession).setAttribute("cell", 1);
        assertThat(page).isEqualTo("redirect:/bookTicket");
    }

    @Test
    public void whenBookTicket() {
        User user = new User(1, "name", "email", "phone");
        Model model = mock(Model.class);
        SessionService service = mock(SessionService.class);
        SessionController controller = new SessionController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("row")).thenReturn(1);
        when(httpSession.getAttribute("cell")).thenReturn(2);
        when(httpSession.getAttribute("sessionId")).thenReturn(3);
        when(Utility.check(httpSession)).thenReturn(user);
        String page = controller.bookTicket(model, httpSession);
        verify(model).addAttribute("row", 1);
        verify(model).addAttribute("cell", 2);
        verify(model).addAttribute("sessionId", 3);
        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("addTicket");
    }
}