package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import com.nimbleways.springboilerplate.common.utils.collections.Mutable;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserSession;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.RefreshToken;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import org.eclipse.collections.api.map.MutableMap;

public class FakeDatabase {
    final MutableMap<String, UserWithPassword> userTable = Mutable.map.empty();
    final MutableMap<String, Purchase> purchaseTable = Mutable.map.empty();
    final MutableMap<RefreshToken, UserSession> sessionTable = Mutable.map.empty();

    record UserWithPassword(User user, EncodedPassword encodedPassword) {
    }

}
