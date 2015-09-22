/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TuiProductUrlResolver;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MarkerMapViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangesData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author s.consolino
 *
 */
public class AccommodationInspirationMapPopulator implements
   Populator<List<AccommodationModel>, InspirationMapViewData>
{

   /**
     *
     */
   private static final String T_RATING = "tRating";

   /**
     *
     */
   private static final String INTRODUCTION = "introduction";

   private static final String INTRO1_BODY = "intro1Body";

   private static final String CLASSIFICATION = "classification";

   private static final String LOW_PER_PERSON_PRICE = "lowPerPersonPrice";

   private static final String LONGITUDE = "longitude";

   private static final String LATITUDE = "latitude";

   private static final String NAME = "name";

   @Resource
   private FeatureService featureService;

   @Resource
   private MediaDataPopulator mediaDataPopulator;

   @Resource
   private AccommodationSubCategoriesPopulator accomSubCategoriesPopulator;

   @Resource
   private TuiProductUrlResolver tuiProductUrlResolver;

   @Resource
   private AccommodationProductRangePopulator accommodationProductRangePopulator;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private BrandUtils brandUtils;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final List<AccommodationModel> source, final InspirationMapViewData target)
      throws ConversionException
   {
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");
      final List<MarkerMapViewData> markerMapViewDataList = new ArrayList<MarkerMapViewData>();
      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { LATITUDE, LONGITUDE, NAME, T_RATING }));

      for (final AccommodationModel accomModel : source)
      {
         final MarkerMapViewData marker = new MarkerMapViewData();
         final AccommodationViewData accommodationData = new AccommodationViewData();

         // First gets the data for the main marker
         final String brand = brandUtils.getFeatureServiceBrand(accomModel.getBrands());
         marker.setUrl(tuiProductUrlResolver.resolve(accomModel));
         getMarkerMapViewData(accomModel, marker, brand);

         accommodationData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
            featureDescriptorList, accomModel, new Date(), brand));

         marker.setName(accommodationData.getFeatureValue(NAME));
         // This gets the sublocations for a particular location using the sublocation populator
         final LocationData locationData = new LocationData();
         accomSubCategoriesPopulator.populate(accomModel, locationData);
         locationData.setProductRanges(getProductRangeForAccom(accomModel));

         accommodationData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
            featureDescriptorList, accomModel, new Date(), brand));
         accommodationData.setUrl(tuiProductUrlResolver.resolve(accomModel));
         accommodationData.setBrand(brand);
         accommodationProductRangePopulator.populate(accomModel, accommodationData);
         final MediaModel thumbnail = accomModel.getThumbnail();
         if (thumbnail != null)
         {
            final MediaViewData mediaData = new MediaViewData();
            mediaDataPopulator.populate(thumbnail, mediaData);
            accommodationData.setThumbnail(mediaData);
         }
         final List<AccommodationViewData> accomViewData = new ArrayList<AccommodationViewData>();
         accomViewData.add(accommodationData);
         locationData.setAccommodations(accomViewData);
         locationData.setBrand(brand);
         marker.setName(getFirstFeatureValue(accommodationData.getFeatureValues(NAME)).toString());
         marker.setLocation(locationData);
         marker.setLocations(locationData.getSubLocations());
         marker.setCode(accomModel.getCode());
         if (viewSelector.checkIsMobile()
            && CollectionUtils.isEmpty(marker.getLocation().getProductRanges()))
         {
            final List<ProductRangesData> productRangeList = new ArrayList<ProductRangesData>();
            int i = 0;
            for (final ProductRangeViewData prdViewData : accommodationData.getProductRanges())
            {
               final ProductRangesData prd = new ProductRangesData();
               prd.setCode(prdViewData.getCode());
               productRangeList.add(i, prd);
               i++;
            }
            marker.getLocation().setProductRanges(productRangeList);
         }

         markerMapViewDataList.add(marker);
      }
      target.setMarkerMapViewDataList(markerMapViewDataList);
      target.setMarkupListCount(markerMapViewDataList.size());
   }

   private MarkerMapViewData getMarkerMapViewData(final AccommodationModel locationModel,
      final MarkerMapViewData markerMapViewData, final String brand)
   {

      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { NAME, LATITUDE, LONGITUDE,
            LOW_PER_PERSON_PRICE, CLASSIFICATION, INTRO1_BODY, INTRODUCTION }));
      final Map<String, List<Object>> features =
         featureService.getOptimizedValuesForFeatures(featureDescriptorList, locationModel,
            new Date(), brand);

      markerMapViewData.setName(getFirstFeatureValue(features.get(NAME)).toString());
      markerMapViewData.setLatitude(getFirstFeatureValue(features.get(LATITUDE)).toString());
      markerMapViewData.setLongitude(getFirstFeatureValue(features.get(LONGITUDE)).toString());
      markerMapViewData.setPrice(getFirstFeatureValue(features.get(LOW_PER_PERSON_PRICE))
         .toString());
      markerMapViewData.setClassification(getFirstFeatureValue(features.get(CLASSIFICATION))
         .toString());
      if (viewSelector.checkIsMobile())
      {
         markerMapViewData
            .setIntroText(getFirstFeatureValue(features.get(INTRODUCTION)).toString());
      }

      if (locationModel.getThumbnail() != null)
      {
         final MediaViewData mediaData = new MediaViewData();
         mediaDataPopulator.populate(locationModel.getThumbnail(), mediaData);
         markerMapViewData.setThumbnail(mediaData);
      }
      return markerMapViewData;
   }

   private List<ProductRangesData> getProductRangeForAccom(final AccommodationModel accModel)
   {
      final Collection<CategoryModel> categoryModelList = accModel.getSupercategories();
      final List<ProductRangesData> productRangesDatas = new ArrayList<ProductRangesData>();
      for (final CategoryModel categoryModel : categoryModelList)
      {
         if (categoryModel instanceof ProductRangeModel)
         {

            final ProductRangeModel productRangeModel = (ProductRangeModel) categoryModel;
            // this is done as FC units should not be differentiated in TH and vice-versa.
            if (StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(),
               brandUtils.getFeatureServiceBrand(productRangeModel.getBrands())))
            {
               final ProductRangesData data = new ProductRangesData();
               data.setCode(productRangeModel.getCode());
               productRangesDatas.add(data);
            }

            return productRangesDatas;
         }
      }
      return Collections.emptyList();
   }

   /**
    * Returns the first value from the list
    *
    * @param featureValues
    * @return object
    */
   private Object getFirstFeatureValue(final List<Object> featureValues)
   {
      if (featureValues != null && !featureValues.isEmpty())
      {
         return featureValues.get(0);
      }
      return "";
   }
}
