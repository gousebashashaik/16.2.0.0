/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author abi
 *
 */
public class AccommodationEnrichPopulator implements Populator<ResultData, AccommodationViewData> {

    @Override
    public void populate(ResultData sourceData, AccommodationViewData targetData) throws ConversionException {
        Assert.notNull(sourceData, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");



    }

}
