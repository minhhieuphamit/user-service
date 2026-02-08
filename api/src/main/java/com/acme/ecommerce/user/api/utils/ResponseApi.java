package com.acme.ecommerce.user.api.utils;

import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import com.acme.ecommerce.user.core.domain.common.ResponseMeta;
import com.acme.ecommerce.user.core.domain.common.ResponseStatus;

public record ResponseApi<T>(ResponseStatus status, T data, ResponseMeta metaData) {

    private ResponseApi(ResponseStatus status, ResponseMeta metaData) {
        this(status, null, metaData);
    }

    public ResponseApi(ResponseStatus status, T data, ResponseMeta metaData) {
        this.status = status;
        this.data = data;
        this.metaData = metaData;
    }

    public static <T> ResponseApi<T> success(T data) {
        return success(new ResponseStatus(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage()), data);
    }

    public static <T> ResponseApi<T> success(ResponseStatus status, T data) {
        return new ResponseApi<>(status, data, ResponseMeta.fromRequestId(null));
    }

    public static <T> ResponseApi<T> error(String errorCode, String errorMessage) {
        ResponseStatus responseStatus = new ResponseStatus(errorCode, errorMessage);
        return new ResponseApi<>(responseStatus, ResponseMeta.fromRequestId(null));
    }

    public static <T> ResponseApi<T> error(String errorCode, String errorMessage, T payload) {
        ResponseStatus responseStatus = new ResponseStatus(errorCode, errorMessage);
        return new ResponseApi<>(responseStatus, payload, ResponseMeta.fromRequestId(null));
    }
}
