package com.amr.api.service;

import com.amr.api.dao.DAO;
import com.amr.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crossref.CrossrefWorksResponse;
import org.crossref.DOI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.amr.Utils.isNullOrEmpty;
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
        List<Publication> ret = addPublications(Collections.singletonList(request));
        return isNullOrEmpty(ret) ? null : ret.get(0);
    }

    @Override
    public List<Publication> addPublications(List<AddPublicationRequest> requests) {
        Set<String> autofillDois = requests.stream()
                .filter(AddPublicationRequest::isAutofill)
                .map(AddPublicationRequest::getDoi)
                .map(DOI::canonical).collect(Collectors.toSet());
        Map<String, CrossrefWorksResponse.WorksList.Item> autofillDataResponse;
        if (autofillDois.isEmpty()) {
            autofillDataResponse = Collections.emptyMap();
        } else {
            try {
                autofillDataResponse = getExternalPublicationsInfo(autofillDois);
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace(System.err);
                return null; // TODO better error handling
            }
        }
        return dao.addPublications(requests.stream()
                .map((AddPublicationRequest request) -> {
                    String title = request.getTitle();
                    if (!isNullOrEmpty(title))
                        title = title.trim();
                    String doi = DOI.canonical(request.getDoi());
                    String url = request.getUrl();
                    if (!isNullOrEmpty(url))
                        url = url.trim();
                    String pubDate = request.getPublicationDate();
                    if (!isNullOrEmpty(pubDate))
                        pubDate = pubDate.trim();
                    String summary = request.getSummary();
                    if (!isNullOrEmpty(summary))
                        summary = summary.trim();

                    if (request.isAutofill()) {
                        final CrossrefWorksResponse.WorksList.Item autofillData = autofillDataResponse.get(doi);
                        if (isNull(autofillData))
                            return null; // TODO better error handling

                        if (isNullOrEmpty(title)) {
                            if (!isNullOrEmpty(autofillData.getTitles())) {
                                title = (autofillData.getTitles().get(0));
                            }
                        }
                        // URL is not autofilled b/c Crossref links to publisher-provided URLs (AKA not open-access)
                        if (isNullOrEmpty(pubDate)) {
                            if (!isNull(autofillData.getPublicationDate()) &&
                                    !isNullOrEmpty(autofillData.getPublicationDate().getDateParts()) &&
                                    !isNullOrEmpty(autofillData.getPublicationDate().getDateParts().get(0))) {
                                final List<Integer> dateParts = autofillData.getPublicationDate().getDateParts().get(0);
                                while (dateParts.size() < 3)
                                    dateParts.add(0);
                                pubDate = String.format("%04d-%02d-%02d", dateParts.get(0), dateParts.get(1), dateParts.get(2));
                            }
                        }
                        if (isNullOrEmpty(summary)) {
                            if (!isNullOrEmpty(autofillData.getSummary()))
                                summary = autofillData.getSummary();
                        }
                    }
                    return new Publication(null, title, doi, url, pubDate, summary, null);
                }).filter(Objects::nonNull)
                ::iterator);
    }

    private Map<String, CrossrefWorksResponse.WorksList.Item> getExternalPublicationsInfo(Collection<String> dois) throws IOException {
        final List<Map.Entry<String, String>> filterParams = dois.stream()
                .map(DOI::canonical)
                .filter((String doi) -> !isNullOrEmpty(doi))
                .map((String doi) -> new AbstractMap.SimpleImmutableEntry<String, String>("doi", doi))
                .collect(Collectors.toList());
        final Map<String, String> params = new HashMap<>();
        params.put("rows", "" + filterParams.size());
        CrossrefWorksResponse response = CrossrefWorksResponse.fromWeb(params, filterParams);

        if (isNullOrEmpty(response.getMessageData().getItems()))
            return new HashMap<>(0);
        Map<String, CrossrefWorksResponse.WorksList.Item> ret = new HashMap<>();
        for (final CrossrefWorksResponse.WorksList.Item item : response.getMessageData().getItems())
            ret.putIfAbsent(item.getDoi(), item);
        while (ret.size() < response.getMessageData().getTotalResultCount()) {
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
            publications.sort(Comparator.comparingInt((Publication pub) -> keywordSearchWeightFunction(pub, request.getSearchKeywords())));
        return publicationToPublicationValue(publications);
    }

    @Override
    public UserValues getUsersByOpenId(String openId) {
        User user = dao.getUserByOpenId(openId);
        return new UserValues(user.getEmail(), user.getOrcidId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getOpenId());
    }

    private static int keywordSearchWeightFunction(Publication publication, Set<String> keywords) {
        return keywords.stream()
                .mapToInt(keyword -> {
                    int titleCount = 0;
                    int fromIndex = 0;
                    while ((fromIndex = publication.getTitle().indexOf(keyword, fromIndex)) != -1) {
                        ++titleCount;
                        fromIndex += keyword.length();
                    }
                    int summaryCount = 0;
                    fromIndex = 0;
                    while ((fromIndex = publication.getSummary().indexOf(keyword, fromIndex)) != -1) {
                        ++summaryCount;
                        fromIndex += keyword.length();
                    }
                    return titleCount + summaryCount;
                }).sum();
    }

    private GetPublicationsAPIResponse publicationToPublicationValue(List<Publication> publications) {
        List<PublicationValues> publicationValues = publications.stream().map(publication -> {
            Set<String> authorNames = publication.getAuthors().stream().map(Author::getName).collect(Collectors.toSet());
            return new PublicationValues(publication.getTitle(), publication.getDoi(), publication.getUrl(), publication.getPublicationDate(), publication.getSummary(), authorNames);
        }).collect(Collectors.toList());
        return new GetPublicationsAPIResponse(publicationValues);
    }
}
