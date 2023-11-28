package artgallery.hsboxoffice.controller.validator;

import lombok.AllArgsConstructor;

/**
 * Violation
 */
@AllArgsConstructor
public class Violation {
    public String field;
    public String violation; // violation description

    //
    @Override
    public String toString() {
        return "{field:" + field + ", violation:" + violation + "}";
    }
}