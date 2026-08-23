package url_shortener.project.serviceImpl;

import lombok.Data;
import org.springframework.stereotype.Service;
import url_shortener.project.entity.UrlEntity;
import url_shortener.project.exception.UrlNotFoundException;
import url_shortener.project.repository.UrlRepository;
import url_shortener.project.service.UrlService;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    public UrlServiceImpl(UrlRepository urlRepository){
        this.urlRepository=urlRepository;
    }

    @Override
    public String shortenUrl(String originalUrl) {
        String shortCode= UUID.randomUUID().toString();
        UrlEntity urlEntity=new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setShortCode(shortCode);
        urlRepository.save(urlEntity);
        return shortCode;
    }

    @Override
    public String getOriginal(String shortCode) {
        Optional<UrlEntity> urlEntity=urlRepository.findByShortCode(shortCode);
        UrlEntity entity=urlEntity.orElseThrow(
                ()-> new UrlNotFoundException("Url not found for :"+ shortCode)
        );

        String originalUrl=entity.getOriginalUrl();
        return originalUrl;
    }
}
