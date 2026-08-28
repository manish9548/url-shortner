package url_shortener.project.service;


import url_shortener.project.dto.UrlResponse;

public  interface UrlService {
    //ReturnType methodName(ParameterType parameterName)
   UrlResponse shortenUrl(String originalUrl);
   String  getOriginal(String shortCode);
}
