/**
 *
 */
package uk.co.portaltech.tui.services;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;




import uk.co.portaltech.tui.web.view.data.DestinationData;
/**
 *
 * This service provides all the products for product range type.
 */
public class HolidayTypeService {


    /**
     * @param destList
     * @param airports
     * @param dates
     * This method provides sets the products for each product in product range type.
     */
    public void getHolidaysForProductRange(List<DestinationData> destList,
            final List<String> airports, final List<String> dates ,String multiSelect)  {
        List<String> addedHolidayTypes = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(destList)){
        for (DestinationData holidayType : destList) {
            addedHolidayTypes.add(holidayType.getName());
            if(StringUtils.equalsIgnoreCase("false", multiSelect)){
                holidayType.setAvailable(false);
            }
            else{
                holidayType.setAvailable(true);
            }

            continue;

        }
    }
    }







}
