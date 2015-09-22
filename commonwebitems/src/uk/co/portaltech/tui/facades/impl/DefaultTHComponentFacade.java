package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.BusinessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.fest.util.Collections;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.travel.services.accommodation.MainstreamAccommodationService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.components.model.BookingComponentModel;
import uk.co.portaltech.tui.components.model.NonGeoHeroCarouselComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.facades.THComponentFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.CarHireViewData;
import uk.co.portaltech.tui.web.view.data.DreamLinerCarouselViewData;
import uk.co.portaltech.tui.web.view.data.DreamLinerUspViewData;
import uk.co.portaltech.tui.web.view.data.GenericContentViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SafariBusPlanData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;

/**
 * Implementation class for GenericContentFacade that represents to fetch generic content for car
 * hire and dream liner.
 *
 * @author narendra.bm
 *
 */
public class DefaultTHComponentFacade implements THComponentFacade
{
   private static final String DREAMLINER_CODE = "787";

   @Resource
   private Populator<ContentValueModel, GenericContentViewData> genericContentPopulator;

   @Resource
   private Converter<MediaModel, MediaViewData> mediaModelConverter;

   @Resource
   private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

   @Resource
   private GenericContentService genericContentService;

   @Resource
   private LocationService tuiLocationService;

   @Resource
   private CategoryService categoryService;

   @Resource
   private MainstreamAccommodationService accommodationService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private ProductService productService;

   private MainStreamTravelLocationService mainStreamTravelLocationService;

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private SearchFacade searchFacade;

   @Resource
   private ComponentFacade componentFacade;

   private static final String KEY_VALUE_PAIR_1 = "key_value_pair_1";

   private static final String[] INTRO = { "intro" };

   private static final String[] NAME = { "display_name" };

   private static final String[] STRAPLINE = { "strapline" };

   private static final String[] KEY_VALUE_PAIR = { KEY_VALUE_PAIR_1, "key_value_pair_2",
      "key_value_pair_3", "key_value_pair_4", "key_value_pair_5" };

   private static final String[] SAFARI_BUS_PLAN_KEYS = { KEY_VALUE_PAIR_1, "key_value_pair_2",
      "key_value_pair_3", "key_value_pair_4", "key_value_pair_5", "key_value_pair_6" };

   private static final String SAFARI_BUS_PLAN_KEY = KEY_VALUE_PAIR_1;

   private static final String SAFARI_BUS_PLAN_CODE = "SAF";

   private static final String[] ESSENTIAL_INFO_KEYS = { KEY_VALUE_PAIR_1, "key_value_pair_2",
      "key_value_pair_3", "key_value_pair_4", "key_value_pair_5", "key_value_pair_6",
      "key_value_pair_7" };

   private static final String[] NON_CORE_PRODUCT_NAME = { "display_name" };

   private static final String GENERIC_KEY_GENERATOR =
      "uk.co.portaltech.tui.utils.GenericKeyGenerator";

   // for avoiding the duplication of literals
   private static final String LOG_MSG = "No component with uid ";

   private static final TUILogUtils LOG = new TUILogUtils("DefaultTHComponentFacade");

   @Resource
   private Populator<Collection<MediaContainerModel>, List<MediaViewData>> heroImagesPopulator;

   @Override
   public CarHireViewData getCarHireViewData(final String accommodationCode)
   {
      LocationModel prodLocation = null;
      AccommodationModel accommodation = null;
      boolean hasCarHire = false;
      ContentValueModel contentValueModel = null;
      String locFeatureValue = null;
      CarHireViewData carHireViewData = null;
      GenericContentViewData genericContentViewData = null;

      accommodation = (AccommodationModel) productService.getProductForCode(accommodationCode);
      Validate.notNull(accommodation);

      hasCarHire = accommodationService.hasCarHireService(accommodation);

      // check either this accommodation is associated with any car hire supplier.
      if (hasCarHire)
      {
         prodLocation =
            tuiLocationService.getLocationForItem(accommodation,
               tuiUtilityService.getSiteReleventBrandPks());
         try
         {
            locFeatureValue =
               mainStreamTravelLocationService.getCarHireCodeForLocation(prodLocation);
         }
         catch (final BusinessException exception)
         {
            LOG.error("Couldn't get car hire code from the location: " + prodLocation, exception);
            return carHireViewData;
         }
         contentValueModel =
            genericContentService.getContentValue(locFeatureValue.split(":")[0],
               Arrays.asList(STRAPLINE)).get(0);
         if (contentValueModel != null)
         {
            carHireViewData = new CarHireViewData();
            genericContentViewData = new GenericContentViewData();
            // populate ContentValueModel's value.
            genericContentPopulator.populate(contentValueModel, genericContentViewData);
            carHireViewData.setContent(genericContentViewData.getContent());
            if (contentValueModel.getMedias() != null && !contentValueModel.getMedias().isEmpty())
            {
               final MediaViewData mediaViewData =
                  getThumbNail(contentValueModel.getMedias().get(0));
               carHireViewData.setGalleryImage(mediaViewData);
            }
            return carHireViewData;
         }
      }
      return carHireViewData;
   }

