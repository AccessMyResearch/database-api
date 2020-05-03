package com.amr;

import java.util.Collection;

import static java.util.Objects.isNull;

public class Utils {
    private Utils() {
        throw new IllegalStateException(); // this class should never be instantiated
    }

    public static boolean isNullOrEmpty(final CharSequence obj) {
        return isNull(obj) || obj.length() == 0;
    }

    public static boolean isNullOrEmpty(final Collection obj) {
        return isNull(obj) || obj.isEmpty();
    }
}
