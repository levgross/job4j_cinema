package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.Utility;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/bookTicket")
    public String addTicket(Model model, HttpSession session) {
        model.addAttribute("row", session.getAttribute("row"));
        model.addAttribute("cell", session.getAttribute("cell"));
        model.addAttribute("sessionID", session.getAttribute("sessionID"));
        model.addAttribute("user", Utility.check(session));
        return "addTicket";
    }

    @PostMapping("/createTicket")
    public String createTicket(@ModelAttribute Ticket ticket,
                               HttpSession session) {
        ticketService.add(ticket);
        return "redirect:/success";
    }
}
