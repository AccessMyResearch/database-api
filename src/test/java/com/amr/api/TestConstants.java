package com.amr.api;

import com.amr.api.model.*;

import java.util.*;

public class TestConstants {

    public static final Integer validAuthorUserID = 1;

    public static final String validAuthorORCIDID = "orcid_id";

    public static final String validAuthorName = "John Doe";
    public static final String missingAuthorName = "Jane Doe";

    public static final Author validAuthor = new Author(1, validAuthorUserID, validAuthorORCIDID, validAuthorName);

    private static final Set<Author> validAuthorSet = Collections.singleton(validAuthor);
    private static final Set<String> validAuthorNameSet = Collections.singleton(validAuthorName);

    public static final Integer validStartYear = 2010;
    public static final Integer validEndYear = 2020;

    public static final String validPublicationTitle = "Research Paper Title Line";

    public static final String validPublicationDOI = "XY.1234/doi";

    public static final String validPublicationURL = "http://www.accessmyresearch.org";

    public static final String validPublicationDate = "2020-04-11";

    public static final String validPublicationSummary = "This is a long text to represent a publication summary.";

    public static final Publication validPublication = new Publication(1, validPublicationTitle, validPublicationDOI, validPublicationURL, validPublicationDate, validPublicationSummary, validAuthorSet);

    public static final List<Publication> validPublicationList = Collections.singletonList(validPublication);

    public static final PublicationValues validPublicationValues = new PublicationValues(validPublicationTitle, validPublicationDOI, validPublicationURL, validPublicationDate, validPublicationSummary, validAuthorNameSet);

//    public static final List<PublicationValues> validPublicationValuesList = Collections.singletonList(validPublicationValues);

    private static final List<PublicationValues> publicationValues = new ArrayList<>();
    private static final List<AuthorValues> authorValues = new ArrayList<>();
    private static final List<UserValues> userValues = new ArrayList<>();

    public static final GetPublicationsAPIResponse getPublicationsAPIResponse = new GetPublicationsAPIResponse(publicationValues);
    public static final GetAuthorsAPIResponse getAuthorsAPIResponse = new GetAuthorsAPIResponse(authorValues);
    public static final GetUsersAPIResponse getUsersAPIResponse = new GetUsersAPIResponse(userValues);

}
