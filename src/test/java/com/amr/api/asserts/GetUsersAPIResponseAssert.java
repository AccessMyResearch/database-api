package com.amr.api.asserts;

import com.amr.api.model.*;
import org.assertj.core.api.AbstractAssert;

import java.util.Set;

import static java.util.Collections.disjoint;

public class GetUsersAPIResponseAssert extends AbstractAssert<GetUsersAPIResponseAssert, GetUsersAPIResponse> {

    GetUsersAPIResponseAssert(GetUsersAPIResponse actual) {
        super(actual, GetUsersAPIResponseAssert.class);
    }

    public static GetUsersAPIResponseAssert assertThat(GetUsersAPIResponse actual) {
        return new GetUsersAPIResponseAssert(actual);
    }

    public void hasUserValues(Set<UserValues> userValues) {
        isNotNull();
        if(!actual.getUsers().containsAll(userValues))
            failWithMessage("Expected authors to contain <%s>, but did not", userValues.toString());
    }

    public void doesNotHaveUserValues(Set<UserValues> userValues) {
        isNotNull();
        if(!disjoint(userValues, actual.getUsers()))
            failWithMessage("Expected authors to not contain <%s>, but did", userValues.toString());
    }

    public void hasNoUserValues() {
        isNotNull();
        if(!actual.getUsers().isEmpty())
            failWithMessage("Expected authors to be empty, but contained <%s>", actual.getUsers().toString());
    }

}
