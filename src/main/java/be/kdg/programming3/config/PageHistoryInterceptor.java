package be.kdg.programming3.config;

import be.kdg.programming3.session.PageHistory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PageHistoryInterceptor implements HandlerInterceptor {

    private final PageHistory pageHistory;

    public PageHistoryInterceptor(PageHistory pageHistory) {
        this.pageHistory = pageHistory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            pageHistory.addEntry(request.getRequestURI());
        }
        return true;
    }
}
