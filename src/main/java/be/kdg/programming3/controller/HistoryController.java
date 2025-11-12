package be.kdg.programming3.controller;

import be.kdg.programming3.session.PageHistory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private final PageHistory pageHistory;

    public HistoryController(PageHistory pageHistory) {
        this.pageHistory = pageHistory;
    }

    @GetMapping
    public String showHistory(Model model) {
        model.addAttribute("historyEntries", pageHistory.getEntries());
        return "history";
    }
}
