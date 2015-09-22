/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.fest.util.Collections;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author omonikhide
 *
 */
public class LocationKeyFactsDataPopulator implements Populator<LocationModel, LocationData>
{

    @Resource
    private BrandUtils brandUtils;

    @Resource
    private FeatureService featureService;

    @Resource
    private LocationService tuiLocationService;

    @Resource
    private SessionService sessionService;

    private static final String TIME_ZONE = "timezone";
    private static final String LANGUAGE = "language";
    private static final String FLT_DURATION = "flightDurationFromUk";

    @Override
    public void populate(final LocationModel sourceModel, final LocationData targetData) throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "currency", FLT_DURATION, "capitalCity", TIME_ZONE, "population", LANGUAGE, "transferTime" }));

        final String brand = brandUtils.getFeatureServiceBrand(sourceModel.getBrands());

        Map<String, List<Object>> valuesForFeatures = featureService.getOptimizedValuesForFeatures(featureDescriptorList, sourceModel,
                new Date(), brand);

        targetData.putFeatureCodesAndValues(populateMap(sourceModel, prepareMap(valuesForFeatures)));


        if (!LocationType.DESTINATION.equals(sourceModel.getType()))
        {
            final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
            final List<String> brandTypePKS = brandDetails.getRelevantBrands();
            final LocationModel locationForItem = tuiLocationService.getLocationForItem(sourceModel, LocationType.DESTINATION,
                    brandTypePKS);

            if (locationForItem != null)
            {
                valuesForFeatures = featureService.getValuesForFeatures(new ArrayList(Arrays.asList(new String[]
                { TIME_ZONE, LANGUAGE, FLT_DURATION })), locationForItem, new Date(), brand);
                if (!LocationType.COUNTRY.equals(sourceModel.getType()))
                {
                    if (valuesForFeatures.get(FLT_DURATION) != null && !valuesForFeatures.get(FLT_DURATION).isEmpty())
                    {
                        targetData.putFeatureValue(FLT_DURATION, valuesForFeatures.get(FLT_DURATION));
                    }
                    if (valuesForFeatures.get(LANGUAGE) != null && !valuesForFeatures.get(LANGUAGE).isEmpty())
                    {
                        targetData.putFeatureValue(LANGUAGE, valuesForFeatures.get(LANGUAGE));
                    }
                }
                if (valuesForFeatures.get(TIME_ZONE) != null && !valuesForFeatures.get(TIME_ZONE).isEmpty())
                {
                    targetData.putFeatureValue(TIME_ZONE, valuesForFeatures.get(TIME_ZONE));
                }
            }
        }
    }

    private Map<String, List<Object>> populateMap(final LocationModel locationModel,
            final Map<String, List<Object>> valuesForFeatures)
    {

        final Map<String, List<Object>> featuresAndValues = new HashMap<String, List<Object>>();
        if (valuesForFeatures != null)
        {
            for (final Map.Entry<String, List<Object>> entry : valuesForFeatures.entrySet())
            {
                if (entry.getValue() == null || Collections.isEmpty(entry.getValue()))
                {
                    final List<Object> values = getValues(entry.getValue(), locationModel, entry.getKey());
                    featuresAndValues.put(entry.getKey(), values);
                }
                else
                {
                    featuresAndValues.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return featuresAndValues;

    }

    private List<Object> getValues(final List<Object> value, final LocationModel locationModel, final String featureCode)
    {
        List<Object> values = value;
        if (values == null || Collections.isEmpty(values))
        {
            final List<CategoryModel> supercategories = locationModel.getSupercategories();
            for (final CategoryModel category : supercategories)
            {
                if (category instanceof LocationModel)
                {
                    final LocationModel model = (LocationModel) category;
                    values = featureService.getFeatureValues(featureCode, model, new Date(),
                            brandUtils.getFeatureServiceBrand(locationModel.getBrands()));
                    if (values == null || Collections.isEmpty(values))
                    {
                        values = getValues(values, model, featureCode);
                    }
                    else
                    {
                        return values;
                    }
                }
            }
        }
        return values;
    }

    private Map<String, List<Object>> prepareMap(final Map<String, List<Object>> map)
    {
        final Map<String, List<Object>> setMap = new HashMap<String, List<Object>>();
        setMap.put("currency", null);
        setMap.put(FLT_DURATION, null);
        setMap.put("capitalCity", null);
        setMap.put(TIME_ZONE, null);
        setMap.put("population", null);
        setMap.put(LANGUAGE, null);
        setMap.put("transferTime", null);
        for (final Map.Entry<String, List<Object>> entry : map.entrySet())
        {
            setMap.put(entry.getKey(), entry.getValue());
        }
        return setMap;
    }
}
