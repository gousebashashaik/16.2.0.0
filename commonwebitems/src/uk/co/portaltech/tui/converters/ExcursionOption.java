/**
 *
 */
package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.List;

/**
 * @author abi
 * @author l.furrer
 *
 */
public class ExcursionOption {

    public static final ExcursionOption    BASIC          = new ExcursionOption("BASIC");
    public static final ExcursionOption    PRIMARY_IMAGE  = new ExcursionOption("PRIMARY_IMAGE");
    public static final ExcursionOption    GALLERY_IMAGE  = new ExcursionOption("GALLERY_IMAGE");
    public static final ExcursionOption    USPS           = new ExcursionOption("USPS");
    public static final ExcursionOption    RESTRICTION           = new ExcursionOption("RESTRICTION");
    public static final ExcursionOption    EXCURSIONPRICE           = new ExcursionOption("EXCURSIONPRICE");

    private static List<ExcursionOption> internalValues = Arrays.asList(BASIC,PRIMARY_IMAGE,GALLERY_IMAGE,USPS,RESTRICTION, EXCURSIONPRICE);

    private final String                   code;

    protected ExcursionOption(final String code) {
        this.code = code;
    }

    public static ExcursionOption valueOf(final String option) {
        final ExcursionOption possibleOption = new ExcursionOption(option);

       final int internalValuesIndex= internalValues.indexOf(possibleOption);
        if (internalValuesIndex!=-1) {
            return internalValues.get(internalValuesIndex);
            // just to always return one instance not many the same (equal)
        } else {
            throw new IllegalArgumentException("Cannot parse into an element of " + ExcursionOption.class.getSimpleName() + ": '" + option + "'");
        }
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ExcursionOption) {
            return code.equals(((ExcursionOption) obj).code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
