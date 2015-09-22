/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author omonikhide
 *
 */
public class AccommodationSubCategoriesPopulator implements Populator<AccommodationModel, LocationData>
{

    @Resource
    private FeatureService featureService;
    @Resource
    private BrandUtils brandUtils;

    //@Autowired


    @Resource
    private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;




    @Resource
    private LocationService tuiLocationService;

    @Resource
    private MediaDataPopulator mediaDataPopulator;

    private static final String NAME = "name";



    @Override
    public void populate(final AccommodationModel sourceModel, final LocationData targetData) throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        populateAccomodationData(sourceModel, targetData);

        final List<String> superCategoryNames = new ArrayList<String>();
        final List<LocationModel> locationModels = new ArrayList<LocationModel>();
        final Collection<CategoryModel> allSupercategories = sourceModel.getSupercategories();

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
            final String firstFeatureValueAsString = featureService.getFirstFeatureValueAsString(NAME, location, new Date(),
                    brandUtils.getFeatureServiceBrand(location.getBrands()));
            if (firstFeatureValueAsString != null)
            {
                superCategoryNames.add(firstFeatureValueAsString);
            }
        }

        targetData.setSuperCategoryNames(superCategoryNames);
    }

    private void populateAccomodationData(final AccommodationModel source, final LocationData target)
    {
        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "longitude", "latitude", "capitalCity", "fromPrice", "tRating", NAME, "strapline" }));
        target.putFeatureCodesAndValues(featureService.getValuesForFeatures(featureDescriptorList, source, new Date(),
                brandUtils.getFeatureServiceBrand(source.getBrands())));
        target.setName(Collections.first(target.getFeatureValues(NAME), "").toString());
        if (source.getThumbnail() != null)
        {
            final MediaViewData mediaData = new MediaViewData();
            mediaDataPopulator.populate(source.getThumbnail(), mediaData);
            target.setThumbnail(mediaData);
        }

        target.setUrl(tuiProductUrlResolver.resolve(source));
    }


}
