/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.apache.commons.lang.StringUtils.join;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.resolvers.CruiseProductUrlResolver;
import uk.co.portaltech.tui.services.pojo.CruiseNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.web.view.data.CRdestinationViewData;
import uk.co.portaltech.tui.web.view.data.CruiseDestinationViewData;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.tui.cruise.mainstream.data.CruiseAreaData;
import uk.co.tui.cruise.mainstream.data.LocationData;

/**
 * @author EXTLP1
 * 
 */
public class CRdestinationNavigationPopulator implements
   Populator<NavigationComponent, GlobalNavigationComponentViewData>
{

   @Resource
   private CruiseProductUrlResolver cruiseProductUrlResolver;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.cr.populator.component.AbstractComplexPopulator#populate(java.lang.Object,
    * java.lang.Object[])
    */
   @Override
   public void populate(final NavigationComponent source,
      final GlobalNavigationComponentViewData target) throws ConversionException
   {
      final CruiseNavigation cruiseNavigation = (CruiseNavigation) source.getIntraSiteNavigation();
      final Map<String, String> megaMenuConfigValues = cruiseNavigation.getMegaMenuConfigValues();
      final List<CRdestinationViewData> destinationViewDatas =
         new ArrayList<CRdestinationViewData>();
      for (final Entry<CruiseAreaData, Set<LocationData>> entry : cruiseNavigation
         .getCruiseAreaMap().entrySet())
      {
         getDestinationViewData(destinationViewDatas, entry);
      }
      final CruiseDestinationViewData cruiseDestinationViewData = new CruiseDestinationViewData();
      cruiseDestinationViewData.setDestViewDatas(destinationViewDatas);
      cruiseDestinationViewData.setTitle(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_DEST_TITLE));
      cruiseDestinationViewData.setLinktitle(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_DEST_LINK_TITLE));
      cruiseDestinationViewData.setLinkUrl(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_DEST_LINK_URL));
      cruiseDestinationViewData.setNileCruise(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_DEST_NILE_CRUISE_TITLE));
      cruiseDestinationViewData.setNileCruiseUrl(megaMenuConfigValues
         .get(GlobalNavigationConstant.MEGA_MENU_DEST_NILE_CRUISE_URL));
      target.getCrGlobalNavigationViewData().setCruiseDestViewData(cruiseDestinationViewData);

   }

   /**
    * @param destinationViewDatas
    * @param entry
    * 
    */
   private void getDestinationViewData(final List<CRdestinationViewData> destinationViewDatas,
      final Entry<CruiseAreaData, Set<LocationData>> entry)
   {
      final List<String> countryNames = new ArrayList<String>();
      final CRdestinationViewData destinationViewData = new CRdestinationViewData();
      final CruiseAreaData cruiseAreaData = entry.getKey();
      for (final LocationData locationData : entry.getValue())
      {
         if (!countryNames.contains(locationData.getName()))
         {
            final String contryName;
            contryName = locationData.getName();
            countryNames.add(contryName);
         }
      }
      destinationViewData.setCruiseAreaCode(cruiseAreaData.getCode());
      destinationViewData.setCruiseAreaName(cruiseAreaData.getName());
      destinationViewData.setUrl(cruiseProductUrlResolver.resolve(cruiseAreaData.getCode(),
         SearchResultType.CRUISEAREA));
      destinationViewData.setCountryNames(join(countryNames,
         GlobalNavigationConstant.COMMA_SEPERATOR));
      destinationViewDatas.add(destinationViewData);
   }

}
