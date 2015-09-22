/**
 *
 */
package uk.co.tui.book.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thyagaraju.e
 *
 */
public class FlightOptionsStaticContentViewData {

    /** The flight content map. */
    private Map<String,String> flightContentMap = new HashMap<String, String>();

    private GenericStaticContentViewData genericContentViewData;

    /**
     * @param flightContentMap the flightContentMap to set
     */
    public void setFlightContentMap(final Map<String, String> flightContentMap) {
        this.flightContentMap = flightContentMap;
    }

    /**
     * @return the flightContentMap
     */
    public Map<String, String> getFlightContentMap() {
        return flightContentMap;
    }

    /**
     * @return the genericContentViewData
     */
    public GenericStaticContentViewData getGenericContentViewData() {
        return genericContentViewData;
    }

    /**
     * @param genericContentViewData the genericContentViewData to set
     */
    public void setGenericContentViewData(
            final GenericStaticContentViewData genericContentViewData) {
        this.genericContentViewData = genericContentViewData;
    }
}
