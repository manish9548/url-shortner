package url_shortener.project.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import url_shortener.project.dto.UrlResponse;
import url_shortener.project.entity.UrlEntity;
import url_shortener.project.exception.UrlExpiredException;
import url_shortener.project.exception.UrlNotFoundException;
import url_shortener.project.exception.AliasAlreadyTakenException;
import url_shortener.project.repository.UrlRepository;
import url_shortener.project.service.UrlService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final StringRedisTemplate redisTemplate;
    private final HttpServletRequest request;

    public UrlServiceImpl(UrlRepository urlRepository, StringRedisTemplate redisTemplate, HttpServletRequest request) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
        this.request = request;
    }

    @Override
    public UrlResponse shortenUrl(String originalUrl, LocalDateTime expiresAt, String customAlias) {

        // --- RATE LIMITING LOGIC (Guest User: Max 2 URLs) ---
        String clientIp = getClientIp(request);
        String rateKey = "rate:guest:" + clientIp;

        String countStr = redisTemplate.opsForValue().get(rateKey);
        int currentCount = countStr != null ? Integer.parseInt(countStr) : 0;

        if (currentCount >= 2) {
            throw new RuntimeException("Guest limit reached (Max 2 URLs). Please login to continue.");
        }
        // ----------------------------------------------------

        String shortCode;
        if (customAlias != null && !customAlias.isBlank()) {
            if (urlRepository.existsByShortCode(customAlias)) {
                throw new AliasAlreadyTakenException("alias already taken choose another name: ");
            }
            shortCode = customAlias;
        } else {
            shortCode = generateShortCode();
            while (urlRepository.existsByShortCode(shortCode)) {
                shortCode = generateShortCode();
            }
        }

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setShortCode(shortCode);
        urlEntity.setExpireAt(expiresAt);

        urlRepository.save(urlEntity);

        // Increment count and set 24-hour expiration on Redis key if success
        redisTemplate.opsForValue().increment(rateKey);
        if (countStr == null) {
            redisTemplate.expire(rateKey, Duration.ofHours(24));
        }

        UrlResponse response = new UrlResponse();
        response.setShortCode(shortCode);
        response.setOriginalUrl(originalUrl);
        response.setShortUrl("http://localhost:8080/" + shortCode);

        return response;
    }

    @Override
    @Cacheable(value = "urls", key = "#shortCode")
    public String getOriginal(String shortCode) {
        Optional<UrlEntity> urlEntity = urlRepository.findByShortCode(shortCode);

        UrlEntity entity = urlEntity.orElseThrow(
                () -> new UrlNotFoundException("Url not found for: " + shortCode)
        );

        if (entity.getExpireAt() != null &&
                LocalDateTime.now().isAfter(entity.getExpireAt())) {
            throw new UrlExpiredException("URL has expired");
        }

        entity.setClickCount(entity.getClickCount() + 1);
        entity.setLastAccessedAt(LocalDateTime.now());

        urlRepository.save(entity);
        return entity.getOriginalUrl();
    }

    private String generateShortCode() {
        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(pool.length());
            shortCode.append(pool.charAt(index));
        }
        return shortCode.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}