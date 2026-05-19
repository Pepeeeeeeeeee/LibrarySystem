package be.kdg.librarysystem.dto;

import be.kdg.librarysystem.domain.enums.Roles;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private int memberId;
    private String username;
    private String password;
    private Roles role;

    public UserDTO(Object... data){
        this.id = data[0] != null ? (Integer) data[0]: -1;
        this.memberId = data[1] != null ? (Integer) data[1]: -1;
        this.username = data[2] != null ? (String) data[2]: null;
        this.password = data[3] != null ? (String) data[3]: null;
        this.role = data[4] != null ? (Roles) data[4]: null;
    }
}
