package artgallery.hsboxoffice.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import artgallery.hsboxoffice.dto.TicketDTO;

public class TicketValidator implements Validator {

    @Override
    public boolean supports(Class<?> cl) {
        return TicketDTO.class.equals(cl);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TicketDTO ticket = (TicketDTO) target;
        if (ticket.getPrice() != null && ticket.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be a positive number");
        }
    }
}
