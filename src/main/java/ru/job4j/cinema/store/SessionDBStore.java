package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SessionDBStore {
    private final static String FIND_ALL = "SELECT * FROM sessions";
    private final static String ADD = "INSERT INTO sessions(name) VALUES (?)";
    private final static String UPDATE = "UPDATE sessions SET name = ? WHERE id = ?";
    private final static String FIND_BY_ID = "SELECT * FROM sessions WHERE id = ?";
    private final static Logger LOG = LoggerFactory.getLogger(SessionDBStore.class.getName());

    private final BasicDataSource pool;

    public SessionDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(createSession(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findAll()", e);
        }
        return sessions;
    }

    public void add(Session session) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, session.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .add(Session)", e);
        }
    }

    public void update(Session session) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getId());
            ps.execute();
        } catch (SQLException e) {
            LOG.error("Exception in method .add(Session)", e);
        }
    }

    public Session findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createSession(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findById(int)", e);
        }
        return null;
    }

    private Session createSession(ResultSet rs) throws SQLException {
        return new Session(
                rs.getInt("id"),
                rs.getString("name"));
    }
}
