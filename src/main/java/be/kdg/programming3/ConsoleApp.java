package be.kdg.programming3;

import be.kdg.programming3.presenter.LibraryPresenter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("console")
public class ConsoleApp implements CommandLineRunner {
    private final LibraryPresenter presenter;

    public ConsoleApp(LibraryPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void run(String... args) {
        presenter.start();
    }
}
