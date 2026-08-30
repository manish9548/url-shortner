package url_shortener.project.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import url_shortener.project.dto.UrlRequest;
import url_shortener.project.dto.UrlResponse;
import url_shortener.project.service.UrlService;

import java.net.URI;

@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;



    @PostMapping("/api/shorten")
    public UrlResponse SendOriginalUrl(@Valid @RequestBody UrlRequest request) {

      UrlResponse   response = urlService.shortenUrl(request.getOriginalUrl(),
              request.getExpireAt());

        return response;
    }
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode) {

        String originalUrl = urlService.getOriginal(shortCode);

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }



}