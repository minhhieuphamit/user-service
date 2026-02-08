package com.acme.ecommerce.user.api.domain.mapper;

import com.acme.ecommerce.user.api.utils.ResponseApi;
import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import io.vavr.control.Either;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class EitherMapper {

    public <T> ResponseEntity<ResponseApi> eitherMapper(Either<ErrorCode, T> either) {
        return either.fold(
                errorCode ->
                        ResponseEntity.status(errorCode.getHttpStatus())
                                .body(ResponseApi.error(
                                        errorCode.getCode(),
                                        errorCode.getMessage(),
                                        errorCode.getPayloadError())),
                responseApi -> ResponseEntity.ok(ResponseApi.success(responseApi)));
    }
}
