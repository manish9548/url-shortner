package url_shortener.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email khali nahi ho sakti")
    @Email(message = "Kripya ek valid email address dalein")
    private String email;

    @NotBlank(message = "Password khali nahi ho sakta")
    @Size(min = 6, message = "Password kam se kam 6 characters ka hona chahiye")
    private String password;
}