package url_shortener.project.service;


import url_shortener.project.dto.UrlRequest;
import url_shortener.project.dto.UrlResponse;

import java.time.LocalDateTime;
import java.util.List;

public  interface UrlService {
    UrlResponse createShortUrl(UrlRequest urlRequest, String userEmail);

    //ReturnType methodName(ParameterType parameterName)
    UrlResponse shortenUrl(String originalUrl, LocalDateTime expiresAt,String customAlias);
   String  getOriginal(String shortCode);
    List<UrlResponse> getUrlsByUser(String userEmail);
}
