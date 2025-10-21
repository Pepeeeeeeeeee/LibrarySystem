package be.kdg.programming3;

import be.kdg.programming3.presenter.LibraryPresenter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main /*implements CommandLineRunner*/{
//    private final LibraryPresenter presenter;
//
//    public Main(LibraryPresenter presenter) {
//        this.presenter = presenter;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
//
//    @Override
//    public void run(String... args) {
//        presenter.start();
//    }
}