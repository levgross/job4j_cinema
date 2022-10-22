package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.Utility;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/createTicket")
    public String createTicket(HttpSession session) {
        Ticket ticket = new Ticket(
                0,
                new Session((Integer) session.getAttribute("sessionID"), ""),
                (Integer) session.getAttribute("row"),
                (Integer) session.getAttribute("cell"),
                (User) session.getAttribute("user")
        );
        Optional<Ticket> ticketDB = ticketService.add(ticket);
        if (ticketDB.isEmpty()) {

            return "redirect:/fail";
        }
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        model.addAttribute("row", session.getAttribute("row"));
        model.addAttribute("cell", session.getAttribute("cell"));
        model.addAttribute("sessionID", session.getAttribute("sessionID"));
        model.addAttribute("user", Utility.check(session));
        return "success";
    }

    @GetMapping("/fail")
    public String fail(Model model, HttpSession session) {
        model.addAttribute("message", "Unfortunately, the chosen seat is been bought by someone else!"
                + " Please, try another one.");
        model.addAttribute("sessionID", session.getAttribute("sessionID"));
        model.addAttribute("user", Utility.check(session));
        return "fail";
    }
}
