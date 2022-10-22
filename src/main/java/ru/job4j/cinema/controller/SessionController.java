package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.Utility;

import javax.servlet.http.HttpSession;
import java.io.*;

@ThreadSafe
@Controller
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("user", Utility.check(session));
        return "index";
    }

    @GetMapping("/photoSession/{sessionId}")
    public ResponseEntity<Resource> download(@PathVariable("sessionId") Integer sessionId) {
        ClassPathResource resource = new ClassPathResource(
                "/img/" + sessionService.findById(sessionId).getName() + ".jpg"
        );
        byte[] photo = new byte[1028];
        try (InputStream is = resource.getInputStream()) {
            photo = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(photo.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(photo));
    }

    @GetMapping("/hall/{sessionId}")
    public String hall(@PathVariable("sessionId") Integer sessionId,
                       Model model,
                       HttpSession session) {
        session.setAttribute("sessionID", sessionId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("user", Utility.check(session));
        return "hallRow";
    }

    @GetMapping("/hallRow")
    public String hallRow(Model model, HttpSession session) {
        model.addAttribute("sessionId", session.getAttribute("sessionID"));
        model.addAttribute("user", Utility.check(session));
        return "hallRow";
    }

    @PostMapping("/chooseRow")
    public String ticketRow(@RequestParam("row") int row,
                            HttpSession session) {
        session.setAttribute("row", row);
        return "redirect:/ticketCell";
    }

    @GetMapping("/ticketCell")
    public String ticketCell(Model model, HttpSession session) {
        model.addAttribute("row", session.getAttribute("row"));
        model.addAttribute("sessionId", session.getAttribute("sessionID"));
        model.addAttribute("user", Utility.check(session));
        return "hallCell";
    }

    @PostMapping("/chooseCell")
    public String ticketCell(@RequestParam("cell") int cell,
                            HttpSession session) {
        session.setAttribute("cell", cell);
        return "redirect:/bookTicket";
    }

    @GetMapping("/bookTicket")
    public String addTicket(Model model, HttpSession session) {
        model.addAttribute("row", session.getAttribute("row"));
        model.addAttribute("cell", session.getAttribute("cell"));
        model.addAttribute("sessionID", session.getAttribute("sessionID"));
        model.addAttribute("user", Utility.check(session));

        return "addTicket";
    }
}
