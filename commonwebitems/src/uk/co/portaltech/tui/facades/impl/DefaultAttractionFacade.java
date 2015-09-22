/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.media.services.AttractionMediaService;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.converters.AttractionOption;
import uk.co.portaltech.tui.converters.ExcursionOption;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.populators.MediaDataPopulator;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.services.LinkedItemService;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ViewData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author l.furrer
 *
 */
public class DefaultAttractionFacade implements AttractionFacade
{
   private static final TUILogUtils LOG = new TUILogUtils("DefaultAttractionFacade");

   @Resource
   private AttractionService attractionService;

   @Resource
   private Converter<AttractionModel, AttractionViewData> attractionConverter;

   @Resource
   private Converter<ExcursionModel, ExcursionViewData> excursionConverter;

   private ConfigurablePopulator<AttractionModel, AttractionViewData, AttractionOption> attractionConfiguredPopulator;

   private ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> excursionConfiguredPopulator;

   @Resource
   private Converter<ItemModel, AttractionViewData> attractionEditorialContentConverter;

   private Populator<ItemModel, AttractionViewData> attractionEditorialContentPopulator;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private MediaDataPopulator mediaDataPopulator;

   @Resource
   private Populator<Collection<MediaContainerModel>, List<MediaViewData>> featureListImagesPopulator;

   private static final String NON_BOOKABLE_ATTRACTION_FD_CODE = "P00067";

   @Resource
   private LinkedItemService linkedItemService;

   @Resource
   private AttractionMediaService attractionMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   @Required
   public void setExcursionConfiguredPopulator(
      final ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> excursionConfiguredPopulator)
   {
      this.excursionConfiguredPopulator = excursionConfiguredPopulator;
   }

   @Required
   public void setAttractionConfiguredPopulator(
      final ConfigurablePopulator<AttractionModel, AttractionViewData, AttractionOption> attractionConfiguredPopulator)
   {
      this.attractionConfiguredPopulator = attractionConfiguredPopulator;
   }

   /**
    * Sets the Attraction Editorial Content Populator
    *
    * @param attractionEditorialContentPopulator the attractionEditorialContentPopulator to set
    */
   @Required
   public void setAttractionEditorialContentPopulator(
      final Populator<ItemModel, AttractionViewData> attractionEditorialContentPopulator)
   {
      this.attractionEditorialContentPopulator = attractionEditorialContentPopulator;
   }

   @Override
   public ViewData getAttractionUspsByCode(final String attractionCode)
   {
      final ItemModel itemModel =
         attractionService.getAttractionForCode(attractionCode,
            cmsSiteService.getCurrentCatalogVersion());
      if (itemModel != null)
      {
         if (itemModel instanceof ExcursionModel)
         {
            final ExcursionModel excursionModel = (ExcursionModel) itemModel;
            final ExcursionViewData excursionData = excursionConverter.convert(excursionModel);
            excursionConfiguredPopulator.populate(excursionModel, excursionData,
               Arrays.asList(ExcursionOption.USPS));
            return excursionData;
         }

         if (itemModel instanceof AttractionModel)
         {
            final AttractionModel attractionModel = (AttractionModel) itemModel;
            final AttractionViewData attractionData = attractionConverter.convert(attractionModel);
            attractionConfiguredPopulator.populate(attractionModel, attractionData,
               Arrays.asList(AttractionOption.USPS));
            return attractionData;
         }

      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AttractionFacade#getEditorialContent(java.lang.String)
    */
   @Override
   public AttractionViewData getEditorialContent(final String attractionCode)
   {
      AttractionViewData attractionViewData = new AttractionViewData();
      ItemModel itemModel = null;
      try
      {
         itemModel =
            attractionService.getAttractionForCode(attractionCode,
               cmsSiteService.getCurrentCatalogVersion());
         if (itemModel != null)
         {
            attractionViewData = attractionEditorialContentConverter.convert(itemModel);
            attractionEditorialContentPopulator.populate(itemModel, attractionViewData);
         }
      }
      catch (final ModelNotFoundException exception)
      {
         LOG.warn("Could not find attraction with code = " + attractionCode, exception);
      }
      return attractionViewData;
   }

   @Override
   public AttractionViewData getAttractionData(final String attractionCode)
   {
      AttractionViewData attractionViewData = new AttractionViewData();
      ItemModel itemModel = null;
      try
      {
         itemModel =
            attractionService.getAttractionForCode(attractionCode,
               cmsSiteService.getCurrentCatalogVersion());
         if (itemModel instanceof AttractionModel)
         {
            final AttractionModel attractionModel = (AttractionModel) itemModel;
            attractionViewData = attractionEditorialContentConverter.convert(attractionModel);
            attractionEditorialContentPopulator.populate(attractionModel, attractionViewData);
            if (StringUtils.isEmpty(attractionViewData.getEditorialContent()))
            {
               return null;
            }
            final MediaModel thumbnail = attractionModel.getThumbnail();
            if (thumbnail != null)
            {
               final MediaViewData mediaData = new MediaViewData();
               mediaDataPopulator.populate(thumbnail, mediaData);
               attractionViewData.setThumbnail(mediaData);

               final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
               featureListImagesPopulator.populate(attractionModel.getGalleryImages(),
                  imageDataList);
               attractionViewData.setGalleryImages(imageDataList);
            }

         }
      }
      catch (final ModelNotFoundException exception)
      {
         LOG.warn("Could not find attraction with code = " + attractionCode, exception);
      }
      return attractionViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AttractionFacade#getAttractionEnrichedData(java.util.List)
    */
   @Override
   public List<AttractionViewData> getAttractionEnrichedData(final List<ResultData> resultData)
   {

      final List<AttractionViewData> list = new ArrayList<AttractionViewData>();
      for (final ResultData result : resultData)
      {

         final ItemModel itemModel =
            attractionService.getAttractionForCode(result.getCode(),
               cmsSiteService.getCurrentCatalogVersion());

         try
         {
            AttractionViewData attractionViewData = null;
            if (itemModel != null)
            {
               final AttractionModel attractionModel = (AttractionModel) itemModel;
               attractionViewData = attractionEditorialContentConverter.convert(attractionModel);
               attractionEditorialContentPopulator.populate(attractionModel, attractionViewData);
               attractionViewData.setType(result.getSearchResultSubtype());
               attractionViewData.setCode(result.getCode());
               attractionViewData.setName(result.getName());
               attractionViewData.setLatitude(result.getLatitude());
               attractionViewData.setLongitude(result.getLongitude());
               final List<MediaViewData> mediaList = new ArrayList<MediaViewData>();
               mediaPopulatorLite.populate(attractionMediaService.getImageMedias(attractionModel),
                  mediaList);
               attractionViewData.getGalleryImages().addAll(mediaList);
               list.add(attractionViewData);
            }
            else
            {
               LOG.warn("Could not find attraction for r code = " + result.getCode());
            }
         }
         catch (final NullPointerException e)
         {

            LOG.error("could not find the attraction", e);
         }

      }
      return list;
   }

   @Override
   public ItineraryViewData getNonBookableAttractions(final LocationModel locationModel)
   {
      return linkedItemService.getItineraryDataForLocation(locationModel,
         NON_BOOKABLE_ATTRACTION_FD_CODE);
   }
}
