package com.amr.api;

import com.amr.api.model.*;

import java.util.*;

public class TestConstants {

    public static final Integer validAuthorUserID = 1;
    public static final Integer otherAuthorUserID = 2;

    public static final String validAuthorORCIDID = "orcid_id";
    public static final String otherAuthorORCIDID = "other_orcid_id";

    public static final String validAuthorName = "John Doe";
    public static final String otherAuthorName = "Jack Doe";
    public static final String missingAuthorName = "Jane Doe";

    public static final Author validAuthor = new Author(1, validAuthorUserID, validAuthorORCIDID, validAuthorName);
    public static final Author otherAuthor = new Author(2, otherAuthorUserID, otherAuthorORCIDID, otherAuthorName);

    public static final List<Author> validAuthorList = Collections.singletonList(validAuthor);

    private static final Set<Author> validAuthorSet = Collections.singleton(validAuthor);
    private static final Set<Author> otherAuthorSet = Collections.singleton(otherAuthor);

    private static final Set<String> validAuthorNameSet = Collections.singleton(validAuthorName);
    private static final Set<String> otherAuthorNameSet = Collections.singleton(otherAuthorName);

    public static final AuthorValues validAuthorValues = new AuthorValues(validAuthorUserID, validAuthorORCIDID, validAuthorName);
    public static final AuthorValues otherAuthorValues = new AuthorValues(otherAuthorUserID, otherAuthorORCIDID, otherAuthorName);

    public static final Integer validStartYear = 2010;
    public static final Integer validEndYear = 2020;

    public static final String validPublicationTitle = "Research Paper Title Line";

    public static final String validPublicationDOI = "XY.1234/doi";

    public static final String validPublicationURL = "http://www.accessmyresearch.org";

    public static final String validPublicationDate = "2020-04-11";
    public static final String beforeRangePublicationDate = "2000-04-11";
    public static final String afterRangePublicationDate = "2030-04-11";
    public static final String endOfRangePublicationDate = "2020-12-31";

    public static final String validPublicationSummary = "This is a long text to represent a publication summary.";

    public static final Publication validPublication = new Publication(1, validPublicationTitle, validPublicationDOI, validPublicationURL, validPublicationDate, validPublicationSummary, validAuthorSet);
    public static final Publication beforeDateRangePublication = new Publication(2, validPublicationTitle, validPublicationDOI, validPublicationURL, beforeRangePublicationDate, validPublicationSummary, validAuthorSet);
    public static final Publication afterDateRangePublication = new Publication(3, validPublicationTitle, validPublicationDOI, validPublicationURL, afterRangePublicationDate, validPublicationSummary, validAuthorSet);
    public static final Publication endOfDateRangePublication = new Publication(4, validPublicationTitle, validPublicationDOI, validPublicationURL, endOfRangePublicationDate, validPublicationSummary, validAuthorSet);
    public static final Publication otherAuthorPublication = new Publication(5, validPublicationTitle, validPublicationDOI, validPublicationURL, validPublicationDate, validPublicationSummary, otherAuthorSet);

    public static final List<Publication> allPublicationList = Arrays.asList(validPublication, beforeDateRangePublication, afterDateRangePublication, endOfDateRangePublication, otherAuthorPublication);
    public static final List<Publication> dateRangePublicationList = Arrays.asList(validPublication, endOfDateRangePublication, otherAuthorPublication);

    public static final PublicationValues validPublicationValues = new PublicationValues(validPublicationTitle, validPublicationDOI, validPublicationURL, validPublicationDate, validPublicationSummary, validAuthorNameSet);
    public static final PublicationValues beforeDateRangePublicationValues = new PublicationValues(validPublicationTitle, validPublicationDOI, validPublicationURL, beforeRangePublicationDate, validPublicationSummary, validAuthorNameSet);
    public static final PublicationValues afterDateRangePublicationValues = new PublicationValues(validPublicationTitle, validPublicationDOI, validPublicationURL, afterRangePublicationDate, validPublicationSummary, validAuthorNameSet);
    public static final PublicationValues endOfDateRangePublicationValues = new PublicationValues(validPublicationTitle, validPublicationDOI, validPublicationURL, endOfRangePublicationDate, validPublicationSummary, validAuthorNameSet);
    public static final PublicationValues otherAuthorPublicationValues = new PublicationValues(validPublicationTitle, validPublicationDOI, validPublicationURL, validPublicationDate, validPublicationSummary, otherAuthorNameSet);

    private static final List<PublicationValues> publicationValues = new ArrayList<>();
    private static final List<AuthorValues> authorValues = new ArrayList<>();
    private static final List<UserValues> userValues = new ArrayList<>();

    public static final GetPublicationsAPIResponse getPublicationsAPIResponse = new GetPublicationsAPIResponse(publicationValues);
    public static final GetAuthorsAPIResponse getAuthorsAPIResponse = new GetAuthorsAPIResponse(authorValues);
    public static final GetUsersAPIResponse getUsersAPIResponse = new GetUsersAPIResponse(userValues);

}
