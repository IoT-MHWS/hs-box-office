package artgallery.hsboxoffice.exception;

public class TicketDoesNotExistException extends DoesNotExistException {
    public TicketDoesNotExistException(Long id) {
        super(String.format(("ticket %s does not exist"), id));
    }
}
