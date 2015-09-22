/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.resolvers.CruiseProductUrlResolver;
import uk.co.portaltech.tui.services.pojo.CruiseNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.utils.GlobalHeaderCommonUtil;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.PortsOfCallViewData;
import uk.co.portaltech.tui.web.view.data.TopPortOfCallViewData;

/**
 * @author EXTLP1
 * 
 */
public class CRPortOfCallNavigationPopulator implements
   Populator<NavigationComponent, GlobalNavigationComponentViewData>
{

   @Resource
   private CruiseProductUrlResolver cruiseProductUrlResolver;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final NavigationComponent source,
      final GlobalNavigationComponentViewData target) throws ConversionException
   {
      final CruiseNavigation cruiseNavigation = (CruiseNavigation) source.getIntraSiteNavigation();

      final List<LocationModel> locationModels = cruiseNavigation.getLocationModels();
      final Map<String, String> megaMenuConfigValues = cruiseNavigation.getMegaMenuConfigValues();
      final List<PortsOfCallViewData> pocViewDatas = new ArrayList<PortsOfCallViewData>();
      final TopPortOfCallViewData topPortOfCallViewData = new TopPortOfCallViewData();
      for (final LocationModel locationModel : locationModels)
      {
         final PortsOfCallViewData pocViewData = new PortsOfCallViewData();
         pocViewData.setName(locationModel.getName());
         cruiseProductUrlResolver.setOverrideSubPageType(GlobalNavigationConstant.OVERVIEW);
         pocViewData.setUrl(cruiseProductUrlResolver.resolve(locationModel.getCode(),
            SearchResultType.LOCATION));
         pocViewData.setCountryName(GlobalHeaderCommonUtil.getParentCountryName(locationModel));
         pocViewDatas.add(pocViewData);
      }
      topPortOfCallViewData.setPocViewDatas(pocViewDatas);
      topPortOfCallViewData.setLinktitle(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_POC_LINK_TITLE));
      topPortOfCallViewData.setTitle(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_POC_TITLE));
      topPortOfCallViewData.setLinkUrl(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_POC_LINK_URL));
      target.getCrGlobalNavigationViewData().setTopPortOfCallViewData(topPortOfCallViewData);
   }
}
