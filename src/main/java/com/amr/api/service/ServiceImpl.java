package com.amr.api.service;

import com.amr.api.dao.DAO;
import com.amr.api.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crossref.CrossrefWorksResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements com.amr.api.service.Service {
    private final DAO dao;

    @Override
    public GetPublicationsAPIResponse getPublications() {
        List<Publication> publications = dao.getPublications();
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsByAuthor(String authorName) {
        List<Publication> publications = dao.getPublicationsByAuthor(authorName);
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsRecent() {
        List<Publication> publications = dao.getPublicationsRecent();
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsMostViewed() {
        return null;
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsByYear(Integer startYear, Integer endYear) {
        List<Publication> publications = dao.getPublicationsByYear(startYear, endYear);
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetAuthorsAPIResponse getAuthors() {
        List<Author> authors = dao.getAuthors();
        List<AuthorValues> authorValues = authors.stream().map(author -> new AuthorValues(author.getUserId(), author.getOrcidId(), author.getName())).collect(Collectors.toList());
        return new GetAuthorsAPIResponse(authorValues);
    }

    @Override
    public GetUsersAPIResponse getUsers() {
        List<User> users = dao.getUsers();
        List<UserValues> userValues = users.stream().map(user -> new UserValues(user.getEmail(), user.getOrcidId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getOpenId())).collect(Collectors.toList());
        return new GetUsersAPIResponse(userValues);
    }

    @Override
    public Publication addPublication(AddPublicationRequest request) {
        // normalize DOI
        String title = request.getTitle();
        String doi = request.getDoi();
        String url = request.getUrl();
        String pubDate = request.getPublicationDate();
        String summary = request.getSummary();
        if (!isNull(title)) {
            title = title.trim();
        }
        if (!isNull(doi)) {
            doi = doi.trim();
            if (doi.endsWith("/"))
                doi = doi.substring(0, doi.length() - 1);
            doi = doi.substring(doi.lastIndexOf("doi.org/") + 1);
        }
        if (!isNull(url)) {
            url = url.trim();
        }
        if (!isNull(pubDate)) {
            pubDate = pubDate.trim();
        }
        if (!isNull(summary)) {
            summary = summary.trim();
        }

        if (request.isAutofill()) {
            List<CrossrefWorksResponse.WorksList.Item> autofillDataResponse;
            try {
                autofillDataResponse = getExternalPublicationsInfo(Collections.singleton(request.getDoi()));
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace(System.err);
                return null; // TODO better error handling
            }
            if (autofillDataResponse.size() >= 1) {
                final CrossrefWorksResponse.WorksList.Item autofillData = autofillDataResponse.get(0);
                if (isNull(title) || title.isEmpty()) {
                    if (!isNull(autofillData.getTitles()) && autofillData.getTitles().size() >= 1) {
                        title = (autofillData.getTitles().get(0));
                    }
                }
                // URL is not autofilled b/c Crossref links to publisher-provided URLs (AKA not open-access)
                if (isNull(pubDate)) {
                    if (!isNull(autofillData.getPublicationDate()) &&
                            !isNull(autofillData.getPublicationDate().getDateParts()) &&
                            autofillData.getPublicationDate().getDateParts().size() >= 1 &&
                            !isNull(autofillData.getPublicationDate().getDateParts().get(0)) &&
                            autofillData.getPublicationDate().getDateParts().get(0).size() >= 1) {
                        final List<Integer> dateParts = autofillData.getPublicationDate().getDateParts().get(0);
                        while (dateParts.size() < 3)
                            dateParts.add(0);
                        pubDate = String.format("%04d-%02d-%02d", dateParts.get(0), dateParts.get(1), dateParts.get(2));
                    }
                }
                if (isNull(summary) || summary.isEmpty()) {
                    if (!isNull(autofillData.getSummary()))
                        summary = autofillData.getSummary();
                }
            }
        }
        Publication publication = new Publication(null, title, doi, url, pubDate, summary,null);
        return dao.addPublication(publication);
    }

    private List<CrossrefWorksResponse.WorksList.Item> getExternalPublicationsInfo(Collection<String> dois) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        final String queryBase = "https://api.crossref.org/works?mailto=mehmet@accessmyresearch.org";
        final String filterParam = dois.stream()
                .map((doi) -> {
                    doi = doi.trim();
                    if (doi.endsWith("/"))
                        doi = doi.substring(0, doi.length() - 1);
                    doi = doi.substring(doi.lastIndexOf("doi.org/") + 1);
                    return doi;
                })
                .filter((String doi) -> !isNull(doi) && !dois.isEmpty())
                .map((String doi) -> "doi:" + URLEncoder.encode(doi))
                .collect(Collectors.joining(","));
        final URL url = new URL(queryBase + "&filter=" + filterParam + "&rows=" + dois.size());
        final HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");
        request.connect();
        // TODO honor these headers
//        request.getHeaderField("X-Rate-Limit-Limit");
//        request.getHeaderField("X-Rate-Limit-Interval");
        final CrossrefWorksResponse result = mapper.readValue(
                request.getInputStream(),
                CrossrefWorksResponse.class);
        request.disconnect();
        if (result.getMessageData().getItems() == null || result.getMessageData().getItems().size() == 0)
            return new ArrayList<>(0);
        List<CrossrefWorksResponse.WorksList.Item> ret = new ArrayList<>((int)result.getMessageData().getTotalResultCount());
        ret.addAll(result.getMessageData().getItems());
        while (ret.size() < result.getMessageData().getTotalResultCount()) {
            break; // TODO handle partial requests
        }
        return ret;
    }

    @Override
    public User addUser(AddUserRequest addUserRequest) {
        User user = new User(null, addUserRequest.getEmail(), addUserRequest.getOrcidId(), addUserRequest.getFirstName(), addUserRequest.getMiddleName(), addUserRequest.getLastName(), addUserRequest.getOpenId());
        return dao.addUser(user);
    }

    @Override
    public Author addAuthor(AddAuthorRequest addAuthorRequest) {
        Author author = new Author(null, addAuthorRequest.getUserId(), addAuthorRequest.getOrcidId(), addAuthorRequest.getName());
        return dao.addAuthor(author);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsFilter(GetPublicationsRequest request) {
        List<Publication> publications = dao.getPublicationsFilter(request.getFilters());
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsNew(GetPublicationsRequest request) {
        List<Publication> publications = dao.getPublicationsNew(request);
        if (!isNull(request.getSearchKeywords()))
            publications.sort((publication1, publication2) -> keywordSearchComparator(publication1, publication2, request.getSearchKeywords()));
        return publicationToPublicationValue(publications);
    }

    @Override
    public UserValues getUsersByOpenId(String openId) {
        User user = dao.getUserByOpenId(openId);
        return new UserValues(user.getEmail(), user.getOrcidId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getOpenId());
    }

    private static int keywordSearchComparator(Publication publication1, Publication publication2, Set<String> keywords) {
        return keywordSearchWeightFunction(publication2, keywords) - keywordSearchWeightFunction(publication1, keywords);
    }

    private static int keywordSearchWeightFunction(Publication publication, Set<String> keywords) {
        return keywords.stream()
                .map(keyword -> {
                    int count = 0;
                    int fromIndex = 0;
                    while ((fromIndex = publication.getTitle().indexOf(keyword, fromIndex)) != -1) {
                        count++;
                        fromIndex++;
                    }
                    fromIndex = 0;
                    while ((fromIndex = publication.getSummary().indexOf(keyword, fromIndex)) != -1) {
                        count++;
                        fromIndex++;
                    }
                    return count;
                })
                .reduce(0, (sum, count) -> sum += count);
    }

    private GetPublicationsAPIResponse publicationToPublicationValue(List<Publication> publications) {
        List<PublicationValues> publicationValues = publications.stream().map(publication -> {
            Set<String> authorNames = publication.getAuthors().stream().map(Author::getName).collect(Collectors.toSet());
            return new PublicationValues(publication.getTitle(), publication.getDoi(), publication.getUrl(), publication.getPublicationDate(), publication.getSummary(), authorNames);
        }).collect(Collectors.toList());
        return new GetPublicationsAPIResponse(publicationValues);
    }
}
