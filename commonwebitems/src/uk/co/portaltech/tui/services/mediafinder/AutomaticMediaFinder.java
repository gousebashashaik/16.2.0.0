/**
 *
 */
package uk.co.portaltech.tui.services.mediafinder;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.travel.media.services.AccommodationMediaService;
import uk.co.portaltech.travel.media.services.LocationMediaService;
import uk.co.portaltech.travel.media.services.domain.Media;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.tui.async.logging.TUILogUtils;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * @author omonikhide
 *
 */
public class AutomaticMediaFinder implements MediaFinder
{

   private static final TUILogUtils LOG = new TUILogUtils("AutomaticMediaFinder");

   private static final String ERROR = "does not have media container model";

   // @Autowired

   @Resource
   private CategoryService categoryService;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private AttractionService attractionService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Autowired
   private Populator<Collection<MediaContainerModel>, List<MediaViewData>> heroImagesPopulator;

   @Resource
   private Converter<AccommodationModel, AccommodationViewData> accommodationConverter;

   @Resource
   private Converter<AttractionModel, AttractionViewData> attractionConverter;

   @Resource
   private Converter<ExcursionModel, ExcursionViewData> excursionConverter;

   @Resource
   private LocationConverter locationConverter;

   @Resource(name = "tuiUtilityService")
   private TuiUtilityService tuiUtilityService;

   @Resource
   private AccommodationMediaService accommodationMediaService;

   @Resource
   private LocationMediaService locationMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   @Override
   public SearchResultData<MediaModel> search(final SearchRequestData request)
   {
      return null;
   }

   @Override
   @Cacheable(cacheName = "mediaFinderCache")
   public HasFeatures searchAutomatic(final SearchRequestData request, final String code,
      final String type)
   {
      LOG.debug("In automatic finder to get media data specific to: " + code);

      List<MediaContainerModel> galleryImages = null;
      if ("accommodation".equalsIgnoreCase(type))
      {

         final AccommodationModel accommodationModel =
            accommodationService.getAccomodationByCodeAndCatalogVersion(code,
               cmsSiteService.getCurrentCatalogVersion(), null);
         return accommodationSearch(request, accommodationModel);
      }
      else if ("attraction".equalsIgnoreCase(type))
      {
         final ItemModel itemModel =
            attractionService.getAttractionForCode(code, cmsSiteService.getCurrentCatalogVersion());
         AttractionModel attractionModel = null;
         ExcursionModel excursionModel = null;
         if (itemModel != null)
         {
            if (itemModel instanceof AttractionModel)
            {
               attractionModel = (AttractionModel) itemModel;
               galleryImages = (List<MediaContainerModel>) attractionModel.getGalleryImages();
               if (galleryImages != null && !galleryImages.isEmpty())
               {
                  return attractionViewSearch(request, galleryImages, attractionModel);
               }
            }
            else if (itemModel instanceof ExcursionModel)
            {
               excursionModel = (ExcursionModel) itemModel;
               galleryImages = excursionModel.getGalleryImages();
               if (galleryImages != null && !galleryImages.isEmpty())
               {
                  return excursionViewSearch(request, galleryImages, excursionModel);
               }
            }

         }
      }
      else if ("location".equalsIgnoreCase(type))
      {
         final LocationModel locationModel =
            (LocationModel) categoryService.getCategoryForCode(code);
         return locationSearch(request, locationModel);
      }

      return null;

   }

   @Override
   public HasFeatures searchManual(final SearchRequestData request, final String code,
      final String type)
   {

      return null;
   }

   @Override
   public HasFeatures searchAttractions(final SearchRequestData request, final String code)
   {

      return null;
   }

   /**
    * @param request
    * @param accommodationModel
    * @return AccommodationViewData.
    *
    *         This method returns the hero-carousel images for accommodation.
    *
    */
   private AccommodationViewData accommodationSearch(final SearchRequestData request,
      final AccommodationModel accommodationModel)
   {

      List<MediaContainerModel> galleryImages = null;
      AccommodationViewData localAccommodationViewData = null;
      try
      {
         List<MediaContainerModel> result = null;
         if (accommodationModel != null)
         {
            localAccommodationViewData = accommodationConverter.convert(accommodationModel);
            galleryImages = accommodationModel.getGalleryImages();

            if (galleryImages != null && !galleryImages.isEmpty())
            {

               if (galleryImages.size() > request.getPageSize())
               {
                  result = galleryImages.subList(request.getOffset(), request.getPageSize());
               }
               else
               {
                  result = galleryImages;
               }

               final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
               heroImagesPopulator.populate(result, imageDataList);

               localAccommodationViewData.setGalleryImages(imageDataList);
               localAccommodationViewData.setGalleryImageVisibleItems(request.getPageSize());

            }

            final List<Media> videos =
               accommodationMediaService.getVideoMedias(accommodationModel,
                  tuiUtilityService.getBrandForModel(accommodationModel));

            final List<MediaViewData> videoDataList = new ArrayList<MediaViewData>();
            mediaPopulatorLite.populate(videos, videoDataList);
            if (CollectionUtils.isNotEmpty(videoDataList))
            {
               localAccommodationViewData.setGalleryVideos(videoDataList);
            }
            localAccommodationViewData.setGalleryImageVisibleItems(request.getPageSize());
         }
      }
      catch (final NullPointerException e)
      {
         LOG.error(ERROR, e);
      }

      return localAccommodationViewData;

   }

