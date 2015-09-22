package uk.co.portaltech.tui.populators;

/*
 * Originating Unit: Portal Technology Systems Ltd
 * http://www.portaltech.co.uk
 *
 * Copyright Portal Technology Systems Ltd.
 *
 * $Id: $
 */

import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * The purpose of this class is to allow conversion from ProductModel to CarouselProductData.
 *
 * @author James Johnstone
 */
public class CarouselDataPopulator
{
    //@Autowired

    @Resource
    private BrandUtils brandUtils;

    @Resource
    private FeatureService featureService;


    private static final TUILogUtils LOG = new TUILogUtils("CarouselDataPopulator");


    public static final int CHARACTER_SIZE = 15;

    /**
     * This method will convert a List of ProductModels into a List of our CarouselProduct Data for passing to the web
     * layer.
     *
     * @param accommodations
     *           A List of ProductModels.
     * @return A List of CarouselProductDatas.
     */
    public List<AccommodationViewData> convertAll(final Collection<AccommodationModel> accommodations)
    {
        if (accommodations == null || accommodations.isEmpty())
        {
            return Collections.emptyList();
        }

        final List<AccommodationViewData> carouselProductDatas = new ArrayList<AccommodationViewData>();

        for (final AccommodationModel accommodation : accommodations)
        {
            carouselProductDatas.add(convert(accommodation));
        }

        return carouselProductDatas;
    }

    /**
     * This method will convert a single ProductModel into a CarouselProductData.
     *
     * @param accommodation
     *           A ProductModel.
     * @return A CarouselProductData containing the relevant data. Null if no ProductModel has been provided.
     */
    public AccommodationViewData convert(final AccommodationModel accommodation)
    {
        if (accommodation == null)
        {
            LOG.error("We were provided a null ProductModel.");
            return null;
        }

        final AccommodationViewData carouselAccommdationData = new AccommodationViewData();
        carouselAccommdationData.putFeatureValue(
                "strapline",
                featureService.getFeatureValues("strapline", accommodation, new Date(),
                        brandUtils.getFeatureServiceBrand(accommodation.getBrands())));
        carouselAccommdationData.putFeatureValue(
                "name",
                featureService.getFeatureValues("name", accommodation, new Date(),
                        brandUtils.getFeatureServiceBrand(accommodation.getBrands())));


        return carouselAccommdationData;
    }

    public List<MediaViewData> convertAllMedia(final Collection<MediaModel> medias)
    {
        final List<MediaViewData> mediaDatas = new ArrayList<MediaViewData>();
        for (final MediaModel media : medias)
        {
            final MediaViewData convertMedia = convertMedia(media);
            mediaDatas.add(convertMedia);
        }
        return mediaDatas;
    }

    public MediaViewData convertMedia(final MediaModel media)
    {
        if (media == null)
        {
            LOG.error("We were provided a null MediaModel.");
            return null;
        }
        final MediaViewData mediaData = new MediaViewData();
        mediaData.setDescription(media.getDescription());
        mediaData.setMainSrc(media.getURL());
        return mediaData;
    }

    public String getSelectedCharacterSet(final String str)
    {
        final String[] split = str.split(" ");
        final StringBuilder sb = new StringBuilder();
        for (int x = 0; x < CHARACTER_SIZE; x++)
        {
            sb.append(split[x]).append(" ");
        }
        return sb.toString();
    }

}
