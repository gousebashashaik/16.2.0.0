/**
 *
 */
package uk.co.portaltech.tui.impex.util;

import java.util.Map;

import uk.co.portaltech.travel.constants.GeneratedTravelConstants.Enumerations.SearchResultType;
import uk.co.portaltech.tui.constants.NumberConstants;

/**
 * @author kandipr
 *
 */
public class URLMapContext {

    public void prepareUrlToCrdMap(Map<Integer, String> line) {
        StringBuilder uniqueCode = new StringBuilder();
        // UniqueCode
        uniqueCode.append(line.get(Integer.valueOf(NumberConstants.ONE)));
        uniqueCode.append('_');
        uniqueCode.append(line.get(Integer.valueOf(NumberConstants.TWO)));
        line.put(Integer.valueOf(NumberConstants.FOURHUNDRED), uniqueCode.toString());

        // CRDCode
        line.put(Integer.valueOf(NumberConstants.FOURHUNDREDANDONE), line.get(Integer.valueOf(NumberConstants.TWO)));

        // URL
        line.put(Integer.valueOf(NumberConstants.FOURHUNDREDANDTWO), line.get(Integer.valueOf(NumberConstants.THREE)));

        // CRD type
        line.put(Integer.valueOf(NumberConstants.FOURHUNDREDANDTHREE), getCRDType(line.get(Integer.valueOf(NumberConstants.ONE))));
    }

    @SuppressWarnings("deprecation")
    private String getCRDType(String typeCode)
    {
        if ("A".equalsIgnoreCase(typeCode))
        {
            return SearchResultType.ACCOMMODATION.toString();
        }
        else if ("G".equalsIgnoreCase(typeCode))
        {
            return SearchResultType.LOCATION.toString();
        }

        return SearchResultType.ACCOMMODATION.toString();

    }

}
