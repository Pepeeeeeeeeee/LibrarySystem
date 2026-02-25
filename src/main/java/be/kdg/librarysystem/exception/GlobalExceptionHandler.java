package be.kdg.librarysystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseException(DataAccessException ex, Model model) {
        log.error("Database error: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "A database error occurred. Please try again later.");
        return "errorDb";
    }

//    @ExceptionHandler(Exception.class)
//    public String handleOtherExceptions(Exception ex, Model model) {
//        log.error("Unexpected error: {}", ex.getMessage(), ex);
//        model.addAttribute("errorMessage", "An unexpected error occurred. Please contact support.");
//        return "error";
//    }
}
