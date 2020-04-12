package com.amr.api.asserts;

import com.amr.api.model.GetPublicationsAPIResponse;
import com.amr.api.model.PublicationValues;
import org.assertj.core.api.AbstractAssert;

import java.util.Collections;
import java.util.Set;

import static java.util.Collections.disjoint;

public class GetPublicationsAPIResponseAssert extends AbstractAssert<GetPublicationsAPIResponseAssert, GetPublicationsAPIResponse> {

    GetPublicationsAPIResponseAssert(GetPublicationsAPIResponse actual) {
        super(actual, GetPublicationsAPIResponseAssert.class);
    }

    public static GetPublicationsAPIResponseAssert assertThat(GetPublicationsAPIResponse actual) {
        return new GetPublicationsAPIResponseAssert(actual);
    }

    public void hasPublicationValues(Set<PublicationValues> publicationValues) {
        isNotNull();
        if(!actual.getPublications().containsAll(publicationValues))
            failWithMessage("Expected publications to contain <%s>, but did not", publicationValues.toString());
    }

    public void doesNotHavePublicationValues(Set<PublicationValues> publicationValues) {
        isNotNull();
        if(!disjoint(publicationValues, actual.getPublications()))
            failWithMessage("Expected publications to not contain <%s>, but did", publicationValues.toString());
    }

    public void hasNoPublicationValues() {
        isNotNull();
        if(!actual.getPublications().isEmpty())
            failWithMessage("Expected publications to be empty, but contained <%s>", actual.getPublications().toString());
    }

}
