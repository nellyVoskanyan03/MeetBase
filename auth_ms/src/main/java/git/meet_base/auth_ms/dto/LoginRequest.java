package git.meet_base.auth_ms.dto;

import jakarta.validation.constraints.NotEmpty;


public class LoginRequest {
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

}
