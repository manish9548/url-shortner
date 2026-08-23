package url_shortener.project.service;




public interface UrlService {
    //ReturnType methodName(ParameterType parameterName)
   String shortenUrl(String originalUrl);
   String  getOriginal(String shortCode);
}
