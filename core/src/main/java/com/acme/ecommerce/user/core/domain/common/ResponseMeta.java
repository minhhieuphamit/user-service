package com.acme.ecommerce.user.core.domain.common;

public record ResponseMeta(String requestId) {
    public static ResponseMeta fromRequestId(String requestId) {
        return new ResponseMeta(requestId);
    }
}