   @Override
   public DreamLinerCarouselViewData getDreamLinerCarouselViewData(final String code,
      final String type) throws BusinessException
   {
      DreamLinerUspViewData dreamLinerUspViewData = null;
      LocationModel locationModel = null;
      List<ContentValueModel> contentValueModels = null;
      List<DreamLinerUspViewData> viewData = null;
      DreamLinerCarouselViewData dreamLinerCarouselViewData = null;
      GenericContentViewData genericContentViewData = null;

      if (StringUtils.isNotEmpty(code))
      {
         try
         {
            locationModel =
               mainStreamTravelLocationService.getLocationModel(code, type,
                  tuiUtilityService.getSiteReleventBrandPks());
         }
         catch (final BusinessException e)
         {
            new BusinessException("Could not get location for the code: " + code + " and type: "
               + type, e);
         }
         // check is dream liner is available for this location.
         if (mainStreamTravelLocationService.hasDreamLiner(locationModel))
         {
            dreamLinerCarouselViewData = new DreamLinerCarouselViewData();
            LOG.info("dreamLiner flight exists for the location code " + locationModel.getCode());
            final List<MediaContainerModel> mediaContainerModels =
               genericContentService.getContentItemMedia(DREAMLINER_CODE);
            if (mediaContainerModels != null && !Collections.isEmpty(mediaContainerModels))
            {
               final MediaViewData mediaViewData = getThumbNail(mediaContainerModels.get(0));
               dreamLinerCarouselViewData.setGalleryImage(mediaViewData);
            }

            contentValueModels =
               genericContentService
                  .getContentValue(DREAMLINER_CODE, Arrays.asList(KEY_VALUE_PAIR));
            if (!CollectionUtils.isEmpty(contentValueModels))
            {
               viewData = new ArrayList<DreamLinerUspViewData>();
               LOG.info("Got " + contentValueModels.size() + " usps");
               for (final ContentValueModel contentValueModel : contentValueModels)
               {
                  dreamLinerUspViewData = new DreamLinerUspViewData();
                  genericContentViewData = new GenericContentViewData();
                  final List<MediaViewData> mediaViewDataList = new ArrayList<MediaViewData>();

                  genericContentPopulator.populate(contentValueModel, genericContentViewData);
                  populateNameAndDescription(genericContentViewData, dreamLinerUspViewData);
                  populateUspMedias(contentValueModel.getMedias(), mediaViewDataList);
                  dreamLinerUspViewData.setGalleryImages(mediaViewDataList);

                  viewData.add(dreamLinerUspViewData);
               }
               dreamLinerCarouselViewData.setDreamLinerUsps(viewData);
            }
            return dreamLinerCarouselViewData;
         }
      }
      return dreamLinerCarouselViewData;
   }

   /**
    * @return the mainStreamTravelLocationService
    */
   public MainStreamTravelLocationService getMainStreamTravelLocationService()
   {
      return mainStreamTravelLocationService;
   }

   /**
    * @param mainStreamTravelLocationService the mainStreamTravelLocationService to set
    */
   public void setMainStreamTravelLocationService(
      final MainStreamTravelLocationService mainStreamTravelLocationService)
   {
      this.mainStreamTravelLocationService = mainStreamTravelLocationService;
   }

