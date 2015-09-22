/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.NonCoreProductIndicator;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;

/**
 * @author deepakkumar.k
 *
 */
public class NonCoreProductsServiceImpl implements NonCoreProductsService
{

   /**
     *
     */
   private static final String MEDIUM = "medium";

   private static final String SMALL = "small";

   private static final String MULTI_CENTRE = "multi_centre";

   private static final String HOLIDAY_TYPE = "holiday_type";

   @Resource
   private LinkedItemService linkedItemService;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.services.NonCoreProductsService#getNonCoreProducts
    * (uk.co.portaltech.travel.model.AccommodationModel,
    * uk.co.portaltech.tui.web.view.data.SearchResultViewData, java.lang.String)
    */
   @Override
   public void checkingNonCoreProducts(final AccommodationModel source,
      final SearchResultViewData target, final String siteBrand)
   {

      final NonCoreProductIndicator product = new NonCoreProductIndicator();
      populateNonCoreIdentifier(source, target, product);
   }

   /**
    * @param source
    * @param target
    * @param product
    */
   private void populateNonCoreIdentifier(final AccommodationModel source,
      final SearchResultViewData target, final NonCoreProductIndicator product)
   {

      final String holidayType = target.getAccommodation().getFeatureValue(HOLIDAY_TYPE);
      final List<String> multipleStay =
         StringUtil.split("Twin_MultiCentre,NileCruiseandStay,SafariandStay,TourandStay", ",");
      final List<String> singleStay = StringUtil.split("NileCruise,GuletCruise,DayTrip", ",");
      if (StringUtils.isNotEmpty(holidayType))
      {

         if ("ThemePark".equalsIgnoreCase(holidayType))
         {

            checkForThemeParkUnderNonCoreProduct(source, product);
            if (!product.isSingleStay())
            {
               product.setMultiStay(true);
            }

         }
         else
         {

            checkingHolidayTypesUnderNonCoreProduct(holidayType, product, singleStay, multipleStay);

         }

         target.setNonCoreProduct(product);
         // added to populate multiple stay images
         target.getAccommodation().setMultiStayImages(populateMultiStayImages(source, product));
         target.getAccommodation().setImageUrlsMap(populateMultiStayImageMap(source, product));

      }
      else
      {
         checkingLaplandPackageExcludingDayTrip(source, target);

      }
   }

   /**
    * @param source
    * @param product
    */
   private List<String> populateMultiStayImages(final AccommodationModel source,
      final NonCoreProductIndicator product)
   {

      if (product.isMultipleStay())
      {
         final List<String> medias = new LinkedList<String>();
         final List<AccommodationModel> accommodationModels =
            linkedItemService.getLinkedItemsOfAccommodationType(source.getCode());

         if (CollectionUtils.isNotEmpty(accommodationModels))
         {
            for (final AccommodationModel accom : linkedItemService
               .getLinkedItemsOfAccommodationType(source.getCode()))
            {
               if (CollectionUtils.isNotEmpty(accom.getGalleryImages()))
               {
                  for (final MediaContainerModel mediaContainerModel : accom.getGalleryImages())
                  {
                     if (mediaContainerModel != null)
                     {
                        for (final MediaModel media : mediaContainerModel.getMedias())
                        {
                           if (media.getMediaFormat().getName() != null
                              && MEDIUM.equalsIgnoreCase(media.getMediaFormat().getName()))
                           {
                              medias.add(media.getURL());
                              break;
                           }
                        }
                     }
                     break;
                  }
               }
            }
         }

         return medias;
      }

      return Collections.emptyList();
   }

