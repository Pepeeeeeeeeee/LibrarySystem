package be.kdg.programming3.view;

import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class ConsoleView implements ViewContract {
    @Override
    public void display(Object obj) {
        System.out.println(obj.toString());
    }

    @Override
    public String prompt(String message) {
        System.out.print(message);
        return new Scanner(System.in).nextLine();
    }

    @Override
    public void showMenu(String title, String[] items) {
        System.out.println("\n--- " + title + " ---");
        for (String item : items) {
            System.out.printf("%s%n", item);
        }
    }
}
