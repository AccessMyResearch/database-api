package org.crossref;

import static com.amr.api.Utils.isNullOrEmpty;

public class DOI {
    private DOI() {
        throw new IllegalStateException(); // this class should never be instantiated
    }

    /**
     * Normalizes a DOI to the form 12.345/what-ever.67
     * For more information: https://www.doi.org/doi_handbook/2_Numbering.html
     *
     * @param doi the DOI to normalize
     * @return the normalized DOI, or null if the DOI is invalid or cannot be normalized
     */
    public static String canonical(String doi) {
        if (isNullOrEmpty(doi))
            return null;
        doi = doi.trim();
        if (doi.endsWith("/"))
            doi = doi.substring(0, doi.length() - 1);
        int index = doi.lastIndexOf("doi.org/");
        if (index >= 0)
            doi = doi.substring(index + "doi.org/".length());
        return doi;
    }
}