   @Override
   public AccommodationViewData getNonGeoHeroCarouselData(final String componentUid)
   {

      List<MediaContainerModel> listmediaImages = null;
      AccommodationViewData heroCarouselViewData = null;
      int visibleImgCount = 0;
      NonGeoHeroCarouselComponentModel componentModel = null;
      try
      {
         componentModel =
            (NonGeoHeroCarouselComponentModel) cmsComponentService
               .getAbstractCMSComponent(componentUid);
         visibleImgCount = componentModel.getVisibleItems().intValue();
      }
      catch (final CMSItemNotFoundException e1)
      {
         LOG.error("CMS Item Not Found ", e1);
      }

      listmediaImages =
         genericContentService.getContentItemMedia(componentModel.getNonCoreHolidayType());

      if (listmediaImages != null && !listmediaImages.isEmpty())
      {

         heroCarouselViewData = new AccommodationViewData();

         if (listmediaImages.size() > visibleImgCount)
         {
            listmediaImages = listmediaImages.subList(0, visibleImgCount);
         }

         final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
         heroImagesPopulator.populate(listmediaImages, imageDataList);

         heroCarouselViewData.setGalleryImages(imageDataList);
         heroCarouselViewData.setGalleryImageVisibleItems(visibleImgCount);
      }

      return heroCarouselViewData;
   }

   @Override
   public GenericContentViewData getNonGeoEditorialContent(final String noncoreHolidayTypeCode)
   {

      List<ContentValueModel> contentValueModels = null;
      GenericContentViewData contentViewData = null;

      contentValueModels =
         genericContentService.getContentValue(noncoreHolidayTypeCode, Arrays.asList(INTRO));

      if (CollectionUtils.isNotEmpty(contentValueModels))
      {
         contentViewData = new GenericContentViewData();
         // populate ContentValueModel's value.
         genericContentPopulator.populate(contentValueModels.get(0), contentViewData);

      }

      return contentViewData;
   }

   @Override
   public List<GenericContentViewData> getNeedToKnowContent(final String holidayTypeCode)
   {

      List<ContentValueModel> contentValueModels = null;
      final List<GenericContentViewData> contentViewDataList =
         new ArrayList<GenericContentViewData>();

      contentValueModels =
         genericContentService.getContentValue(holidayTypeCode, Arrays.asList(ESSENTIAL_INFO_KEYS));

      if (CollectionUtils.isNotEmpty(contentValueModels))
      {

         for (final ContentValueModel contentValueModel : contentValueModels)
         {
            GenericContentViewData contentViewData = null;
            if (contentValueModel.getValue() != null)
            {
               contentViewData = new GenericContentViewData();
               contentViewData.setContent((String) contentValueModel.getValue());
               contentViewDataList.add(contentViewData);
            }
         }

      }

      return contentViewDataList;
   }

   // @Override
   public GenericContentViewData getNonCoreProductNameContent(final String holidayTypeCode)
   {

      List<ContentValueModel> contentValueModels = null;
      GenericContentViewData contentViewData = null;

      contentValueModels =
         genericContentService.getContentValue(holidayTypeCode,
            Arrays.asList(NON_CORE_PRODUCT_NAME));
      if (CollectionUtils.isNotEmpty(contentValueModels))
      {
         contentViewData = new GenericContentViewData();
         // populate ContentValueModel's value.
         genericContentPopulator.populate(contentValueModels.get(0), contentViewData);
      }
      return contentViewData;
   }

   @Override
   public SafariBusPlanData getSafariBusPlanComponentData(final String productCode)
   {
      List<ContentValueModel> contentValueModels = null;
      contentValueModels =
         genericContentService.getContentValue(SAFARI_BUS_PLAN_CODE,
            Arrays.asList(SAFARI_BUS_PLAN_KEYS));
      final SafariBusPlanData safariBusPlanData = new SafariBusPlanData();
      GenericContentViewData genericContentViewData = null;
      final DreamLinerUspViewData dreamLinerUspViewData = new DreamLinerUspViewData();

      for (final ContentValueModel contentValueModel : contentValueModels)
      {
         genericContentViewData = new GenericContentViewData();
         genericContentPopulator.populate(contentValueModel, genericContentViewData);
         if (contentValueModel.getName().equals(SAFARI_BUS_PLAN_KEY))
         {
            populateNameAndDescription(genericContentViewData, dreamLinerUspViewData);
            safariBusPlanData.setName(genericContentViewData.getName());
            safariBusPlanData.setDescription(genericContentViewData.getDescription());
            if (!CollectionUtils.sizeIsEmpty(contentValueModel.getMedias()))
            {
               safariBusPlanData
                  .setGalleryImage(getThumbNail(contentValueModel.getMedias().get(0)));
            }
         }
         else
         {
            safariBusPlanData.getContent().add(genericContentViewData.getContent());
         }
      }
      return safariBusPlanData;
   }

