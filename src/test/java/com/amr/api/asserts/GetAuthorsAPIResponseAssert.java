package com.amr.api.asserts;

import com.amr.api.model.AuthorValues;
import com.amr.api.model.GetAuthorsAPIResponse;
import com.amr.api.model.GetPublicationsAPIResponse;
import com.amr.api.model.PublicationValues;
import org.assertj.core.api.AbstractAssert;

import java.util.Set;

import static java.util.Collections.disjoint;

public class GetAuthorsAPIResponseAssert extends AbstractAssert<GetAuthorsAPIResponseAssert, GetAuthorsAPIResponse> {

    GetAuthorsAPIResponseAssert(GetAuthorsAPIResponse actual) {
        super(actual, GetAuthorsAPIResponseAssert.class);
    }

    public static GetAuthorsAPIResponseAssert assertThat(GetAuthorsAPIResponse actual) {
        return new GetAuthorsAPIResponseAssert(actual);
    }

    public void hasAuthorValues(Set<AuthorValues> authorValues) {
        isNotNull();
        if(!actual.getAuthors().containsAll(authorValues))
            failWithMessage("Expected authors to contain <%s>, but did not", authorValues.toString());
    }

    public void doesNotHaveAuthorValues(Set<AuthorValues> authorValues) {
        isNotNull();
        if(!disjoint(authorValues, actual.getAuthors()))
            failWithMessage("Expected authors to not contain <%s>, but did", authorValues.toString());
    }

    public void hasNoAuthorValues() {
        isNotNull();
        if(!actual.getAuthors().isEmpty())
            failWithMessage("Expected authors to be empty, but contained <%s>", actual.getAuthors().toString());
    }

}
