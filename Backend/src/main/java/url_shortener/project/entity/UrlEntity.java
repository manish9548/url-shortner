package url_shortener.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UrlEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String originalUrl;

    @Column(unique = true, nullable = false)
    String shortCode;
}
