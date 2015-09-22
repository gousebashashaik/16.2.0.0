/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.tui.cr.resolvers.impl.CRProductUrlResolver;

/**
 * @author omonikhide
 *
 */

public class TuiCategoryUrlResolver extends TUIUrlResolver<CategoryModel>
{

   @Resource
   private CrdToUrlMappingFacade crdToUrlMapFacade;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private TypeService typeService;

   @Resource
   private CRProductUrlResolver crProductUrlResolver;

   @Override
   public String resolve(final CategoryModel source)
   {
      if (source == null)
      {
         return null;
      }
      // Work out values
      final String categoryCode = source.getCode();
      String url = "";
      final String siteBrand = tuiUtilityService.getSiteBrand();
      if ((BrandType.TH.getCode().equals(siteBrand) || BrandType.FJ.getCode().equals(siteBrand))
         && source instanceof LocationModel)
      {
         url =
            crdToUrlMapFacade.getUrlForCRD(
               categoryCode,
               typeService
                  .getEnumerationValue("SearchResultType", SearchResultType.LOCATION.getCode())
                  .getPk().toString(),
               typeService.getEnumerationValue("BrandType", BrandType.valueOf(siteBrand).getCode())
                  .getPk().toString());
         if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(overrideSubPageType))
         {
            url = getTabUrl(url, overrideSubPageType);
         }
         overrideSubPageType = null;
         return url;
      }
      else if ((BrandType.CR.getCode().equals(siteBrand)) && source instanceof LocationModel)
      {
         return getCRCategoryUrl(source);
      }
      else
      {
         return getFCCategoryUrl(source);
      }
   }

   private String getCRCategoryUrl(final CategoryModel source)
   {
      String url = "";
      if (((LocationModel) source).getIsPOC().booleanValue())
      {

         url = crProductUrlResolver.resolve(source.getCode(), SearchResultType.CRUISEANDSTAY);
      }
      else
      {
         url = crProductUrlResolver.resolve(source.getCode(), SearchResultType.LOCATION);

      }
      return url;
   }
}