   /**
    * @param genericContentViewData
    */
   private void populateNameAndDescription(final GenericContentViewData genericContentViewData,
      final DreamLinerUspViewData uspViewData)
   {
      final Object description = genericContentViewData.getContent();
      String s = "";
      if (description != null)
      {
         final String uspDescription = description.toString();
         final int index1 = uspDescription.indexOf("<h2>");

         final int index2 = uspDescription.indexOf("</h2>");

         if (index1 != -1 && index2 != -1)
         {
            s = uspDescription.substring(index1);
            s = uspDescription.substring(CommonwebitemsConstants.FOUR, index2);
            uspViewData.setUspName(s);
            uspViewData.setUspDesc(uspDescription.substring(index2 + CommonwebitemsConstants.FIVE));
         }
      }
   }

   /**
    * It populates one image to {@link CarHireViewData#setGalleryImage(MediaViewData)} from list of
    * gallery images from the {@link ContentValueModel#getMedias()}
    *
    */
   private MediaViewData getThumbNail(final MediaContainerModel mediaModel)
   {
      MediaViewData mediaViewData = new MediaViewData();
      final Collection<MediaModel> medias = mediaModel.getMedias();
      for (final MediaModel mediaModel2 : medias)
      {
         if (mediaModel2.getMediaFormat() != null
            && "small".equalsIgnoreCase(mediaModel2.getMediaFormat().getName()))
         {
            mediaViewData = mediaModelConverter.convert(mediaModel2);
            mediaViewDataPopulator.populate(mediaModel2, mediaViewData);
            if (mediaViewData != null && mediaViewData.getMainSrc() != null)
            {
               return mediaViewData;
            }
         }
      }
      return mediaViewData;
   }

   /**
    * @param galleryImages
    */
   private void populateUspMedias(final List<MediaContainerModel> galleryImages,
      final List<MediaViewData> mediaViewDatas)
   {
      if (galleryImages != null && !galleryImages.isEmpty())
      {
         for (final MediaContainerModel mediaModel : galleryImages)
         {
            final Collection<MediaModel> medias = mediaModel.getMedias();
            for (final MediaModel mediaModel2 : medias)
            {
               final MediaViewData mediaViewData = mediaModelConverter.convert(mediaModel2);
               mediaViewDataPopulator.populate(mediaModel2, mediaViewData);
               mediaViewDatas.add(mediaViewData);
            }
         }
      }
   }

   @Override
   public GenericContentViewData getNonCoreProductName(final String noncoreHolidayTypeCode,
      final String categoryCode)
   {

      List<ContentValueModel> contentValueModels = null;
      GenericContentViewData contentViewData = null;

      contentValueModels =
         genericContentService.getContentValue(noncoreHolidayTypeCode, Arrays.asList(NAME));

      if (CollectionUtils.isNotEmpty(contentValueModels))
      {

         contentViewData = new GenericContentViewData();
         // populate ContentValueModel's value.
         genericContentPopulator.populate(contentValueModels.get(0), contentViewData);

         final LocationModel locationModel =
            (LocationModel) categoryService.getCategoryForCode(categoryCode);
         contentViewData.setName(locationModel.getName());

      }

      return contentViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ComponentFacade#getAccommodationPrice(java .lang.String,
    * java.lang.String)
    */
   @Override
   @Cacheable(cacheName = "browsePriceDataCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
   public AccommodationViewData getAccommodationPrice(final String componentUid,
      final String accommodationCode)
   {
      // Performance Fix Done by creating AccommodationViewData with required attributes
      final AccommodationViewData requiredAccommodationViewData = new AccommodationViewData();

      try
      {
         final AccommodationViewData accommodationViewData =
            searchFacade.getAccommodationPriceData(accommodationCode,
               (BookingComponentModel) componentFacade.getComponent(componentUid));
         requiredAccommodationViewData.setBoardBasis(accommodationViewData.getBoardBasis());
         requiredAccommodationViewData.setPriceFrom(accommodationViewData.getPriceFrom());
         requiredAccommodationViewData.setAvailableFrom(accommodationViewData.getAvailableFrom());
         requiredAccommodationViewData.setStayPeriod(accommodationViewData.getStayPeriod());
         requiredAccommodationViewData.setDeparturePoint(accommodationViewData.getDeparturePoint());
         requiredAccommodationViewData.setRoomOccupancy(accommodationViewData.getRoomOccupancy());
         return requiredAccommodationViewData;

      }
      catch (final NoSuchComponentException e)
      {
         LOG.error(LOG_MSG + componentUid + "]", e);
         return null;
      }
   }

}
