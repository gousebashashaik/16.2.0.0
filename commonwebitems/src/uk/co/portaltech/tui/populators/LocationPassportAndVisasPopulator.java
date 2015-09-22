/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author omonikhide
 *
 */
public class LocationPassportAndVisasPopulator implements Populator<LocationModel, LocationData>
{

    @Resource
    private FeatureService featureService;
    @Resource
    private BrandUtils brandUtils;

    @Override
    public void populate(final LocationModel sourceModel, final LocationData targetData) throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");
        targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(Arrays.asList(new String[]
        { "passportAndVisaInfo" }), sourceModel, new Date(), brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));

    }

}
