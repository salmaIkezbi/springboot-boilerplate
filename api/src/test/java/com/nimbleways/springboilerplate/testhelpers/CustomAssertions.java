package com.nimbleways.springboilerplate.testhelpers;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomAssertions {

    public static <T> T assertPresent(Optional<T> optional) {
        assertTrue(optional.isPresent());
        return optional.orElseThrow();
    }
}