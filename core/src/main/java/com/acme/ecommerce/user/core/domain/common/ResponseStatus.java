package com.acme.ecommerce.user.core.domain.common;

public record ResponseStatus(String code, String message) {
    public ResponseStatus(String code) {
        this(code, null);
    }
}
