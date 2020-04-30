package com.amr.api.asserts;

import com.amr.api.model.GetAuthorsAPIResponse;
import com.amr.api.model.GetPublicationsAPIResponse;
import com.amr.api.model.GetUsersAPIResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.ResponseEntity;

public class TestAssertions extends Assertions {

    public static ResponseEntityAssert assertThat(ResponseEntity actual) {
        return new ResponseEntityAssert(actual);
    }

    public static GetPublicationsAPIResponseAssert assertThat(GetPublicationsAPIResponse actual) {
        return new GetPublicationsAPIResponseAssert(actual);
    }

    public static GetAuthorsAPIResponseAssert assertThat(GetAuthorsAPIResponse actual) {
        return new GetAuthorsAPIResponseAssert(actual);
    }

    public static GetUsersAPIResponseAssert assertThat(GetUsersAPIResponse actual) {
        return new GetUsersAPIResponseAssert(actual);
    }

}
