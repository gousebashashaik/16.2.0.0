/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author omonikhide
 *
 */
public class AccommodationThumbnailMapPopulator implements Populator<ItemModel, AccommodationViewData>
{

    @Resource
    private FeatureService featureService;

    @Resource
    private LocationService tuiLocationService;

    @Resource
    private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

    @Resource
    private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

    @Resource
    private MediaDataPopulator mediaDataPopulator;

    @Resource
    private ViewSelector viewSelector;


    @Resource
    private SessionService sessionService;

    @Resource
    private BrandUtils brandUtils;




    @Override
    public void populate(final ItemModel sourceModel, final AccommodationViewData targetData) throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");
        String brand = null;

        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { "longitude", "latitude" }));
        if (sourceModel instanceof ExcursionModel)
        {
            final ExcursionModel excursionModel = (ExcursionModel) sourceModel;
            final Set<ExcursionPriceModel> prices = excursionModel.getExcursionPrices();
            for (final ExcursionPriceModel price : prices)
            {
                final LocationModel locationModel = price.getLocation();
                if (locationModel != null)
                {
                    targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, locationModel,
                            new Date(), brand));
                    break;
                }
            }
        }
        else
        {
            if (sourceModel instanceof AccommodationModel)
            {
                brand = brandUtils.getFeatureServiceBrand(((AccommodationModel) sourceModel).getBrands());
            }
            targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, sourceModel, new Date(),
                    brand));
        }
        // Set location URL
        if (sourceModel instanceof ExcursionModel || sourceModel instanceof AttractionModel)
        {
            final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
            final List<String> brandTypePKS = brandDetails.getRelevantBrands();
            final LocationModel location = tuiLocationService.getLocationForItem(sourceModel, brandTypePKS);
            tuiCategoryModelUrlResolver.setOverrideSubPageType("places-to-go");
            final String url = tuiCategoryModelUrlResolver.resolve(location);
            targetData.setLocationMapUrl(url);
            if (viewSelector.checkIsMobile())
            {
                final MediaModel thumbnail = location.getThumbnail();
                if (thumbnail != null)
                {
                    final MediaViewData mediaData = new MediaViewData();
                    mediaDataPopulator.populate(thumbnail, mediaData);
                    targetData.setThumbnail(mediaData);
                }
            }
        }
        else if (sourceModel instanceof AccommodationModel)
        {
            final AccommodationModel accommodationModel = (AccommodationModel) sourceModel;
            tuiProductUrlResolver.setOverrideSubPageType("location");
            final String url = tuiProductUrlResolver.resolve(accommodationModel);
            targetData.setLocationMapUrl(url);
            if (viewSelector.checkIsMobile())
            {
                final MediaModel thumbnail = accommodationModel.getThumbnail();
                if (thumbnail != null)
                {
                    final MediaViewData mediaData = new MediaViewData();
                    mediaDataPopulator.populate(thumbnail, mediaData);
                    targetData.setThumbnail(mediaData);
                }
            }
        }
    }
}
