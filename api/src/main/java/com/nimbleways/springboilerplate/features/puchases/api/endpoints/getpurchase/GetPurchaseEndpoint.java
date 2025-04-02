package com.nimbleways.springboilerplate.features.puchases.api.endpoints.getpurchase;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.getpurchase.GetPurchaseUseCase;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GetPurchaseEndpoint {
    private static final String URL = "/purchases";

    private final GetPurchaseUseCase getPurchaseUseCase;


    public GetPurchaseEndpoint(GetPurchaseUseCase getPurchaseUseCase) {
        this.getPurchaseUseCase = getPurchaseUseCase;
    }

    @GetMapping(URL + "/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public GetPurchaseResponse getPurchase(@PathVariable("id") String id) {
        UUID userId = UUID.fromString(id);
        ImmutableList<Purchase> purchases = getPurchaseUseCase.handle(userId);
        return GetPurchaseResponse.from(purchases);
    }
}
