/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.TransferTimeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author omonikhide
 *
 */
public class AccommodationInfoPopulator implements Populator<AccommodationModel, AccommodationViewData>
{

    private static final TUILogUtils LOG = new TUILogUtils("AccommodationInfoPopulator");

    @Resource
    private FeatureService featureService;

    @Resource
    private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;

    @Resource
    private MediaPopulator heroImagesPopulator;

    @Resource
    private BrandUtils brandUtils;



    private static final String STRAPLINE = "strapline";


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final AccommodationModel sourceModel, final AccommodationViewData targetData) throws ConversionException
    {

        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        final String brand = brandUtils.getFeatureServiceBrand(sourceModel.getBrands());
        //retail specific change

        targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(Arrays.asList(new String[]
        { "distanceFromAirport", "locationInformation", "latitude", "longitude", "productName", STRAPLINE }), sourceModel,
                new Date(), brand));

        // The thumbnail image and also introduction text for the resort is needed to be returned to the view and this
        // is gotten getting the supercategories of the accommodation
        // and getting the required data from the resort model directly.
        final Collection<TransferTimeModel> transferTimes = sourceModel.getTransferTimes();
        final List<TransferTimeModel> timeModels = new ArrayList<TransferTimeModel>();
        if (transferTimes != null && !transferTimes.isEmpty())
        {
            for (final TransferTimeModel timeModel : transferTimes)
            {
                timeModels.add(timeModel);
            }
            final TransferTimeModel transferTimeModel = timeModels.get(0);
            final Map<String, List<Object>> valuesForFeatures = featureService.getOptimizedValuesForFeatures(
                    Arrays.asList(new String[]
                    { "primaryTransferMode", "transferTime" }), transferTimeModel, new Date(), null);
            targetData.putFeatureValue("primaryTransferMode", valuesForFeatures.get("primaryTransferMode"));
            targetData.putFeatureValue("transferTime", valuesForFeatures.get("transferTime"));
            targetData.putFeatureValue("airportName", airportNameOf(transferTimeModel));
        }

        final List<MediaContainerModel> galleryImages = sourceModel.getGalleryImages();
        final List<MediaViewData> galleryData = new ArrayList<MediaViewData>();
        heroImagesPopulator.populate(galleryImages, galleryData);
        targetData.setGalleryImages(galleryData);

        final Collection<CategoryModel> supercategories = sourceModel.getSupercategories();

        for (final CategoryModel categoryModel : supercategories)
        {
            if ("Location".equalsIgnoreCase(categoryModel.getItemtype()))
            {
                if (categoryModel.getThumbnail() == null || StringUtils.isEmpty(categoryModel.getThumbnail().getURL()))
                {
                    LOG.warn("There is no thumbnail pics for resort: " + categoryModel.getName());
                }
                else
                {
                    targetData.putFeatureValueNonObjectType("resortThumbnailUrl", Arrays.asList(new String[]
                    { categoryModel.getThumbnail().getURL() }));
                }
                final Map<String, List<Object>> valuesForFeatures = featureService.getOptimizedValuesForFeatures(
                        Arrays.asList(new String[]
                        { "introduction", STRAPLINE }), categoryModel, new Date(),
                        brandUtils.getFeatureServiceBrand(categoryModel.getBrands()));
                targetData.putFeatureValue("resortIntroductionText", valuesForFeatures.get("resortIntroductionText"));
                targetData.putFeatureValue(STRAPLINE, valuesForFeatures.get(STRAPLINE));
            }
        }
        tuiProductUrlResolver.setOverrideSubPageType("location");
        final String url = tuiProductUrlResolver.resolve(sourceModel);
        targetData.setLocationMapUrl(url);
    }

    private List<Object> airportNameOf(final TransferTimeModel transferTimeModel)
    {
        final List<Object> result = new ArrayList<Object>();
        result.add(transferTimeModel.getAirportName());
        return result;
    }

}
