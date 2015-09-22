/**
 *
 */
package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.List;

/**
 * @author omonikhide
 *
 */
public class AccommodationBenefitOption {

    public static final AccommodationBenefitOption    BASIC          = new AccommodationBenefitOption("BASIC");

    private static List<AccommodationBenefitOption> internalValues = Arrays.asList(BASIC);

    private final String                              code;

    protected AccommodationBenefitOption(final String code) {
        this.code = code;
    }

    public static AccommodationBenefitOption valueOf(final String option) {
        final AccommodationBenefitOption possibleOption = new AccommodationBenefitOption(option);
        int internalValuesIndex=internalValues.indexOf(possibleOption);
        if (internalValuesIndex != -1)
        {
            return internalValues.get(internalValuesIndex);
            // just to always return one instance not many the same (equal)
        } else {
            throw new IllegalArgumentException("Cannot parse into an element of " + AccommodationBenefitOption.class.getSimpleName() + ": '" + option + "'");
        }
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AccommodationBenefitOption) {
            return code.equals(((AccommodationBenefitOption) obj).code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

}
