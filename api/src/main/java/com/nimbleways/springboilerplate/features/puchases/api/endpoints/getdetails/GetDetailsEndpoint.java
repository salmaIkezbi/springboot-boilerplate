package com.nimbleways.springboilerplate.features.puchases.api.endpoints.getdetails;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.getdetails.GetDetailsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GetDetailsEndpoint {
    private static final String URL = "/purchase";

    private final GetDetailsUseCase getDetailsUseCase;

    public GetDetailsEndpoint(GetDetailsUseCase getDetailsUseCase) {
        this.getDetailsUseCase = getDetailsUseCase;
    }

    @GetMapping(URL + "/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public GetDetailsResponse getDetails(@PathVariable("id") String id) {
        UUID purchaseId = UUID.fromString(id);
        Purchase purchase = getDetailsUseCase.handle(purchaseId);
        return GetDetailsResponse.from(purchase);
    }
}
