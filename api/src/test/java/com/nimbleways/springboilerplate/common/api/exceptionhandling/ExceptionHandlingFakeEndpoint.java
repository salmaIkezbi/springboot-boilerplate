package com.nimbleways.springboilerplate.common.api.exceptionhandling;

import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestController
@Setter
class ExceptionHandlingFakeEndpoint {
    public static final String EXCEPTION_GET_ENDPOINT = "/exception-handling/throw";
    public static final String PERMIT_ALL_GET_ENDPOINT = "/exception-handling/get";
    public static final String AUTHENTICATED_GET_ENDPOINT = "/exception-handling/auth_get";
    public static final String POST_WITH_BODY_ENDPOINT = "/exception-handling/post";
    public static final String POST_WITH_BODY_FAILING_TYPE_LEVEL_VALIDATION = "/exception-handling/complex/post";

    private Exception exceptionToThrow;

    @SneakyThrows
    @GetMapping(EXCEPTION_GET_ENDPOINT)
    public void throwException() {
        throw exceptionToThrow;
    }

    @GetMapping(PERMIT_ALL_GET_ENDPOINT)
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> getEndpoint() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(AUTHENTICATED_GET_ENDPOINT)
    @PreAuthorize("isAuthenticated()")
    public void authenticatedGetEndpoint() {
    }

    @PostMapping(POST_WITH_BODY_ENDPOINT)
    public void postEndpoint(@RequestBody @Valid final MyRequest myRequest) {
    }

    @PostMapping(POST_WITH_BODY_FAILING_TYPE_LEVEL_VALIDATION)
    public void postComplexValidationEndpoint(@RequestBody @Valid final ExceptionHandlingFakeEndpoint.RequestWithAlwaysFailingTypeLevelValidation myRequest) {
    }

    record MyRequest(
        @NotBlank
        String notBlankString,

        @NotNull
        Integer requiredInt
    ) {}

    @AlwaysFailingTypeLevelValidation
    public record RequestWithAlwaysFailingTypeLevelValidation() {}

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AlwaysFailingTypeLevelValidation.TypeLevelValidator.class)
    public @interface AlwaysFailingTypeLevelValidation {
        String message() default "Type-level validation failed";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

        class TypeLevelValidator implements ConstraintValidator<AlwaysFailingTypeLevelValidation, RequestWithAlwaysFailingTypeLevelValidation> {
            @Override
            public boolean isValid(RequestWithAlwaysFailingTypeLevelValidation order, ConstraintValidatorContext constraintContext) {
                return false;
            }
        }
    }
}
