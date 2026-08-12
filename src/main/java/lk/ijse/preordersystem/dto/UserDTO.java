package lk.ijse.preordersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private long userId;
    private String username;
    private String userRoles;
    private String contact;
    private String email;
    private String password;

    public UserDTO(String userRoles, String password, String username) {
        this.userRoles = userRoles;
        this.password = password;
        this.username = username;
    }

    public UserDTO(long userId, String username, String userRoles) {
        this.userId = userId;
        this.username = username;
        this.userRoles = userRoles;
    }
}
