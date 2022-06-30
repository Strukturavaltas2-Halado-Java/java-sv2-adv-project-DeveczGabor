package training.petshop.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PetNotFoundException extends AbstractThrowableProblem {

    public PetNotFoundException(long id) {
        super(
                URI.create("pet/pet-not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Pet not found with id: %d", id)
        );
    }
}
