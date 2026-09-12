package url_shortener.project.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import url_shortener.project.dto.UrlRequest;
import url_shortener.project.dto.UrlResponse;
import url_shortener.project.service.UrlService;

import java.net.URI;

@RestController
@AllArgsConstructor
public class UrlController {
    @Autowired
    private final UrlService urlService;



    @PostMapping("/api/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(
            @Valid @RequestBody UrlRequest request,
            @AuthenticationPrincipal Object principal) {

        // Extract user email from JWT security context if logged in
        String userEmail = null;
        if (principal != null) {
            userEmail = principal.toString();
        }

        UrlResponse response = urlService.createShortUrl(request, userEmail);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode) {

        String originalUrl = urlService.getOriginal(shortCode);

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }



}