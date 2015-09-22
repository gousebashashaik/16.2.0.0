/**
 *
 */
package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.List;

/**
 * @author l.furrer
 *
 */
public class AttractionOption {

    public static final AttractionOption    USPS           = new AttractionOption("USPS");
    public static final AttractionOption    BASIC           = new AttractionOption("BASIC");

    private static List<AttractionOption> internalValues = Arrays.asList(USPS,BASIC);

    private final String                   code;

    protected AttractionOption(final String code) {
        this.code = code;
    }

    public static AttractionOption valueOf(final String option) {
        final AttractionOption possibleOption = new AttractionOption(option);
       final int internalValuesIndex= internalValues.indexOf(possibleOption);
        if (internalValuesIndex!=-1) {
            return internalValues.get(internalValuesIndex);
            // just to always return one instance not many the same (equal)
        } else {
            throw new IllegalArgumentException("Cannot parse into an element of " + AttractionOption.class.getSimpleName() + ": '" + option + "'");
        }
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AttractionOption) {
            return code.equals(((AttractionOption) obj).code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
