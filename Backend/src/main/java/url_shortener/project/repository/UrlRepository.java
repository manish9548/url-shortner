package url_shortener.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import url_shortener.project.entity.UrlEntity;
import url_shortener.project.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity ,Long> {
   Optional<UrlEntity> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    List<UrlEntity> findByUser(UserEntity user);

}
