package be.kdg.programming3.view;

public interface ViewContract {
    void display(Object obj);
    String prompt(String message);
    void showMenu(String title, String[] items);
}
