/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductPromoViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author narendra.bm
 *
 */
public class ProductPromoPopulator implements Populator<ProductRangeModel, ProductPromoViewData>
{

   @Resource
   private Converter<MediaModel, MediaViewData> mediaModelConverter;

   @Resource
   private CrdToUrlMappingFacade crdToUrlMapFacade;

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private TypeService typeService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final ProductRangeModel source, final ProductPromoViewData target)
      throws ConversionException
   {
      target.setProductName(source.getName());
      target.setProductDescription(source.getDescription());

      if (StringUtils.equalsIgnoreCase("FJ", tuiUtilityService.getSiteBrand()))
      {
         // Fetching the product range URL from CRDToURLMap
         final String brand = brandUtils.getFeatureServiceBrand(source.getBrands());
         final String url =
            crdToUrlMapFacade.getUrlForCRD(
               source.getCode(),
               typeService
                  .getEnumerationValue("SearchResultType", SearchResultType.PRODUCTRANGE.getCode())
                  .getPk().toString(), typeService.getEnumerationValue("BrandType", brand).getPk()
                  .toString());

         target.setProductURL(url);
      }
      else
      {
         target.setProductURL(source.getDifferentiatorURL());
      }

      target.setProductURLForMobile(source.getDifferentiatorURLForMobile());

      final MediaModel media = source.getPicture();
      final MediaViewData mediaViewData = mediaModelConverter.convert(media);
      if (media != null)
      {
         mediaViewDataPopulator.populate(media, mediaViewData);
      }
      target.setImage(mediaViewData);
      if (source.getCode() != null)
      {
         target.setCode(source.getCode().toLowerCase());
      }

   }

   /**
    * @return the mediaViewDataPopulator
    */
   public Populator<MediaModel, MediaViewData> getMediaViewDataPopulator()
   {
      return mediaViewDataPopulator;
   }

   /**
    * @param mediaViewDataPopulator the mediaViewDataPopulator to set
    */
   public void setMediaViewDataPopulator(
      final Populator<MediaModel, MediaViewData> mediaViewDataPopulator)
   {
      this.mediaViewDataPopulator = mediaViewDataPopulator;
   }
}
