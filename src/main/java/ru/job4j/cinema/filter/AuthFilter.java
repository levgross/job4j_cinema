package ru.job4j.cinema.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class AuthFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        Set<String> pages = Set.of(
                "loginPage",
                "login",
                "index",
                "photoSession",
                "formAddUser",
                "registration"
                );
        if (anyMatch(uri, pages)) {
            chain.doFilter(req, resp);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, resp);
    }

    private boolean anyMatch(String uri, Set<String> set) {
        return set.stream()
                .anyMatch(uri::contains);
    }
}
