package com.nimbleways.springboilerplate.features.puchases.api.endpoints.getcoworkerspurchases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.getcoworkerspurchases.GetCoworkersPurchasesUseCase;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GetCoworkersPurchasesEndpoint {
    private static final String URL = "/purchases/coworkerspurchases";

    private final GetCoworkersPurchasesUseCase getCoworkersPurchasesUseCase;

    public GetCoworkersPurchasesEndpoint(GetCoworkersPurchasesUseCase getCoworkersPurchasesUseCase) {
        this.getCoworkersPurchasesUseCase = getCoworkersPurchasesUseCase;
    }

    @GetMapping(URL + "/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public GetCoworkersPurchasesResponse getCoworkersPurchases(@PathVariable("id") String id) {
        UUID userId = UUID.fromString(id);
        ImmutableList<Purchase> purchases = getCoworkersPurchasesUseCase.handle(userId);
        return GetCoworkersPurchasesResponse.from(purchases);
    }
}
