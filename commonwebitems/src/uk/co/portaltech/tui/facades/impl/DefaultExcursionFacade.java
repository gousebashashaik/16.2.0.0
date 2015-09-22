/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static uk.co.portaltech.travel.enums.ExcursionType.SHORE;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.media.services.ProductMediaService;
import uk.co.portaltech.travel.media.services.domain.Media;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.ExcursionPriceService;
import uk.co.portaltech.travel.services.ExcursionService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.converters.AttractionConverter;
import uk.co.portaltech.tui.converters.AttractionOption;
import uk.co.portaltech.tui.converters.ExcursionOption;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author l.furrer
 *
 */
public class DefaultExcursionFacade implements ExcursionFacade
{

   private static final TUILogUtils LOG = new TUILogUtils("DefaultExcursionFacade");

   private static final int TWO = 2;

   @Resource
   private ExcursionService excursionService;

   @Resource
   private AttractionService attractionService;

   @Resource
   private Converter<ExcursionModel, ExcursionViewData> excursionConverter;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private AttractionConverter attractionConverter;

   @Resource
   private ExcursionPriceService excursionPriceService;

   @Resource
   private ProductMediaService productMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   private ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> excursionConfiguredPopulator;

   private ConfigurablePopulator<AttractionModel, AttractionViewData, AttractionOption> attractionConfiguredPopulator;

   @Required
   public void setAttractionConfiguredPopulator(
      final ConfigurablePopulator<AttractionModel, AttractionViewData, AttractionOption> attractionConfiguredPopulator)
   {
      this.attractionConfiguredPopulator = attractionConfiguredPopulator;
   }

   @Required
   public void setExcursionConfiguredPopulator(
      final ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> excursionConfiguredPopulator)
   {
      this.excursionConfiguredPopulator = excursionConfiguredPopulator;
   }

