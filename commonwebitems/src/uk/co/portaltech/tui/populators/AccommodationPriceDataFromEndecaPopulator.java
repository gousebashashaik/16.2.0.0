/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.fest.util.Collections;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author s.consolino
 *
 */
public class AccommodationPriceDataFromEndecaPopulator implements Populator<SearchResultData<ResultData>, AccommodationViewData> {

      private static final TUILogUtils LOG = new TUILogUtils("AccommodationPriceDataFromEndecaPopulator");

    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(SearchResultData<ResultData> source, AccommodationViewData target) throws ConversionException {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
        if (!Collections.isEmpty(source.getResults())) {
            ResultData resultData = source.getResults().get(0);
            if(resultData != null) {
                target.setPriceFrom(resultData.getPriceFrom());
                target.setAvailableFrom(resultData.getAvailableFrom());
                target.setDeparturePoint(resultData.getDeparturePoint());
                target.setRoomOccupancy(getRoomOccupancy(resultData.getRoomOccupancy()));
            } else {
                LOG.debug("Receved the empty result");
            }
        }
    }

    private String getRoomOccupancy(String roomOccupancy)
    {
        String occupancy = "2 adults";
        try
        {
                if(StringUtils.isNotEmpty(roomOccupancy))
                {
                        String [] roomOcc = roomOccupancy.split("/");
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtils.substring(roomOcc[0], 0, 1)).append(" adults, ").append(StringUtils.substring(roomOcc[1],0, 1)).append(" children, ").append(StringUtils.substring(roomOcc[1],0, 1)).append(" infants");
                occupancy = sb.toString();
                }

        }
        catch (PatternSyntaxException pse)
        {
            occupancy = "2 adults";
                LOG.info("Endeca has changed the way the data use to be sent" , pse);
        }
        return occupancy;
    }
}
