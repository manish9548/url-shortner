package url_shortener.project.service;


import url_shortener.project.dto.UrlResponse;

import java.time.LocalDateTime;

public  interface UrlService {
    //ReturnType methodName(ParameterType parameterName)
    UrlResponse shortenUrl(String originalUrl, LocalDateTime expiresAt);
   String  getOriginal(String shortCode);
}
