package com.nimbleways.springboilerplate.features.puchases.domain.ports;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;


public interface PurchaseRepositoryPort {
    ImmutableList<Purchase> findByUserID(UUID id);

}
