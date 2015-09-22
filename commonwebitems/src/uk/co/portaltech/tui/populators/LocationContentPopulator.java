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
 * @author abi
 *
 */
public class LocationContentPopulator implements Populator<LocationModel, LocationData>
{
    private static final String WHERE_TO_GO = "whereToGo";
    private static final String THINGS_TO_SEE_AND_DO_EDITORIAL = "thingsToSeeAndDoEditorial";

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
        { THINGS_TO_SEE_AND_DO_EDITORIAL, WHERE_TO_GO }), sourceModel, new Date(),
                brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
    }
}
