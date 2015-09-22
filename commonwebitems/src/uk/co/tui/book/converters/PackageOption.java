package uk.co.tui.book.converters;

import java.util.Arrays;
import java.util.List;

/**
 * The Class PackageOption.
 *
 * @author samantha.gd
 */
public class PackageOption {

    public static final PackageOption CONTENT = new PackageOption("CONTENT");
    public static final PackageOption FLIGHT = new PackageOption("FLIGHT");
    public static final PackageOption PRICE_BREAKDOWN = new PackageOption("PRICE_BREAKDOWN");
    public static final PackageOption ACCOMMODATION = new PackageOption("ACCOMMODATION");
    public static final PackageOption PAX_COMPOSITION = new PackageOption("PAX_COMPOSITION");
    public static final PackageOption MEMO = new PackageOption("MEMO");
    public static final PackageOption PRICE = new PackageOption("PRICE");

    private static List<PackageOption> internalValues = Arrays.asList(
            CONTENT,FLIGHT, PRICE_BREAKDOWN, ACCOMMODATION, PAX_COMPOSITION, MEMO,PRICE);

    /** The code. */
    private final String code;

    /**
     * Instantiates a new package option.
     *
     * @param code
     *            the code
     */
    protected PackageOption(final String code) {
        this.code = code;
    }

    /**
     * Value of.
     *
     * @param option
     *            the option
     * @return the package option
     */
    public static PackageOption valueOf(final String option) {
        final PackageOption possibleOption = new PackageOption(option);
        final int internalValuesIndex=internalValues.indexOf(possibleOption);
        if (internalValuesIndex!=-1) {
            return internalValues.get(internalValuesIndex);
            // just to always return one instance not many the same (equal)
        } else {
            throw new IllegalArgumentException(
                    "Cannot parse into an element of "
                            + PackageOption.class.getSimpleName() + ": '"
                            + option + "'");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return code;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PackageOption) {
            return code.equals(((PackageOption) obj).code);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
