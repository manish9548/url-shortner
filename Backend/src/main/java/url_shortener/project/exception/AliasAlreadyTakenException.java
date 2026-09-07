package url_shortener.project.exception;

public class AliasAlreadyTakenException extends RuntimeException{
    public AliasAlreadyTakenException(String message){
        super(message);
    }
}
