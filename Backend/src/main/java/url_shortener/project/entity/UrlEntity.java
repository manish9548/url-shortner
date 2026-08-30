package url_shortener.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UrlEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
 private    String originalUrl;

    @Column(unique = true, nullable = false)
  private   String shortCode;

   private LocalDateTime expireAt;
}
