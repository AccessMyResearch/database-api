package com.amr.api.asserts;

import com.amr.api.model.GetPublicationsAPIResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.ResponseEntity;

public class TestAssertions extends Assertions {

    public static ResponseEntityAssert assertThat(ResponseEntity actual) {
        return new ResponseEntityAssert(actual);
    }

    public static GetPublicationsAPIResponseAssert assertThat(GetPublicationsAPIResponse actual) {
        return new GetPublicationsAPIResponseAssert(actual);
    }

}
