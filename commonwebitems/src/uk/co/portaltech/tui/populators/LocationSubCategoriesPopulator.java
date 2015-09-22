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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author omonikhide
 *
 */
public class LocationSubCategoriesPopulator implements Populator<LocationModel, LocationData>
{
    @Resource
    private BrandUtils brandUtils;

    @Resource
    private FeatureService featureService;

    @Resource
    private TUIUrlResolver<LocationModel> tuiCategoryModelUrlResolver;

    @Resource
    private LocationConverter locationConverter;

    @Resource
    private LocationService tuiLocationService;

    @Resource
    private MediaDataPopulator mediaDataPopulator;

    @Resource
    private SessionService sessionService;



    @Override
    public void populate(final LocationModel sourceModel, final LocationData targetData) throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        populateLocationData(sourceModel, targetData);

        List<LocationData> subcategories = null;
        List<LocationModel> subLocations = null;
        final List<CategoryModel> categories = sourceModel.getCategories();
        if (categories != null && !categories.isEmpty())
        {
            subcategories = new ArrayList<LocationData>();

            subLocations = filteredLocations(categories);
            for (final LocationModel locationModel : subLocations)
            {
                final LocationData locationData = locationConverter.convert(locationModel);

                populateLocationData(locationModel, locationData);

                subcategories.add(locationData);
            }
            targetData.setSubLocations(subcategories);
        }
        final List<String> superCategoryNames = new ArrayList<String>();
        final List<LocationModel> locationModels = new ArrayList<LocationModel>();
        final Collection<CategoryModel> allSupercategories = sourceModel.getAllSupercategories();

        for (final CategoryModel category : allSupercategories)
        {
            if (category instanceof LocationModel)
            {
                locationModels.add((LocationModel) category);
            }
        }

        tuiLocationService.orderLocationsByGeoLevel(locationModels);

        for (final LocationModel location : locationModels)
        {
            final String firstFeatureValueAsString = featureService.getFirstFeatureValueAsString("name", location, new Date(),
                    brandUtils.getFeatureServiceBrand(location.getBrands()));
            if (firstFeatureValueAsString != null)
            {
                superCategoryNames.add(firstFeatureValueAsString);
            }
        }

        targetData.setSuperCategoryNames(superCategoryNames);
    }

    private void populateLocationData(final LocationModel source, final LocationData target)
    {
        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "longitude", "latitude", "capitalCity", "fromPrice", "tRating", "name", "strapline" }));
        target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, source, new Date(),
                brandUtils.getFeatureServiceBrand(source.getBrands())));
        target.setName(Collections.first(target.getFeatureValues("name"), "").toString());
        target.setCode(source.getCode());
        if (source.getThumbnail() != null)
        {
            final MediaViewData mediaData = new MediaViewData();
            mediaDataPopulator.populate(source.getThumbnail(), mediaData);
            target.setThumbnail(mediaData);
        }

        target.setUrl(tuiCategoryModelUrlResolver.resolve(source));
    }

    private List<LocationModel> filteredLocations(final List<CategoryModel> categoryModels)
    {
        List<LocationModel> locationModels = new ArrayList<LocationModel>();

        for (final CategoryModel categoryModel : categoryModels)
        {
            if (categoryModel instanceof LocationModel)
            {
                final LocationModel locationModel = (LocationModel) categoryModel;

                locationModels.add(locationModel);
            }
        }
        //filtering by brand
        final BrandDetails brandDetails = (BrandDetails) sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
        final List<String> relevantBrands = brandDetails.getRelevantBrands();
        locationModels = tuiLocationService.getLocationsFilteredByBrand(locationModels, relevantBrands);
        return locationModels;
    }
}
