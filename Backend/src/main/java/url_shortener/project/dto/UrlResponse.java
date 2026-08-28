package url_shortener.project.dto;

import lombok.Data;

@Data
public class UrlResponse {
   private String  shortCode;
    private   String shortUrl;
    private   String originalUrl;
}
