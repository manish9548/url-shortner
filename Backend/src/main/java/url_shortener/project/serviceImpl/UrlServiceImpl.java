package url_shortener.project.serviceImpl;


import org.springframework.stereotype.Service;
import url_shortener.project.dto.UrlResponse;
import url_shortener.project.entity.UrlEntity;
import url_shortener.project.exception.UrlExpiredException;
import url_shortener.project.exception.UrlNotFoundException;
import url_shortener.project.repository.UrlRepository;
import url_shortener.project.service.UrlService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    public UrlServiceImpl(UrlRepository urlRepository){
        this.urlRepository=urlRepository;
    }

    @Override
    public UrlResponse shortenUrl(String originalUrl , LocalDateTime expiresAt) {

        String shortCode = generateShortCode();

        while (urlRepository.existsByShortCode(shortCode)) {
            shortCode = generateShortCode();
        }

        UrlEntity urlEntity = new UrlEntity();


        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setShortCode(shortCode);
        urlEntity.setExpireAt(expiresAt);

        urlRepository.save(urlEntity);

        UrlResponse response = new UrlResponse();

        response.setShortCode(shortCode);
        response.setOriginalUrl(originalUrl);
        response.setShortUrl("http://localhost:8080/" + shortCode);

        return response;
    }

    @Override
    public String getOriginal(String shortCode) {

        Optional<UrlEntity> urlEntity = urlRepository.findByShortCode(shortCode);

        UrlEntity entity = urlEntity.orElseThrow(
                () -> new UrlNotFoundException("Url not found for: " + shortCode)
        );

        if (entity.getExpireAt() != null &&
                LocalDateTime.now().isAfter(entity.getExpireAt())) {

            throw new UrlExpiredException("URL has expired");
        }

        return entity.getOriginalUrl();
    }

    private String generateShortCode(){

        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random= new Random();
        StringBuilder shortCode = new StringBuilder();
        for(int i=0;i<6;i++){
            int index= random.nextInt(pool.length());
            shortCode.append(pool.charAt(index));

        }
        return shortCode.toString();
    }


}
