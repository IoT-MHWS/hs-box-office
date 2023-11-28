package artgallery.hsboxoffice.exception;

public class OrderDoesNotExistException extends DoesNotExistException {
    public OrderDoesNotExistException(Long id) {
        super(String.format(("order %s does not exist"), id));
    }
}