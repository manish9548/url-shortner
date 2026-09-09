package url_shortener.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import url_shortener.project.dto.UrlResponse;
import url_shortener.project.entity.UrlEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import url_shortener.project.exception.AliasAlreadyTakenException;
import url_shortener.project.exception.UrlExpiredException;
import url_shortener.project.exception.UrlNotFoundException;
import url_shortener.project.repository.UrlRepository;
import url_shortener.project.serviceImpl.UrlServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;


    @Test
    void shouldCreateUrlWithCustomAlias() {

        String originalUrl = "https://github.com";
        String customAlias = "github";

        when(urlRepository.existsByShortCode(customAlias))
                .thenReturn(false);

        UrlResponse response = urlService.shortenUrl(
                originalUrl,
                null,
                customAlias
        );

        assertEquals(customAlias, response.getShortCode());
        assertEquals(originalUrl, response.getOriginalUrl());

        verify(urlRepository).save(any(UrlEntity.class));
    }
    @Test
    void shouldThrowExceptionWhenAliasAlreadyTaken() {

        String originalUrl = "https://github.com";
        String customAlias = "github";

        when(urlRepository.existsByShortCode(customAlias))
                .thenReturn(true);

        assertThrows(
                AliasAlreadyTakenException.class,
                () -> urlService.shortenUrl(
                        originalUrl,
                        null,
                        customAlias
                )
        );

        verify(urlRepository, never()).save(any(UrlEntity.class));
    }
    @Test
    void shouldShortenUrlSuccessfullyWithCustomAlias() {
        String originalUrl = "https://github.com";
        String customAlias = "github";

        when(urlRepository.existsByShortCode(customAlias)).thenReturn(false);

        UrlResponse response = urlService.shortenUrl(originalUrl, null, customAlias);

        assertNotNull(response);
        assertEquals(customAlias, response.getShortCode());
        assertEquals(originalUrl, response.getOriginalUrl());
        verify(urlRepository, times(1)).save(any(UrlEntity.class));
    }
    @Test
    void shouldShortenUrlSuccessfullyWithoutCustomAlias() {
        String originalUrl = "https://google.com";

        when(urlRepository.existsByShortCode(anyString())).thenReturn(false);

        UrlResponse response = urlService.shortenUrl(originalUrl, null, null);

        assertNotNull(response);
        assertNotNull(response.getShortCode());
        assertEquals(originalUrl, response.getOriginalUrl());
        verify(urlRepository, times(1)).save(any(UrlEntity.class));
    }
    @Test
    void shouldGetOriginalUrlSuccessfully() {
        String shortCode = "abc123";
        String originalUrl = "https://github.com";

        UrlEntity entity = new UrlEntity();
        entity.setOriginalUrl(originalUrl);
        entity.setShortCode(shortCode);
        entity.setClickCount(0L);
        entity.setExpireAt(null);

        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(entity));

        String result = urlService.getOriginal(shortCode);

        assertEquals(originalUrl, result);
        assertEquals(1L, entity.getClickCount());
        verify(urlRepository, times(1)).save(entity);
    }
    @Test
    void shouldThrowExceptionWhenUrlNotFound() {
        String shortCode = "notfound";

        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

        assertThrows(
                UrlNotFoundException.class,
                () -> urlService.getOriginal(shortCode)
        );

        verify(urlRepository, never()).save(any(UrlEntity.class));
    }
    @Test
    void shouldThrowExceptionWhenUrlExpired() {
        String shortCode = "expired";
        UrlEntity entity = new UrlEntity();
        entity.setOriginalUrl("https://github.com");
        entity.setShortCode(shortCode);
        entity.setExpireAt(LocalDateTime.now().minusMinutes(10)); // Past time

        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(entity));

        assertThrows(
                UrlExpiredException.class,
                () -> urlService.getOriginal(shortCode)
        );

        verify(urlRepository, never()).save(any(UrlEntity.class));
    }

}