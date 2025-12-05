package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.Flags;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private final int id;
    private final int memberId;
    private final String username;
    private final String password;
    private final Flags flags;
    private String token;

    public void updateToken(String token) {
        this.token = token;
    }
}