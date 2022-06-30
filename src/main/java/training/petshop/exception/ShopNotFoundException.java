package training.petshop.exception;


import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ShopNotFoundException extends AbstractThrowableProblem {

    public ShopNotFoundException(long id) {
        super(
                URI.create("shop/shop-not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Shop not found with id: %d", id)
        );
    }
}
