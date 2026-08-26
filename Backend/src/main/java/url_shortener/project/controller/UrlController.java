package url_shortener.project.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import url_shortener.project.dto.UrlRequest;
import url_shortener.project.service.UrlService;

@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;



    @PostMapping("/api/shorten")
    public String SendOriginalUrl(@RequestBody UrlRequest request) {

        String shortCode = urlService.shortenUrl(request.getOriginalUrl());

        return shortCode;
    }
    @GetMapping("/api/{shortCode}")
    public String getOriginalUrl(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginal(shortCode);
        return originalUrl;

    }



}