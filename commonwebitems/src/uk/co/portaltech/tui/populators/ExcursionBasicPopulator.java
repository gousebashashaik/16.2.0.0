/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.AttractionUrlResolver;
import uk.co.portaltech.tui.resolvers.ExcursionURLResolver;
import uk.co.portaltech.tui.resolvers.ExcursionURLResolverForLegacySystems;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;


/**
 * @author abi
 *
 */
public class ExcursionBasicPopulator implements Populator<ExcursionModel, ExcursionViewData>
{


    @Resource
    private FeatureService featureService;

    @Resource
    private ExcursionURLResolverForLegacySystems excursionUrlResolverForLegacyStstem;

    @Resource
    private ExcursionURLResolver excursionUrlResolver;

    @Resource
    private AttractionUrlResolver attractionUrlResolver;

    private static final String EDITORIAL_CONTENT = "editorialContent";

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final ExcursionModel sourceModel, final ExcursionViewData targetDataParam) throws ConversionException
    {
        ExcursionViewData targetData = targetDataParam;
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");



        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "name", "suitableFor", EDITORIAL_CONTENT, "duration", "childAgeMax", "childAgeMin" }));
        final Map<String, List<Object>> features = featureService.getValuesForFeatures(featureDescriptorList, sourceModel,
                new Date(), null);

        if (features != null && features.containsKey(EDITORIAL_CONTENT))
        {
            final List<Object> description = features.get(EDITORIAL_CONTENT);
            if (description != null && !description.isEmpty())
            {
                targetData.setDescription(description.get(0).toString());
                features.remove(EDITORIAL_CONTENT);
            }
        }
        if (features != null)
        {
        targetData.putFeatureCodesAndValues(features);
        }
        // lat long needed for interactive map - get it from the parent
        targetData = getLatitudeAndLongitude(sourceModel, targetData);
        if (features != null)
        {
            final List<Object> childAgeMax = features.get("childAgeMax");

            targetData = getLatitudeAndLongitude(sourceModel, targetData);


            if (childAgeMax != null && !childAgeMax.isEmpty())
            {
                targetData.setChildAgeMax(childAgeMax.get(0).toString());
            }

            final List<Object> childAgeMin = features.get("childAgeMin");
            if (childAgeMin != null && !childAgeMin.isEmpty())
            {
                targetData.setChildAgeMin(childAgeMin.get(0).toString());
            }
        }
        targetData.setCode(sourceModel.getCode());
        targetData.setName(sourceModel.getName());

        if (StringUtils.isBlank(sourceModel.getAtlasId())
                || de.hybris.platform.util.Config.getBoolean("buildExcursionURL.legacy", false))
        {
            targetData.setBookingUrl(excursionUrlResolverForLegacyStstem.resolve(sourceModel));
        }
        else
        {
            targetData.setBookingUrl(excursionUrlResolver.resolve(sourceModel));
        }

        targetData.setUrl(attractionUrlResolver.resolve(sourceModel));

    }

    /**
     * @param source
     * @param target
     * @return ExcursionViewData
     *
     *         This method returns the excursion data with latitude and longitude.
     */
    private ExcursionViewData getLatitudeAndLongitude(final ExcursionModel source, final ExcursionViewData target)
    {
        final ExcursionModel sourceModel = source;
        final ExcursionViewData targetData = target;
        if (sourceModel.getExcursionPrices() != null && !sourceModel.getExcursionPrices().isEmpty())
        {
            final String lat = null;
            final String lon = null;
            for (final ExcursionPriceModel price : sourceModel.getExcursionPrices())
            {
                if (StringUtils.isBlank(lat) && StringUtils.isBlank(lon))
                {
                    if (price.getLocation() != null)
                    {
                        final Map<String, List<Object>> latLong = featureService.getValuesForFeatures(Arrays.asList(new String[]
                        { "name", "latitude", "longitude" }), price.getLocation(), new Date(), null);
                        targetData.getFeatureCodesAndValues().put("latitude", latLong.get("latitude"));
                        targetData.getFeatureCodesAndValues().put("longitude", latLong.get("longitude"));
                    }
                }
                else
                {
                    break;
                }
            }
        }
        return targetData;

    }

}
