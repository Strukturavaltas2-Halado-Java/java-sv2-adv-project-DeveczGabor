package training.petshop.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PetNotFoundInTheShopException extends AbstractThrowableProblem {

    public PetNotFoundInTheShopException(long id) {
        super(
                URI.create("pet/pet-not-found" + id),
                "Not found",
                Status.NOT_FOUND,
                String.format("Pet not found in the shop with id: %d", id)
        );
    }
}
