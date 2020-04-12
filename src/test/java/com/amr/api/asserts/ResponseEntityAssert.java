package com.amr.api.asserts;

import org.assertj.core.api.AbstractAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class ResponseEntityAssert extends AbstractAssert<ResponseEntityAssert, ResponseEntity> {

    ResponseEntityAssert(ResponseEntity actual) {
        super(actual, ResponseEntityAssert.class);
    }

    public static ResponseEntityAssert assertThat(ResponseEntity actual) {
        return new ResponseEntityAssert(actual);
    }

    public void hasStatusCode(HttpStatus status) {
        isNotNull();
        if(!Objects.equals(status, actual.getStatusCode()))
            failWithMessage("Expected HTTPStatus Code to be <%s>, but was <%s>", status.toString(), actual.getStatusCode());
    }

    public void hasBody(Object body) {
        isNotNull();
        if(!Objects.equals(body, actual.getBody()))
            failWithMessage("Expected Response Body to be <%s>, but was <%s>", body.toString(), actual.getBody().toString());
    }

    public void doesNotHaveBody() {
        isNotNull();
        if(!Objects.isNull(actual.getBody()))
            failWithMessage("Expected Response Body to be null, but was <%s>", actual.getBody().toString());
    }
}
