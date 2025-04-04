package com.nimbleways.springboilerplate.common.api.exceptionhandling;

import com.nimbleways.springboilerplate.common.domain.ports.EventPublisherPort;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.AbstractAuthenticationDomainException;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.BadUserCredentialException;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.UnknownEmailException;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.EmailAlreadyExistsInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseResponseEntityExceptionHandler {

    protected GlobalExceptionHandler(EventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @Nullable
    public final ResponseEntity<Object> handleException(AccessDeniedException ex, WebRequest request) {
        if (request.getUserPrincipal() == null) {
            return getDefaultResponseEntity(ex, request, HttpStatus.UNAUTHORIZED, ApiErrorCodes.UNAUTHORIZED_ERROR);
        }
        return getDefaultResponseEntity(ex, request, HttpStatus.FORBIDDEN, ApiErrorCodes.ACCESS_DENIED_ERROR);
    }

    @ExceptionHandler({ AbstractAuthenticationDomainException.class })
    @Nullable
    public final ResponseEntity<Object> handleException(AbstractAuthenticationDomainException ex, WebRequest request) {
        return getDefaultResponseEntity(ex, request, HttpStatus.UNAUTHORIZED, ApiErrorCodes.UNAUTHORIZED_ERROR);
    }

    @ExceptionHandler({ EmailAlreadyExistsInRepositoryException.class })
    @Nullable
    public final ResponseEntity<Object> handleException(EmailAlreadyExistsInRepositoryException ex,
                                                        WebRequest request) {
        return getDefaultResponseEntity(ex, request, HttpStatus.BAD_REQUEST, ApiErrorCodes.EMAIL_ALREADY_EXISTS_ERROR);
    }

    @ExceptionHandler({ UserNotFoundInRepositoryException.class })
    @Nullable
    public final ResponseEntity<Object> handleException(UserNotFoundInRepositoryException ex,
                                                        WebRequest request) {
        return getDefaultResponseEntity(ex, request, HttpStatus.NOT_FOUND, ApiErrorCodes.USER_NOT_FOUND_ERROR);
    }

    @ExceptionHandler({ UnknownEmailException.class })
    @Nullable
    public final ResponseEntity<Object> handleException(UnknownEmailException ex,
                                                        WebRequest request) {
        return getDefaultResponseEntity(ex, request, HttpStatus.UNAUTHORIZED, ApiErrorCodes.EMAIL_NOT_FOUND_ERROR);
    }

    @ExceptionHandler({ BadUserCredentialException.class })
    @Nullable
    public final ResponseEntity<Object> handleException(BadUserCredentialException ex,
                                                        WebRequest request) {
        return getDefaultResponseEntity(ex, request, HttpStatus.UNAUTHORIZED, ApiErrorCodes.BAD_CREDENTIAL_ERROR);
    }

    @ExceptionHandler({ Exception.class })
    @Nullable
    public final ResponseEntity<Object> catchAll(Exception ex, WebRequest request) {
        return getDefaultResponseEntity(ex, request, HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorCodes.INTERNAL_SERVER_ERROR);
    }
}
