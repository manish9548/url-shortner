package url_shortener.project.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import url_shortener.project.dto.UrlRequest;
import url_shortener.project.service.UrlService;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public String SendOriginalUrl(@RequestBody UrlRequest request) {

        String shortCode = urlService.shortenUrl(request.getOriginalUrl());

        return shortCode;
    }
}