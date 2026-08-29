package url_shortener.project.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UrlRequest {
    @NotBlank(message = "URL cannot be blank")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Invalid URL. URL must start with http:// or https://"
    )
    private String originalUrl;
}
