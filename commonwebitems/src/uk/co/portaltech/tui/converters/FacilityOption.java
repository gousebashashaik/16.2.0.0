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
public class FacilityOption
{

    public static final FacilityOption FACILITY_BASIC = new FacilityOption("FACILITY_BASIC");

    private static List<FacilityOption> internalValues = Arrays.asList(FACILITY_BASIC);

    private final String code;

    protected FacilityOption(final String code)
    {
        this.code = code;
    }

    public static FacilityOption valueOf(final String option) {
        final FacilityOption possibleOption = new FacilityOption(option);
      final int internalValuesIndex=  internalValues.indexOf(possibleOption);
        if (internalValuesIndex!=-1) {
            return internalValues.get(internalValuesIndex);
            // just to always return one instance not many the same (equal)
        } else {
            throw new IllegalArgumentException("Cannot parse into an element of " + FacilityOption.class.getSimpleName() + ": '" + option + "'");
        }
    }

    @Override
    public String toString()
    {
        return code;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof FacilityOption)
        {
            return code.equals(((FacilityOption) obj).code);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return code.hashCode();
    }
}
