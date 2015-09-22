/**
 *
 */
package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.List;

/**
 * @author gagan
 *
 */
public class AccommodationRoomOption {

    public static final AccommodationRoomOption    BASIC          = new AccommodationRoomOption("BASIC");

    private  static List<AccommodationRoomOption> internalValues = Arrays.asList(BASIC);

    private final String                       code;

    protected AccommodationRoomOption(final String code) {
        this.code = code;
    }

    public static AccommodationRoomOption valueOf(final String option) {
        final AccommodationRoomOption possibleOption = new AccommodationRoomOption(option);
        final int internalValuesIndex=internalValues.indexOf(possibleOption);
        if (internalValuesIndex!= -1) {
            return internalValues.get(internalValuesIndex);
            // just to always return one instance not many the same (equal)
        } else {
            throw new IllegalArgumentException("Cannot parse into an element of " + AccommodationRoomOption.class.getSimpleName() + ": '" + option + "'");
        }
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AccommodationRoomOption) {
            return code.equals(((AccommodationRoomOption) obj).code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

}
