package org.crossref;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static com.amr.Utils.isNullOrEmpty;

/**
 * A JSON object that maps to the result of a GET request made to https://api.crossref.org/works
 */
@Data
public class CrossrefWorksResponse {
    private static ObjectMapper jsonMapper;
    static {
        jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public static CrossrefWorksResponse fromWeb(final Map<String, String> params) throws IOException {
        return fromWeb(params, Collections.emptySet());
    }

    public static CrossrefWorksResponse fromWeb(final Map<String, String> params, final Map<String, String> filterParams) throws IOException {
        return fromWeb(params, filterParams.entrySet());
    }

    public static CrossrefWorksResponse fromWeb(final Map<String, String> params, final Collection<Map.Entry<String, String>> filterParams) throws IOException {
        final StringBuilder query = new StringBuilder("https://api.crossref.org/works?");
        if (isNullOrEmpty(params)) {
            query.append("mailto=")
                    .append(URLEncoder.encode("mehmet@accessmyresearch.org", "UTF-8"))
                    .append("&");
        } else {
            if (!params.containsKey("mailto")) {
                query.append("mailto=")
                        .append(URLEncoder.encode("mehmet@accessmyresearch.org", "UTF-8"))
                        .append("&");
            }
            params.entrySet().stream()
                    .forEach((Map.Entry<String, String> param) -> {
                        try {
                            if (!"filter".equals(param.getKey()) || isNullOrEmpty(filterParams)) {
                                query.append(param.getKey())
                                        .append("=")
                                        .append(URLEncoder.encode(param.getValue(), "UTF-8"))
                                        .append("&");
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new IllegalStateException(e); // this should never occur
                        }
                    });
        }
        if (!isNullOrEmpty(filterParams)) {
            query.append("filter=");
            filterParams.stream()
                    .forEach((Map.Entry<String, String> param) -> {
                        try {
                            query.append(param.getKey())
                                    .append(":")
                                    .append(URLEncoder.encode(param.getValue(), "UTF-8"))
                                    .append(",");
                        } catch (UnsupportedEncodingException e) {
                            throw new IllegalStateException(e); // this should never occur
                        }
                    });
        }
        final HttpURLConnection request = (HttpURLConnection) new URL(query.toString()).openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");
        request.connect();

        // TODO honor these headers
//        request.getHeaderField("X-Rate-Limit-Limit");
//        request.getHeaderField("X-Rate-Limit-Interval");
        final CrossrefWorksResponse result = jsonMapper.readValue(
                request.getInputStream(),
                CrossrefWorksResponse.class);
        request.disconnect();
        return result;
    }

    @JsonProperty("status")
    private String status;
    @JsonProperty("message-type")
    private String messageType;
    @JsonProperty("message-version")
    private String messageVersion;
    @JsonProperty("message")
    private WorksList messageData;

    @Data
    public static class WorksList {
        @JsonProperty("next-cursor")
        private String nextCursor;
        @JsonProperty("total-results")
        private long totalResultCount;
        @JsonProperty("items")
        private List<Item> items;
        @JsonProperty("items-per-page")
        private int maxResultsPerRequest;
        @JsonProperty("query")
        private QueryInfo queryInfo;

        @Getter(AccessLevel.NONE)
        @Setter(AccessLevel.NONE)
        @JsonIgnore
        private final Map<String, Object> otherProps = new HashMap<>();

        @JsonAnyGetter
        public Map<String, Object> getOtherProperties() {
            return this.otherProps;
        }

        @JsonAnySetter
        public void setOtherProperty(String name, Object value) {
            this.otherProps.put(name, value);
        }

        @Data
        public static class Item {
            @JsonProperty("abstract")
            private String summary;
            @JsonProperty("DOI")
            private String doi;
            @JsonProperty("created")
            private DateInfo publicationDate;
            @JsonProperty("title")
            private List<String> titles;
            @JsonProperty("author")
            private List<Author> authors;

            @Getter(AccessLevel.NONE)
            @Setter(AccessLevel.NONE)
            @JsonIgnore
            private final Map<String, Object> otherProps = new HashMap<>();

            @JsonAnyGetter
            public Map<String, Object> getOtherProperties() {
                return this.otherProps;
            }

            @JsonAnySetter
            public void setOtherProperty(String name, Object value) {
                this.otherProps.put(name, value);
            }

            @Data
            public static class Author {
                @JsonProperty("ORCID")
                private String orcid;
                @JsonProperty("given")
                private String givenName;
                @JsonProperty("family")
                private String familyName;

                @Getter(AccessLevel.NONE)
                @Setter(AccessLevel.NONE)
                @JsonIgnore
                private final Map<String, Object> otherProps = new HashMap<>();

                @JsonAnyGetter
                public Map<String, Object> getOtherProperties() {
                    return this.otherProps;
                }

                @JsonAnySetter
                public void setOtherProperty(String name, Object value) {
                    this.otherProps.put(name, value);
                }
            }
        }

        @Data
        public static final class QueryInfo {
            @JsonProperty("start-index")
            private long startIndex;
            @JsonProperty("search-terms")
            private String searchTerms;
        }

        @Data
        public static final class DateInfo {
            @JsonProperty("date-parts")
            private List<List<Integer>> dateParts;
            @JsonProperty("date-time")
            private String datetimeString;
            @JsonProperty("timestamp")
            private Long timestamp;
        }
    }
}