   /**
    * @param request
    * @param locationModel
    * @return LocationData
    *
    *         This method returns the hero-carousel images for location.
    */
   private LocationData locationSearch(final SearchRequestData request,
      final LocationModel locationModel)
   {
      LocationData localLocationData = null;

      try
      {
         List<MediaContainerModel> result = null;

         if (locationModel != null)
         {
            localLocationData = locationConverter.convert(locationModel);

            final Collection<MediaContainerModel> mediaContainers =
               locationModel.getGalleryImages();
            if (mediaContainers != null && !mediaContainers.isEmpty())
            {
               if (mediaContainers.size() > request.getPageSize())
               {
                  result =
                     ((List<MediaContainerModel>) mediaContainers).subList(request.getOffset(),
                        request.getPageSize());
               }
               else
               {
                  result = (List<MediaContainerModel>) mediaContainers;
               }
               final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
               heroImagesPopulator.populate(result, imageDataList);

               localLocationData.setGalleryImages(imageDataList);
               localLocationData.setGalleryImageVisibleItems(request.getPageSize());
            }
            localLocationData.setCode(locationModel.getCode());

            // To be enabled when videos are added at locaiton level

            final List<Media> videos = locationMediaService.getVideoMedias(locationModel);
                  final List<MediaViewData> videoDataList = new ArrayList<MediaViewData>();
            mediaPopulatorLite.populate(videos, videoDataList);
            localLocationData.setGalleryVideos(videoDataList);
         }

      }
      catch (final NullPointerException e)
      {
         LOG.error(ERROR, e);
      }
      return localLocationData;
   }

   /**
    * @param request
    * @param galleryImages
    * @param attractionModel
    * @return AttractionViewData
    *
    *         This method returns the hero -carousel images for attractions.
    */
   private AttractionViewData attractionViewSearch(final SearchRequestData request,
      final List<MediaContainerModel> galleryImages, final AttractionModel attractionModel)
   {

      AttractionViewData attractionViewData = null;
      try
      {
         List<MediaContainerModel> result = null;
         if (galleryImages.size() > request.getPageSize())
         {
            result = galleryImages.subList(request.getOffset(), request.getPageSize());
         }
         else
         {
            result = galleryImages;
         }
         final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
         heroImagesPopulator.populate(result, imageDataList);
         attractionViewData = attractionConverter.convert(attractionModel);
         attractionViewData.setGalleryImages(imageDataList);
         attractionViewData.setGalleryImageVisibleItems(request.getPageSize());
      }
      catch (final NullPointerException e)
      {

         LOG.error(ERROR, e);
      }
      return attractionViewData;
   }

   /**
    * @param request
    * @param galleryImages
    * @param excursionModel
    * @return ExcursionViewData
    *
    *         This method returns the hero-carousel images for excursions.
    */
   private ExcursionViewData excursionViewSearch(final SearchRequestData request,
      final List<MediaContainerModel> galleryImages, final ExcursionModel excursionModel)
   {

      ExcursionViewData excursionViewData = null;
      try
      {
         List<MediaContainerModel> result = null;
         if (galleryImages.size() > request.getPageSize())
         {
            result = galleryImages.subList(request.getOffset(), request.getPageSize());
         }
         else
         {
            result = galleryImages;
         }
         final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
         heroImagesPopulator.populate(result, imageDataList);
         excursionViewData = excursionConverter.convert(excursionModel);
         excursionViewData.setGalleryImages(imageDataList);
         excursionViewData.setGalleryImageVisibleItems(request.getPageSize());
      }
      catch (final NullPointerException e)
      {

         LOG.error(ERROR, e);
      }
      return excursionViewData;

   }
}
