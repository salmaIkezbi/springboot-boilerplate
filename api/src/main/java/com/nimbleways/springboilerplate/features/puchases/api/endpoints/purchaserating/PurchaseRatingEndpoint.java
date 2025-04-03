package com.nimbleways.springboilerplate.features.puchases.api.endpoints.purchaserating;


import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating.PurchaseRatingCommand;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating.PurchaseRatingUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PurchaseRatingEndpoint {
    private static final String URL = "/purchases";

    private final PurchaseRatingUseCase purchaseRatingUseCase;

    PurchaseRatingEndpoint(PurchaseRatingUseCase purchaseRatingUseCase) {
        this.purchaseRatingUseCase = purchaseRatingUseCase;
    }

    @PutMapping(URL + "/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseRatingResponse ratePurchase(@PathVariable("id") String id, @Valid @RequestBody RatingRequest ratingRequest) {
        UUID purchaseId = UUID.fromString(id);

        // Create rating object from request and path variable
        PurchaseRatingCommand rating = ratingRequest.toPurchaseRatingCommand(purchaseId);

        // Pass the rating object to the use case
        Purchase ratedPurchase = purchaseRatingUseCase.handle(rating);

        return PurchaseRatingResponse.from(ratedPurchase);
    }
}
