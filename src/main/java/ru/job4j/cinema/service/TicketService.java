package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.TicketDBStore;

import java.util.Collection;

@ThreadSafe
@Service
public class TicketService {
    private final TicketDBStore store;

    public TicketService(TicketDBStore store) {
        this.store = store;
    }

    public void add(Ticket ticket) {
        store.add(ticket);
    }

    public Ticket findById(int id) {
        return store.findById(id);
    }

    public Collection<Ticket> findAll() {
        return store.findAll();
    }
}
