package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketDBStore {
    private final static String FIND_ALL = "SELECT * FROM ticket";
    private final static String ADD = "INSERT INTO ticket(session_id, pos_row, cell, user_id)"
            + "VALUES (?, ?, ?, ?)";
    private final static String FIND_BY_ID = "SELECT * FROM ticket WHERE id = ?";
    private final static Logger LOG = LoggerFactory.getLogger(TicketDBStore.class.getName());
    private final BasicDataSource pool;

    public TicketDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicket(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findAll()", e);
        }
        return tickets;
    }

    public void add(Ticket ticket) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .add(Ticket)", e);
        }
    }

    public Ticket findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createTicket(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findById(int)", e);
        }
        return null;
    }

    private Ticket createTicket(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getInt("id"),
                new Session(rs.getInt("session_id"), ""),
                rs.getInt("pos_row"),
                rs.getInt("cell"),
                new User(rs.getInt("user_id"), "", "", ""));
    }
}
