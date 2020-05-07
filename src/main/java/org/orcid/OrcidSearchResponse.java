package org.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.crossref.CrossrefWorksResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.amr.Utils.isNullOrEmpty;

/**
 * A JSON object that maps to the result of a GET request made to https://pub.orcid.org/v3.0/search
 */
@Data
public class OrcidSearchResponse {
    private static ObjectMapper jsonMapper;
    static {
        jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public static OrcidSearchResponse fromWeb(final String query) throws IOException {
        return fromWeb(query, Collections.emptyMap());
    }

    public static OrcidSearchResponse fromWeb(final Map<String, String> params) throws IOException {
        return fromWeb(null, params);
    }

    public static OrcidSearchResponse fromWeb(final String query, final Map<String, String> params) throws IOException {
        final StringBuilder queryUrl = new StringBuilder("https://pub.orcid.org/v3.0/search?");
        if (!isNullOrEmpty(query)) {
            queryUrl.append("q=")
                    .append(URLEncoder.encode(query, "UTF-8"))
                    .append("&");
        }
        params.entrySet().stream()
                .forEach((Map.Entry<String, String> param) -> {
                    try {
                        if (!"q".equals(param.getKey()) || isNullOrEmpty(query)) {
                            queryUrl.append(param.getKey())
                                    .append("=")
                                    .append(URLEncoder.encode(param.getValue(), "UTF-8"))
                                    .append("&");
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalStateException(e); // this should never occur
                    }
                });

        final HttpURLConnection request = (HttpURLConnection) new URL(queryUrl.toString()).openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");
        // TODO get access token
        request.connect();

        final OrcidSearchResponse result = jsonMapper.readValue(
                request.getInputStream(),
                OrcidSearchResponse.class);
        request.disconnect();
        return result;
    }

    @JsonProperty("result")
    private List<OrcidResultItem> items;
    @JsonProperty("num-found")
    private Long totalResultCount;

    @Data
    public static class OrcidResultItem {
        @JsonProperty("orcid-identifier")
        Orcid data;
    }
}
