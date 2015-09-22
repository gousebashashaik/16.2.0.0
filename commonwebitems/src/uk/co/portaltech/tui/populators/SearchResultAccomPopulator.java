/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.cache.SiteAwareKeyGenerator;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.media.services.AccommodationMediaService;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.thirdparty.endeca.enums.BrandName;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MapPositionsData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

/**
 *
 */
public class SearchResultAccomPopulator implements
   Populator<AccommodationModel, SearchResultViewData>
{

   /**
     *
     */
   private static final String T_RATING = "tRating";

   private static final String NAME = "name";

   private static final String STRAP_LINE = "strapline";

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private FeatureService featureService;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private MediaDataPopulator mediaDataPopulator;

   @Resource(name = "tuiUtilityService")
   private TuiUtilityService tuiUtilityService;

   @Resource
   private LocationFacade locationFacade;

   @Resource
   private CacheUtil cacheUtil;

   @Resource(name = "siteAwareKeyGenerator")
   private SiteAwareKeyGenerator keyGenerator;

   private static final TUILogUtils LOG = new TUILogUtils("SearchResultAccomPopulator");

   @Resource
   private Converter<AccommodationModel, AccommodationViewData> accommodationConverter;

   private static final int TWO = 2;

   @Resource
   private AccommodationMediaService accommodationMediaService;
   /**
     *
     */
   public SearchResultAccomPopulator()
   {

   }

   /**
     *
     */
   public SearchResultAccomPopulator(final FeatureService featureService)
   {

      this.featureService = featureService;
   }

   @Override
   public void populate(final AccommodationModel accomModel, final SearchResultViewData target)
      throws ConversionException
   {

      final Map locations = new LinkedHashMap<String, String>();
      final AccommodationModel source = accomModel;
      AccommodationViewData accommodationViewData = null;
      target.getAccommodation().setBrands(accomModel.getBrands());

      // Code written to know the accommodation is either falcon or thomson ireland.
      for (final BrandType brandType : accomModel.getBrands())
      {
         if ("FJ".equalsIgnoreCase(brandType.getCode())
            || "TI".equalsIgnoreCase(brandType.getCode()))
         {
            target.getAccommodation().setBrand(BrandType.valueOf(brandType.getCode()));
            break;
         }
      }
      final Map<Integer, List<MediaViewData>> imagesList =
         new HashMap<Integer, List<MediaViewData>>();
      populateCategoryContent(target, source.getSupercategories(), locations);
      target.getAccommodation().setLocationMap(locations);
      target.getAccommodation().setName(source.getName());
      /**
       * Changed this code to fix Hotel name difference in Search result page again Accom details
       * page
       **/

      target.getAccommodation().setAccomType(source.getType().getCode());
      getRatings(source, target);

      if (source.getReviewsCount() != null)
      {
         target.getAccommodation().setTripAdvisorReviewCount(source.getReviewsCount().intValue());
      }

      try
      {
         if (viewSelector.checkIsMobile() && source.getGalleryImages() != null)
         {
            Integer index = 0;
            final List<MediaViewData> imageDataListForMobile = new ArrayList<MediaViewData>();
            for (final MediaContainerModel mediaContainerModel : source.getGalleryImages())
            {
               final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();

               if (mediaContainerModel != null)
               {
                  for (final MediaModel media : mediaContainerModel.getMedias())
                  {

                     final MediaViewData mediaData = new MediaViewData();
                     media.setDescription(mediaContainerModel.getCaption());
                     mediaDataPopulator.populate(media, mediaData);
                     source.setThumbnail(media);
                     imageDataList.add(mediaData);
                     imageDataListForMobile.add(mediaData);
                  }
               }
               imagesList.put(index, imageDataList);
               index++;
               target.getAccommodation().setGalleryImages(imageDataList);
            }
            target.getAccommodation().setGalleryImages(imageDataListForMobile);
            target.getAccommodation().setImages(imagesList);
         }

      }
      catch (final JaloItemNotFoundException jaloItemNotFoundException)
      {
         LOG.debug("jaloItemNotFoundException: mediaContainer not found for accommodation code :"
            + source.getCode() + "name :" + source.getName(), jaloItemNotFoundException);
      }
      catch (final JaloSystemException jaloSystemException)
      {
         LOG.debug("jaloSystemException: mediaContainer not found for accommodation code :"
            + source.getCode() + "name :" + source.getName(), jaloSystemException);
      }

      target.getAccommodation().setImageUrl(setImageUrl(source));

      target.getAccommodation().setImageUrlsMap(setImageUrlsForMap(source));

      target.getAccommodation().setVideoPresent(checkIfGalleryVideoPresent(source));

      try
      {
         if (viewSelector.checkIsMobile())
         {
            final String mapType = "hotels";
            String topLocationName = null;

            final Collection<CategoryModel> supercategories = source.getSupercategories();
            if (supercategories != null && !supercategories.isEmpty())
            {
               for (final CategoryModel categoryModel : supercategories)
               {
                  if (categoryModel instanceof LocationModel)
                  {
                     final LocationModel locationModel = (LocationModel) categoryModel;
                     topLocationName =
                        locationFacade.getLocationData(locationModel.getCode()).getName();
                  }
               }
            }
            final int id = 1;
            accommodationViewData = accommodationConverter.convert(source);
            target.getAccommodation().setPositionsData(
               updateMapData(id, mapType, accommodationViewData, target.getAccommodation()
                  .getFeatureCodesAndValues(), target.getAccommodation().getImageUrl(),
                  topLocationName));
            target.getAccommodation().setDataTitle(updateMapTitleDataForMobile());

         }
      }
      catch (final UnknownIdentifierException uke)
      {
         LOG.error("Error loading usps from Product with Code:" + source.getCode(), uke);
      }

   }

   /**
    * @param source
    */
   private boolean checkIfGalleryVideoPresent(final AccommodationModel source)
   {
      return CollectionUtils.isNotEmpty(accommodationMediaService.getVideoMedias(source,
         tuiUtilityService.getBrandForModel(source)));
   }

   /**
    * @param source
    */
   private String setImageUrl(final AccommodationModel source)
   {

      final String key = keyGenerator.generate("setImageUrl", source.getCode());
      final CacheWrapper<String, String> imageUrlCache =
         cacheUtil.getAccomImageUrlInSearchCacheWrapper();

      // get from cache
      String imageUrl = imageUrlCache.get(key);

      if (StringUtils.isBlank(imageUrl) && source.getGalleryImages() != null)
      {
         for (final MediaContainerModel mediaContainerModel : source.getGalleryImages())
         {
            if (mediaContainerModel != null)
            {
               for (final MediaModel media : mediaContainerModel.getMedias())
               {
                  if (media.getMediaFormat().getName() != null
                     && "medium".equalsIgnoreCase(media.getMediaFormat().getName()))
                  {
                     imageUrl = media.getURL();
                     // put in cache
                     imageUrlCache.put(key, imageUrl);
                     break;
                  }
               }
            }
            break;
         }
      }

      return imageUrl;
   }

   private Map<String, List<String>> setImageUrlsForMap(final AccommodationModel source)
   {

      Map<String, List<String>> imageUrlMap = null;
      final String keyForImage = keyGenerator.generate("setImageUrlMap", source.getCode());
      final CacheWrapper<String, Map<String, List<String>>> imageUrlMapCache =
         cacheUtil.getAccomImageUrlMapInSearchCacheWrapper();

      // get from cache
      imageUrlMap = imageUrlMapCache.get(keyForImage);

      if (null == imageUrlMap)
      {
         imageUrlMap = new HashMap<String, List<String>>();
      }

      if (MapUtils.isEmpty(imageUrlMap) && source.getGalleryImages() != null)
      {
         for (final MediaContainerModel mediaContainerModel : source.getGalleryImages())
         {
            if (mediaContainerModel != null)
            {
               for (final MediaModel media : mediaContainerModel.getMedias())
               {
                  if (media.getMediaFormat().getName() != null
                     && ("medium".equalsIgnoreCase(media.getMediaFormat().getName()) || "small"
                        .equalsIgnoreCase(media.getMediaFormat().getName())))
                  {
                     final List<String> lstImageUrl = new ArrayList<String>();
                     lstImageUrl.add(media.getURL());
                     imageUrlMap.put(media.getMediaFormat().getName(), lstImageUrl);
                  }
               }
               break;
            }
         }

         // put in cache
         imageUrlMapCache.put(keyForImage, imageUrlMap);
      }

      return imageUrlMap;
   }

   public void populateCategoryContent(final SearchResultViewData target,
      final Collection<CategoryModel> productCategories, final Map locations)
   {
      if (CollectionUtils.isNotEmpty(productCategories))
      {
         for (final CategoryModel category : productCategories)
         {
            processCategory(target, category, locations);
         }
      }
   }

   /**
    * @param target
    * @param category
    */
   private void processCategory(final SearchResultViewData target, final CategoryModel category,
      final Map locations)
   {
      if (category instanceof LocationModel)
      {
         if (locations.size() < TWO)
         {
            addLocation(target, category, locations);
         }

      }
      else if (category instanceof ProductRangeModel)
      {
         addProductRange(target, category);
      }
   }

   /**
    * @param target
    * @param category
    */
   private void addLocation(final SearchResultViewData target, final CategoryModel category,
      final Map locations)
   {

      final LocationModel locModel = (LocationModel) category;
      locations.put(locModel.getType().getCode(), locModel.getName());
      populateCategoryContent(target, locModel.getSupercategories(), locations);

      /** added to fix bug 112463 */
      if (locModel.getType().equals(LocationType.DESTINATION))
      {
         target.getAccommodation().getLocation().getDestination().setCode(category.getCode());

      }
      else if (locModel.getType().equals(LocationType.RESORT))
      {

         target.getAccommodation().getLocation().getResort().setCode(category.getCode());

      }
      else if (locModel.getType().equals(LocationType.COUNTRY))
      {

         target.getAccommodation().getLocation().getCountry().setCode(category.getCode());

      }
      else if (locModel.getType().equals(LocationType.REGION))
      {

         target.getAccommodation().getLocation().getRegion().setCode(category.getCode());

      }
   }

   /**
    * @param target
    * @param category
    */
   private void addProductRange(final SearchResultViewData target, final CategoryModel category)
   {

      final ProductRangeModel prodRange = (ProductRangeModel) category;

      final String siteBrand = tuiUtilityService.getSiteBrand();
      final String productRangeBrand = brandUtils.getFeatureServiceBrand(prodRange.getBrands());
      final String holidayBrand = target.getBrandType();
      /*
       * RETAIL CODE
       */
      final String aniteBrandEnum = BrandName.valueOf(siteBrand).getBrandName();

      if ("FJ".equalsIgnoreCase(siteBrand)
         && prodRange.getBrands().contains(target.getAccommodation().getBrand()))
      {
         addFalconProductRange(target, category, prodRange);

      }
      else if (StringUtils.equalsIgnoreCase(holidayBrand, aniteBrandEnum)
         && StringUtils.equalsIgnoreCase(siteBrand, productRangeBrand))
      {
         addTHFCProductRange(target, category, prodRange);
      }

   }

   /**
    * @param target
    * @param category
    * @param prodRange
    */
   private void addFalconProductRange(final SearchResultViewData target,
      final CategoryModel category, final ProductRangeModel prodRange)
   {
      final String featureValue =
         target.getAccommodation().getFeatureValue("falcon_product_override");
      if (!StringUtils.equalsIgnoreCase(featureValue, "ZZZZZZ"))
      {
         target.getAccommodation().setDifferentiatedType(category.getName());
         target.getAccommodation().setDifferentiatedCode(category.getCode());
         target.getAccommodation().setDifferentiatedProduct(true);

         Map<String, List<Object>> featureCodeMap = null;
         try
         {
            featureCodeMap =
               featureService.getOptimizedValuesForFeatures(
                  Arrays.asList(new String[] { STRAP_LINE }), prodRange, new Date(), null);
            target.getAccommodation().putFeatureCodesAndValues(featureCodeMap);
         }
         catch (final UnknownIdentifierException uke)
         {
            LOG.error("Error loading strapline from Product with Code:" + prodRange.getCode(), uke);
         }
      }
   }

   /**
    * @param target
    * @param category
    * @param prodRange
    */
   private void addTHFCProductRange(final SearchResultViewData target,
      final CategoryModel category, final ProductRangeModel prodRange)
   {
      target.getAccommodation().setDifferentiatedType(category.getName());
      target.getAccommodation().setDifferentiatedCode(category.getCode());
      target.getAccommodation().setDifferentiatedProduct(true);

      Map<String, List<Object>> featureCodeMap = null;
      try
      {
         featureCodeMap =
            featureService.getOptimizedValuesForFeatures(
               Arrays.asList(new String[] { STRAP_LINE }), prodRange, new Date(), null);
         target.getAccommodation().putFeatureCodesAndValues(featureCodeMap);
      }
      catch (final UnknownIdentifierException uke)
      {
         LOG.error("Error loading strapline from Product with Code:" + prodRange.getCode(), uke);
      }
   }

   /**
    * @param target
    * @param source
    * @Converts AccommodationModel instance to ViewData
    */
   public void getRatings(final AccommodationModel source, final SearchResultViewData target)
   {
      try
      {

         if (accomFeatureCheck(target)
            && target.getAccommodation().getFeatureCodesAndValues().get(T_RATING) != null
            && CollectionUtils.isNotEmpty(target.getAccommodation().getFeatureCodesAndValues()
               .get(T_RATING)))
         {

            final String officialRating =
               target.getAccommodation().getFeatureCodesAndValues().containsKey(T_RATING) ? target
                  .getAccommodation().getFeatureCodesAndValues().get(T_RATING).get(0).toString()
                  : StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(officialRating))
            {
               target.getAccommodation().getRatings().setOfficialRating(officialRating);
            }
         }

      }
      catch (final UnknownIdentifierException uke)
      {
         LOG.error("Error loading Ratings from Product with Code:" + source.getCode(), uke);
      }
   }

   /**
    * @param target
    * @return
    */
   private boolean accomFeatureCheck(final SearchResultViewData target)
   {
      return target != null && target.getAccommodation() != null
         && target.getAccommodation().getFeatureCodesAndValues() != null;
   }

   private MapPositionsData updateMapData(final int id, final String mapType,
      final AccommodationViewData accomData, final Map<String, List<Object>> featureCodesAndValues,
      final String infoUrl, final String topLocationName)
   {
      if ((CollectionUtils.isNotEmpty(featureCodesAndValues.get("latitude")))
         || (CollectionUtils.isNotEmpty(featureCodesAndValues.get("longitude"))))
      {
         final MapPositionsData mapPositionsData = new MapPositionsData();
         final Map<String, String> info = new HashMap<String, String>();
         mapPositionsData.setId(String.valueOf(id));
         mapPositionsData.setLat(featureCodesAndValues.get("latitude").get(0).toString());
         mapPositionsData.setLng(featureCodesAndValues.get("longitude").get(0).toString());
         mapPositionsData.setName(accomData.getName());
         mapPositionsData.setType(mapType);

         mapPositionsData.setUrl(accomData.getUrl());

         info.put("link", accomData.getUrl());
         info.put("topLocationName", topLocationName);

         if (infoUrl != null)
         {
            info.put("url", infoUrl);
         }
         else
         {
            info.put("url", "");
         }

         if (featureCodesAndValues.containsKey(STRAP_LINE)
            && !featureCodesAndValues.get(STRAP_LINE).isEmpty())
         {
            info.put("description", featureCodesAndValues.get(STRAP_LINE).get(0).toString());
         }
         else
         {
            info.put("description", "");
         }

         if (featureCodesAndValues.containsKey(T_RATING)
            && !featureCodesAndValues.get(T_RATING).isEmpty())
         {
            info.put(T_RATING, featureCodesAndValues.get(T_RATING).get(0).toString());
         }
         else
         {
            info.put(T_RATING, "");
         }
         if (featureCodesAndValues.containsKey(NAME) && !featureCodesAndValues.get(NAME).isEmpty())
         {
            info.put(NAME, featureCodesAndValues.get(NAME).get(0).toString());
         }
         info.put("template", "/holiday/mobile/js/tui/widget/maps/templates/info_box.html");

         mapPositionsData.setInfo(info);

         if ("1".equals(mapPositionsData.getId()))
         {
            mapPositionsData.setSelected(true);
         }
         return mapPositionsData;
      }
      return null;
   }

   private Map<String, String> updateMapTitleDataForMobile()
   {

      final Map<String, String> mapTitledata = new HashMap<String, String>();

      mapTitledata.put(NAME, "Location");

      return mapTitledata;
   }

}
