/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.media.services.AccommodationMediaService;
import uk.co.portaltech.travel.media.services.AttractionMediaService;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.tui.model.ItineraryLeg;


/**
 * @author narendra.bm
 *
 */
public class ItineraryPopulator implements Populator<ItemModel, ItineraryLeg>
{

    private static final String ATTRACTION = "Attraction";
    private static final String EXCURSION = "Excursion";
    private static final String ACCOMMODATION = "Accommodation";
    private Populator<Collection<MediaContainerModel>, List<ImageData>> itineraryMediaPopulator;

   @Resource
   private AccommodationMediaService accommodationMediaService;

   @Resource
   private AttractionMediaService attractionMediaService;

    @Override
    public void populate(final ItemModel source, final ItineraryLeg target) throws ConversionException
    {

        final List<MediaContainerModel> container = new ArrayList<MediaContainerModel>();
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");

        if (ACCOMMODATION.equals(source.getItemtype()) || EXCURSION.equals(source.getItemtype()))
        {
            final ProductModel productModel = (ProductModel) source;
            getProductMediaContainer(container, productModel);
            if (ACCOMMODATION.equals(source.getItemtype()))
            {
                final AccommodationModel model = (AccommodationModel) source;
                target.setVideoPresent(CollectionUtils.isNotEmpty(accommodationMediaService
                    .getVideoMedias(model)));
            }
            else
            {
                target.setVideoPresent(false);
            }

        }
        else if (ATTRACTION.equals(source.getItemtype()))
        {
            final AttractionModel attractionModel = (AttractionModel) source;
            getAttractionMediaContainer(container, attractionModel);
            target.setVideoPresent(CollectionUtils.isNotEmpty(attractionMediaService
               .getVideoMedias(attractionModel)));
        }
        final List<ImageData> imageDataList = new ArrayList<ImageData>();
        itineraryMediaPopulator.populate(container, imageDataList);
        target.setImages(imageDataList);




    }

    /**
     * @param container
     * @param productModel
     */
    private void getProductMediaContainer(final List<MediaContainerModel> container, final ProductModel productModel)
    {
        final MediaModel mediaModel = productModel.getThumbnail();

        if (mediaModel != null)
        {
            container.add(mediaModel.getMediaContainer());
        }
        else
        {
            container.addAll(productModel.getGalleryImages());
        }
    }



    /**
     * @param container
     * @param attractionModel
     */
    private void getAttractionMediaContainer(final List<MediaContainerModel> container, final AttractionModel attractionModel)
    {
        final MediaModel mediaModel = attractionModel.getThumbnail();

        if (mediaModel != null)
        {
            container.add(mediaModel.getMediaContainer());
        }
        else
        {
            container.addAll(attractionModel.getGalleryImages());
        }
    }

    /**
     * @return the itineraryMediaPopulator
     */
    public Populator<Collection<MediaContainerModel>, List<ImageData>> getItineraryMediaPopulator()
    {
        return itineraryMediaPopulator;
    }

    /**
     * @param itineraryMediaPopulator
     *           the itineraryMediaPopulator to set
     */
    public void setItineraryMediaPopulator(
            final Populator<Collection<MediaContainerModel>, List<ImageData>> itineraryMediaPopulator)
    {
        this.itineraryMediaPopulator = itineraryMediaPopulator;
    }
}