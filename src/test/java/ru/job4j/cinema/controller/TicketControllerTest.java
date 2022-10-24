package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.Utility;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Test
    void whenCreateTicketThenSuccess() {
        Ticket ticket = new Ticket(0, new Session(1, ""), 3, 4, new User());
        Model model = mock(Model.class);
        TicketService service = mock(TicketService.class);
        when(service.add(ticket)).thenReturn(Optional.of(ticket));
        TicketController controller = new TicketController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("sessionId")).thenReturn(0);
        when(httpSession.getAttribute("row")).thenReturn(0);
        when(httpSession.getAttribute("cell")).thenReturn(0);
        when(httpSession.getAttribute("user")).thenReturn(new User());
        String page = controller.createTicket(model, httpSession);
        assertThat(page).isEqualTo("redirect:/success");
    }

    @Test
    void whenCreateTicketThenFail() {
        Ticket ticket = new Ticket(0, new Session(1, ""), 3, 4, new User());
        Model model = mock(Model.class);
        TicketService service = mock(TicketService.class);
        when(service.add(ticket)).thenReturn(Optional.empty());
        TicketController controller = new TicketController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("sessionId")).thenReturn(0);
        when(httpSession.getAttribute("row")).thenReturn(0);
        when(httpSession.getAttribute("cell")).thenReturn(0);
        when(httpSession.getAttribute("user")).thenReturn(new User());
        String page = controller.createTicket(model, httpSession);
        assertThat(page).isEqualTo("redirect:/fail");
    }

    @Test
    void whenSuccess() {
        User user = new User(1, "", "", "");
        Model model = mock(Model.class);
        TicketService service = mock(TicketService.class);
        TicketController controller = new TicketController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("row")).thenReturn(1);
        when(httpSession.getAttribute("cell")).thenReturn(2);
        when(httpSession.getAttribute("sessionId")).thenReturn(3);
        when(Utility.check(httpSession)).thenReturn(user);
        String page = controller.success(model, httpSession);
        verify(model).addAttribute("row", 1);
        verify(model).addAttribute("cell", 2);
        verify(model).addAttribute("sessionId", 3);
        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("success");
    }

    @Test
    void whenFail() {
        User user = new User(1, "", "", "");
        Model model = mock(Model.class);
        TicketService service = mock(TicketService.class);
        TicketController controller = new TicketController(service);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("sessionId")).thenReturn(2);
        when(Utility.check(httpSession)).thenReturn(user);
        String page = controller.fail(model, httpSession);
        verify(model).addAttribute("sessionId", 2);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("message", "Unfortunately, the chosen seat is been bought by someone else!"
                + " Please, try another one.");
        assertThat(page).isEqualTo("fail");
    }
}