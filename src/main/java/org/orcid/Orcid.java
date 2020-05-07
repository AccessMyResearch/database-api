package org.orcid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.beans.ConstructorProperties;

import static com.amr.Utils.isNullOrEmpty;

@Value
public class Orcid {
    @JsonProperty("uri")
    String url;
    @JsonProperty("path")
    String orcid;
    @JsonProperty("host")
    String hostName;

    @ConstructorProperties({"orcid"})
    public Orcid(String orcid) {
        this.orcid = canonical(orcid);
        this.hostName = "orcid.org";
        this.url = "https://" + this.hostName + "/" + this.orcid;
    }

    /**
     * Normalizes an ORCID to the form 0123456789012345, ideal for database storage
     * For more information: https://support.orcid.org/hc/en-us/articles/360006897674-Structure-of-the-ORCID-Identifier
     *
     * @param orcid the ORCID to normalize
     * @return the normalized ORCID, or null if the ORCID cannot be normalized
     */
    public static String internal(String orcid) {
        if (isNullOrEmpty(orcid))
            return null;
        if (orcid.length() > 16) {
            orcid = orcid.replaceAll("\\s+", ""); // ignore whitespace
        }
        if (orcid.length() > 16) {
            orcid = orcid.replace("-", ""); // ignore dashes
        }
        if (orcid.length() > 16) {
            orcid = orcid.replace('\\', '/'); // normalize slashes
        }
        if (orcid.length() > 16) {
            if (orcid.endsWith("/")) // ignore trailing slash
                orcid = orcid.substring(0, orcid.length() - 1);
        }
        if (orcid.length() > 16) {
            orcid = orcid.substring(orcid.lastIndexOf('/') + 1); // ignore URL host name
        }
        if (orcid.length() != 16)
            return null;
        orcid = orcid.toUpperCase();
        if (!orcid.matches("[0-9]{15}[0-9X]"))
            return null;

        // validate the checksum
        int total = 0;
        for (int i = 0; i < orcid.length() - 1; ++i) {
            int digit = Character.getNumericValue(orcid.charAt(i));
            total = (total + digit) * 2 % 11;
        }
        int checksum = (12 - total) % 11;
        char checkDigit = checksum == 10 ? 'X' : (char) (checksum + '0');
        if (checkDigit != orcid.charAt(orcid.length() - 1))
            return null;

        return orcid;
    }

    /**
     * Normalizes an ORCID to the form 0123456789012345, ideal for database storage
     * For more information: https://support.orcid.org/hc/en-us/articles/360006897674-Structure-of-the-ORCID-Identifier
     *
     * @param orcid the ORCID to normalize
     * @return the normalized ORCID, or null if the ORCID is invalid or cannot be normalized
     */
    public static String canonical(String orcid) {
        final String raw = internal(orcid);
        if (isNullOrEmpty(raw))
            return null;
        return String.join("-",
                raw.substring(0, 4),
                raw.substring(4, 8),
                raw.substring(8, 12),
                raw.substring(12, 16));
    }
}