   private Map<String, List<String>> populateMultiStayImageMap(final AccommodationModel source,
      final NonCoreProductIndicator product)
   {
      final Map<String, List<String>> imageUrlMap = new HashMap<String, List<String>>();
      if (product.isMultipleStay())
      {
         final List<String> mediaMedium = new LinkedList<String>();
         final List<String> mediasSmall = new LinkedList<String>();
         final List<AccommodationModel> accommodationModels =
            linkedItemService.getLinkedItemsOfAccommodationType(source.getCode());
         String imageFormatMedium = null;
         String imageFormatSmall = null;

         if (CollectionUtils.isNotEmpty(accommodationModels))
         {
            for (final AccommodationModel accom : linkedItemService
               .getLinkedItemsOfAccommodationType(source.getCode()))
            {
               if (CollectionUtils.isNotEmpty(accom.getGalleryImages()))
               {
                  for (final MediaContainerModel mediaContainerModel : accom.getGalleryImages())
                  {
                     if (mediaContainerModel != null)
                     {
                        for (final MediaModel media : mediaContainerModel.getMedias())
                        {
                           if (media.getMediaFormat().getName() != null
                              && MEDIUM.equalsIgnoreCase(media.getMediaFormat().getName()))
                           {
                              imageFormatMedium =
                                 MULTI_CENTRE + "_" + media.getMediaFormat().getName();
                              mediaMedium.add(media.getURL());
                           }
                           else if (media.getMediaFormat().getName() != null
                              && SMALL.equalsIgnoreCase(media.getMediaFormat().getName()))
                           {
                              imageFormatSmall =
                                 MULTI_CENTRE + "_" + media.getMediaFormat().getName();
                              mediasSmall.add(media.getURL());
                           }
                        }
                        break;
                     }
                  }
               }
            }
         }

         imageUrlMap.put(imageFormatMedium, mediaMedium);
         imageUrlMap.put(imageFormatSmall, mediasSmall);
         return imageUrlMap;
      }

      return imageUrlMap;
   }

   /**
    * @param source
    * @param target
    */
   private void checkingLaplandPackageExcludingDayTrip(final AccommodationModel source,
      final SearchResultViewData target)
   {

      // looking for lapland packages,excluding Daytrip.
      if (target.getNonCoreProduct() == null)
      {
         LocationModel locationModel = null;

         for (final CategoryModel category : source.getSupercategories())
         {
            if (category instanceof LocationModel)
            {
               locationModel = (LocationModel) category;

               for (final CategoryModel loc : locationModel.getAllSupercategories())
               {
                  if (loc instanceof LocationModel)
                  {
                     final LocationModel location = (LocationModel) loc;

                     if ("lapland".equalsIgnoreCase(location.getName()))
                     {
                        final NonCoreProductIndicator product = new NonCoreProductIndicator();
                        product.setSingleStay(true);
                        target.setNonCoreProduct(product);

                     }
                  }

               }
            }
         }
      }

   }

   /**
    * @param holidayType
    * @param product
    * @param singleStay
    * @param multipleStay
    */
   private void checkingHolidayTypesUnderNonCoreProduct(final String holidayType,
      final NonCoreProductIndicator product, final List<String> singleStay,
      final List<String> multipleStay)
   {
      if (!"ThemePark".equalsIgnoreCase(holidayType))
      {
         for (final String multiple : multipleStay)
         {
            if (multiple.equalsIgnoreCase(holidayType))
            {
               product.setMultiStay(true);
               product.setMultipleStay(true);
            }
         }
         for (final String single : singleStay)
         {
            if (single.equalsIgnoreCase(holidayType))
            {
               product.setSingleStay(true);
            }
         }
      }
   }

   /**
    * @param source
    * @param product
    */
   private void checkForThemeParkUnderNonCoreProduct(final AccommodationModel source,
      final NonCoreProductIndicator product)
   {
      LocationModel locationModel = null;

      for (final CategoryModel category : source.getSupercategories())
      {
         if (category instanceof LocationModel)
         {
            locationModel = (LocationModel) category;

            for (final CategoryModel loc : locationModel.getAllSupercategories())
            {

               if (loc instanceof LocationModel)
               {
                  final LocationModel location = (LocationModel) loc;

                  if ("KEN".equals(location.getCode()))
                  {

                     product.setSingleStay(true);

                  }
               }

            }

         }
      }
   }

}
