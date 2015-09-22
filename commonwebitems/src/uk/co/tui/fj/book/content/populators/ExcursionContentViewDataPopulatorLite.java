/**
 *
 */
package uk.co.tui.fj.book.content.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.services.ExcursionService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.fj.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.fj.book.view.data.ExtraFacilityContentViewData;

/**
 * @author pradeep.as
 *
 */
public class ExcursionContentViewDataPopulatorLite implements
   Populator<ExtraFacility, ExtraFacilityCategoryViewData>
{

   /** The feature service. */
   @Resource
   private FeatureService featureService;

   /** The excursion service. */
   @Resource
   private ExcursionService excursionService;

   /** The log. */
   // declared variable in small case because of checkstyle warning
   private static final TUILogUtils LOG = new TUILogUtils("ExcursionContentViewDataPopulatorLite");

   /**
    * Populate the ExtrafacilityCategories for the view.
    *
    * @param source the source
    * @param target the target
    * @throws ConversionException the conversion exception
    */
   @Override
   public void populate(final ExtraFacility source, final ExtraFacilityCategoryViewData target)
      throws ConversionException
   {

      final ExcursionModel excursionModel =
         excursionService.getExcursionForInventoryCode(source.getExtraFacilityCode());
      logXcursionModelNotFoundIssue(excursionModel, source.getExtraFacilityCode());
      if (validXcusion(excursionModel))
      {
         buildExtraFacilityContent(target, excursionModel);
      }

   }

   /**
    * @param excursionModel
    * @return boolean.
    */
   private boolean validXcusion(final ExcursionModel excursionModel)
   {
      return SyntacticSugar.isNotNull(excursionModel);
   }

   /**
    * @param excursionModel
    * @param extraFacilityCode
    */
   private void logXcursionModelNotFoundIssue(final ExcursionModel excursionModel,
      final String extraFacilityCode)
   {
      if (!validXcusion(excursionModel))
      {
         LOG.debug("Xcursion not found for the extra facility code ::" + extraFacilityCode);
      }
   }

   /**
    * @param target
    * @param excursionModel
    */
   private void buildExtraFacilityContent(final ExtraFacilityCategoryViewData target,
      final ExcursionModel excursionModel)
   {
      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { "name", "editorialContent", "included",
            "notIncluded", "duration", "itinerary" }));
      final Map<String, String> extraFacilityContent = new ConcurrentHashMap<String, String>();
      final Map<String, List<Object>> features =
         featureService.getValuesForFeatures(featureDescriptorList, excursionModel, new Date(),
            null);
      for (final Entry<String, List<Object>> entry : features.entrySet())
      {
         if (validFeature(entry))
         {
            extraFacilityContent.put(entry.getKey(), entry.getValue().get(0).toString());
         }
      }
      populateLatitudeLongitude(excursionModel, extraFacilityContent);
      target.getExtraContent().putExtraFacilityContent(extraFacilityContent);
      getMediaDataForExcursion(excursionModel, target.getExtraContent());
   }

   /**
    * @param entry
    * @return
    */
   private boolean validFeature(final Entry<String, List<Object>> entry)
   {
      return CollectionUtils.isNotEmpty(entry.getValue())
         && SyntacticSugar.isNotNull(entry.getValue().get(0));
   }

   /**
    * Gets the Latitude and Longitude.
    *
    * @param source the source
    * @param extraFacilityContent the extra facility content
    */
   private void populateLatitudeLongitude(final ExcursionModel source,
      final Map<String, String> extraFacilityContent)
   {
      if (CollectionUtils.isNotEmpty(source.getExcursionPrices()))
      {
         final Map<String, List<Object>> latLong = getExcurisonLatitudeLongitude(source);
         addLatitudeLongitude(extraFacilityContent, latLong);
      }
   }

   /**
    * Sets the latitude longitude.
    *
    * @param extraFacilityContent the extra facility content
    * @param latLong the latitude and longitude for the excursion
    */
   private void addLatitudeLongitude(final Map<String, String> extraFacilityContent,
      final Map<String, List<Object>> latLong)
   {
      if (latLong != null)
      {
         for (final Entry<String, List<Object>> entry : latLong.entrySet())
         {
            extraFacilityContent.put(entry.getKey(), entry.getValue().get(0).toString());
         }
      }
   }

   /**
    * Gets the longitude latitude.
    *
    * @param source the source
    * @return the longitude latitude
    */
   private Map<String, List<Object>> getExcurisonLatitudeLongitude(final ExcursionModel source)
   {
      final Map<String, List<Object>> latLong = new ConcurrentHashMap<String, List<Object>>();
      for (final ExcursionPriceModel price : source.getExcursionPrices())
      {
         latLong.putAll(getLatitudeLongitude(price));
      }
      return latLong;
   }

   /**
    * Gets the latitude and longitude.
    *
    * @param excursionPrice the excursion price
    * @return the latitudelongitude map
    */
   private Map<String, List<Object>> getLatitudeLongitude(final ExcursionPriceModel excursionPrice)
   {
      if (excursionPrice.getLocation() != null)
      {
         return featureService.getValuesForFeatures(
            Arrays.asList(new String[] { "name", "latitude", "longitude" }),
            excursionPrice.getLocation(), new Date(), null);
      }
      return null;
   }

   /**
    * Gets the media data for excursion.
    *
    * @param excursion the excursion
    * @param viewData the view data
    * @return the media data for excursion
    */
   private ExtraFacilityContentViewData getMediaDataForExcursion(final ExcursionModel excursion,
      final ExtraFacilityContentViewData viewData)
   {
      if (CollectionUtils.isNotEmpty(excursion.getGalleryImages()))
      {
         for (final MediaContainerModel mediaContainerModel : excursion.getGalleryImages())
         {
            if (SyntacticSugar.isNotNull(mediaContainerModel))
            {
               addMediaData(viewData, mediaContainerModel);
            }
         }
      }
      return viewData;
   }

   /**
    * Populates the media data to the ExtraFacilityContentViewData.
    *
    * @param viewData the ExtraFacilityContentViewData
    * @param mediaContainerModel the media container model
    */
   private void addMediaData(final ExtraFacilityContentViewData viewData,
      final MediaContainerModel mediaContainerModel)
   {
      for (final MediaModel media : mediaContainerModel.getMedias())
      {
         viewData.getGalleryImages().add(getMediaData(media));
      }
   }

   /**
    * Populate media data.
    *
    * @param sourceModel the source model
    * @throws ConversionException the conversion exception
    */
   private MediaViewData getMediaData(final MediaModel sourceModel) throws ConversionException
   {
      final MediaViewData mediaData = new MediaViewData();
      Assert.notNull(sourceModel, "Converter source must not be null");
      mediaData.setCode(sourceModel.getCode());
      mediaData.setAltText(sourceModel.getAltText());
      mediaData.setDescription(sourceModel.getDescription());
      mediaData.setMime(sourceModel.getMime());
      mediaData.setMainSrc(sourceModel.getURL());
      mediaData.setSize(sourceModel.getMediaFormat().getName());
      return mediaData;
   }

}
