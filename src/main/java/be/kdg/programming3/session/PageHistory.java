package be.kdg.programming3.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class PageHistory {
    private final List<HistoryEntry> entries = new ArrayList<>();

    public void addEntry(String url) {
        entries.add(new HistoryEntry(url, LocalDateTime.now()));
    }

    public List<HistoryEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public static class HistoryEntry {
        private final String url;
        private final LocalDateTime timestamp;

        public HistoryEntry(String url, LocalDateTime timestamp) {
            this.url = url;
            this.timestamp = timestamp;
        }

        public String getUrl() {
            return url;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