   @Override
   public ExcursionViewData getExcursionUspsByCode(final String excursionCode)
   {
      final ExcursionModel excursionModel =
         excursionService.getExcursionForCode(excursionCode,
            cmsSiteService.getCurrentCatalogVersion());
      if (excursionModel != null)
      {
         final ExcursionViewData excursionData = excursionConverter.convert(excursionModel);
         excursionConfiguredPopulator.populate(excursionModel, excursionData,
            Arrays.asList(ExcursionOption.USPS));
         return excursionData;
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AccomodationFacade#getRestrictionInfo(java.lang.String)
    */
   @Override
   public ExcursionViewData getRestrictionInfo(final String excursionCode)
   {
      final ExcursionModel excursionModel =
         excursionService.getExcursionForCode(excursionCode,
            cmsSiteService.getCurrentCatalogVersion());
      if (excursionModel != null)
      {
         final ExcursionViewData excursionFeatureValue = new ExcursionViewData();
         final ExcursionViewData excursionData = excursionConverter.convert(excursionModel);
         excursionConfiguredPopulator.populate(excursionModel, excursionData,
            Arrays.asList(ExcursionOption.RESTRICTION));
         excursionFeatureValue.putFeatureCodesAndValues(excursionData.getFeatureCodesAndValues());
         return excursionFeatureValue;
      }
      else
      {
         return null;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ExcursionFacade#getExcursionsWithEndecaData(java.util.List)
    */
   @Override
   public List<ExcursionViewData> getExcursionsWithEndecaData(final List<ResultData> list)
   {
      final List<ExcursionViewData> excursions = new ArrayList<ExcursionViewData>();
      if (list != null && !list.isEmpty())
      {
         for (final ResultData resultData : list)
         {
            final String codeFromEndeca = resultData.getCode();
            if (codeFromEndeca != null)
            {
               ExcursionModel excursionModel = null;
               ExcursionViewData excursionViewData = null;
               try
               {
                  excursionModel =
                     excursionService.getExcursionForCode(codeFromEndeca,
                        cmsSiteService.getCurrentCatalogVersion());
               }
               catch (final ModelNotFoundException ex)
               {
                  LOG.warn("Excusrion Model with code : " + codeFromEndeca + " not found", ex);
               }
               if (isValidExcursions(excursionModel))
               {
                  excursionViewData = excursionConverter.convert(excursionModel);
                  excursionConfiguredPopulator.populate(excursionModel, excursionViewData,
                     Arrays.asList(ExcursionOption.BASIC, ExcursionOption.PRIMARY_IMAGE));

                  // mediapoulator
                  // change the excursionviewdata
                  excursionViewData = getMediaDataForExcursion(excursionModel, excursionViewData);
                  // enrich the endeca data
                  excursionViewData.setFromPrice(resultData.getPriceFrom());
                  excursions.add(excursionViewData);
               }
            }
         }
      }
      return excursions;
   }

   private boolean isValidExcursions(final ExcursionModel excursionModel)
   {
      return (excursionModel != null)
         && !(equalsIgnoreCase(excursionModel.getExcursionType().toString(), SHORE.toString()));
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.ExcursionFacade#getLowsetPriceExcursionForLocation(uk.co.portaltech
    * .travel.model .LocationModel)
    */
   @Override
   public PriceAndAvailabilityWrapper getLowsetPriceExcursionForLocation(
      final LocationModel location, final ItemModel item)
   {
      final PriceAndAvailabilityWrapper priceAndAvailabilityWrapper =
         new PriceAndAvailabilityWrapper();

      BigDecimal lowestPrice = null;
      BigDecimal lowestChildPrice = null;
      if (item instanceof ExcursionModel)
      {
         final ExcursionModel excursionModel = (ExcursionModel) item;
         final Set<ExcursionPriceModel> prices = excursionModel.getExcursionPrices();

         for (final ExcursionPriceModel price : prices)
         {
            if (lowestPrice == null)
            {
               lowestPrice = price.getAdultPrice();
            }
            else if (price.getAdultPrice().compareTo(lowestPrice) < 0)
            {
               lowestPrice = price.getAdultPrice();
            }

            if (lowestChildPrice == null)
            {
               lowestChildPrice = price.getChildPrice();
            }
            else if (price.getChildPrice().compareTo(lowestChildPrice) < 0)
            {
               lowestChildPrice = price.getChildPrice();
            }
         }
         final ExcursionViewData excursionViewData = excursionConverter.convert(excursionModel);
         excursionConfiguredPopulator.populate(excursionModel, excursionViewData,
            Arrays.asList(ExcursionOption.BASIC, ExcursionOption.PRIMARY_IMAGE));
         if (lowestPrice != null)
         {
            excursionViewData.setFromPrice(lowestPrice.setScale(TWO, RoundingMode.UNNECESSARY)
               .toString());

         }
         if (lowestChildPrice != null)
         {
            excursionViewData.setChildPrice(lowestChildPrice
               .setScale(TWO, RoundingMode.UNNECESSARY).toString());
         }

         // start excursion currency conversion
         final ExcursionPriceModel excursionPriceModel =
            excursionPriceService.getExcursionPrice(
               ((ExcursionPriceModel) CollectionUtils.get(prices, 0)).getCode(),
               cmsSiteService.getCurrentCatalogVersion());
         excursionConfiguredPopulator.populate(excursionPriceModel.getExcursion(),
            excursionViewData, Arrays.asList(ExcursionOption.EXCURSIONPRICE));
         // end excursion currency conversion

         priceAndAvailabilityWrapper.setExcursion(excursionViewData);

      }
      else if (item instanceof AttractionModel)
      {

         final AttractionModel attractionModel = (AttractionModel) item;
         final AttractionViewData attractionViewData = new AttractionViewData();
         attractionConfiguredPopulator.populate(attractionModel, attractionViewData,
            Arrays.asList(AttractionOption.BASIC));
         priceAndAvailabilityWrapper.setAttraction(attractionViewData);

      }
      return priceAndAvailabilityWrapper;

   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.ExcursionFacade#getExcursionNameComponentData(java.lang.String)
    */
   @Override
   public ViewData getExcursionNameComponentData(final String code)
   {
      final ItemModel itemModel =
         attractionService.getAttractionForCode(code, cmsSiteService.getCurrentCatalogVersion());
      if (itemModel != null)
      {
         if (itemModel instanceof ExcursionModel)
         {
            final ExcursionModel excursionModel = (ExcursionModel) itemModel;
            final ExcursionViewData excursionData = excursionConverter.convert(excursionModel);
            excursionConfiguredPopulator.populate(excursionModel, excursionData,
               Arrays.asList(ExcursionOption.BASIC));
            return excursionData;
         }

         if (itemModel instanceof AttractionModel)
         {
            final AttractionModel attractionModel = (AttractionModel) itemModel;
            final AttractionViewData attractionData = attractionConverter.convert(attractionModel);
            attractionConfiguredPopulator.populate(attractionModel, attractionData,
               Arrays.asList(AttractionOption.BASIC));
            return attractionData;
         }

      }
      return null;
   }

   /**
    * @param excursion
    * @param viewData
    * @return ExcursionViewData This method returns the media data for excursions.
    */
   private ExcursionViewData getMediaDataForExcursion(final ExcursionModel excursion,
      final ExcursionViewData viewData)
   {
      final ExcursionModel excursionModel = excursion;
      final ExcursionViewData excursionViewData = viewData;

      if (excursionModel.getGalleryImages() != null)
      {
         final List<Media> mediaLists = productMediaService.getImageMedias(excursionModel);
         final List<MediaViewData> mediaViewDatas = new ArrayList<MediaViewData>();
         mediaPopulatorLite.populate(mediaLists, mediaViewDatas);
         excursionViewData.getGalleryImages().addAll(mediaViewDatas);
      }
      return excursionViewData;

   }
}
