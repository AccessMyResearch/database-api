package org.crossref;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JSON object that maps to the result of a GET request made to https://api.crossref.org/works
 */
@Data
public class CrossrefWorksResponse {
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
