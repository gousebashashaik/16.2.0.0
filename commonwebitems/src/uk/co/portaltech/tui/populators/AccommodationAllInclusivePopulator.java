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

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author s.consolino
 *
 */
public class AccommodationAllInclusivePopulator implements Populator<AccommodationModel, AccommodationViewData>
{

    private static final String FEATURE_DESCRIPTOR_ALL_INCLUSIVE_FOOD = "allInclusiveFood";
    private static final String FEATURE_DESCRIPTOR_ALL_INCLUSIVE_DRINK = "allInclusiveDrink";
    private static final String FEATURE_DESCRIPTOR_ALL_INCLUSIVE_SPORTS_ACTIVITY = "allInclusiveSportActivity";

    @Resource
    private FeatureService featureService;
    @Resource
    private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;
    @Resource
    private BrandUtils brandUtils;


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final AccommodationModel source, final AccommodationViewData target) throws ConversionException
    {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
        target.setCode(source.getCode());
        target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
                Arrays.asList(new String[]
                { FEATURE_DESCRIPTOR_ALL_INCLUSIVE_FOOD, FEATURE_DESCRIPTOR_ALL_INCLUSIVE_DRINK,
                        FEATURE_DESCRIPTOR_ALL_INCLUSIVE_SPORTS_ACTIVITY }), source, new Date(),
                brandUtils.getFeatureServiceBrand(source.getBrands())));
        tuiProductUrlResolver.setOverrideSubPageType("facilities");
        final String url = tuiProductUrlResolver.resolve(source);
        target.setFacilitiesUrl(url);
    }
}
