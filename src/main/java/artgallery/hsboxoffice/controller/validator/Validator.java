package artgallery.hsboxoffice.controller.validator;


import java.util.LinkedList;
import java.util.List;

/**
 * Validator
 */
public class Validator {

    private final List<Violation> violations;

    public Validator() {
        violations = new LinkedList<Violation>();
    }

    protected void addViolation(String field, String violation) {
        violations.add(new Violation(field, violation));
    }

    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    public String getDescription() {
        return violations.toString();
    }
}
