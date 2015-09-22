/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.tui.components.model.NavigationComponentModel;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;
import uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData;
import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;
import uk.co.tui.cr.components.model.CruiseHeaderNavigationComponentModel;
import uk.co.tui.web.common.enums.NavigationViewMode;

/**
 * @author EXTLP1
 *
 */
public class CRNavigationLinksPopulator implements
   Populator<NavigationComponent, GlobalNavigationComponentViewData>
{

   @Resource
   private CMSNavigationNodePopulator cmsNavigationNodePopulator;

   @Resource
   private CMSLinkComponentPopulator cmsLinkComponentPopulator;

   public static final String STYLE_CLASS = "active";

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final NavigationComponent source,
      final GlobalNavigationComponentViewData target)
   {
      final NavigationComponentViewData navigationViewData = new NavigationComponentViewData();

      final CruiseHeaderNavigationComponentModel componentModel =
         (CruiseHeaderNavigationComponentModel) source.getComponent();
      final NavigationComponentModel navComponent = componentModel.getNavigationComponent();
      final Collection<NavigationBarComponentModel> navigationBarComponents =
         navComponent.getNavigationBarComponents();
      populateNavigationBarData(navigationViewData, navigationBarComponents);
      navigationViewData.setNavigationViewMode(navComponent.getNavigationViewMode());
      navigationViewData.setComponentStyle(navComponent.getComponentStyle().getCode());
      for (final NavigationBarComponentViewData bar : navigationViewData
         .getNavigationBarComponents())
      {
         populateBarComponent(navigationViewData, bar);
      }

      if (componentModel.getComponentStyle() != null)
      {
         navigationViewData.setComponentStyle(navComponent.getComponentStyle().toString());
      }
      target.getCrGlobalNavigationViewData().setNavComViewData(navigationViewData);
   }

   /**
         *
         */
   private void populateNavigationBarData(final NavigationComponentViewData target,
      final Collection<NavigationBarComponentModel> navigationBarComponents)
   {
      if (CollectionUtils.isNotEmpty(navigationBarComponents))
      {
         getNavigationBarViewData(target, navigationBarComponents);
      }
   }

   /**
    *
    * @param target
    * @param navigationBarComponents
    */
   private void getNavigationBarViewData(final NavigationComponentViewData target,
      final Collection<NavigationBarComponentModel> navigationBarComponents)
   {
      final List<NavigationBarComponentViewData> navigationBarCompViewDataList =
         new ArrayList<NavigationBarComponentViewData>();

      for (final NavigationBarComponentModel navigationBarComponentModel : navigationBarComponents)
      {
         populateNavidationComponent(navigationBarCompViewDataList, navigationBarComponentModel);
      }
      target.setNavigationBarComponents(navigationBarCompViewDataList);
   }

   /**
         *
         */
   private void populateBarComponent(final NavigationComponentViewData target,
      final NavigationBarComponentViewData bar)
   {
      if (bar.getNavigationNode() != null && bar.getNavigationNode().getLinks() != null)
      {
         resolveCMSLinkURL(bar, target.getNavigationViewMode());

      }
   }

   /**
         *
         */
   private void populateNavidationComponent(
      final List<NavigationBarComponentViewData> navigationBarCompViewDataList,
      final NavigationBarComponentModel navigationBarComponentModel)
   {
      if (!navigationBarComponentModel.getUid().contains("IslandCruises"))
      {
         getViewData(navigationBarCompViewDataList, navigationBarComponentModel);
      }

   }

   /**
   *
   */
   private void getViewData(
      final List<NavigationBarComponentViewData> navigationBarCompViewDataList,
      final NavigationBarComponentModel sourceModel)
   {
      final NavigationBarComponentViewData targetData = new NavigationBarComponentViewData();

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      targetData.setStyleClass(sourceModel.getStyleClass());
      targetData.setWrapAfter(sourceModel.getWrapAfter());
      targetData.setDropDownLayout(sourceModel.getDropDownLayout());
      targetData.setName(sourceModel.getName());
      targetData.setUid(sourceModel.getUid());
      final CMSNavigationNodeModel navigationNodeModel = sourceModel.getNavigationNode();
      if (navigationNodeModel != null)
      {
         final CMSNavigationNodeViewData navigationNode = new CMSNavigationNodeViewData();
         cmsNavigationNodePopulator.populate(navigationNodeModel, navigationNode);
         targetData.setNavigationNode(navigationNode);
      }

      final CMSLinkComponentModel cmsLinkComponentModel = sourceModel.getLink();
      if (cmsLinkComponentModel != null)
      {
         final CMSLinkComponentViewData cmsLinkComponentViewData = new CMSLinkComponentViewData();
         cmsLinkComponentPopulator.populate(cmsLinkComponentModel, cmsLinkComponentViewData);
         targetData.setLink(cmsLinkComponentViewData);
      }
      navigationBarCompViewDataList.add(targetData);
   }

   /**
    *
    * @param bar
    * @param navigationViewMode
    */
   private void resolveCMSLinkURL(final NavigationBarComponentViewData bar,
      final NavigationViewMode navigationViewMode)
   {

      for (final CMSLinkComponentViewData link : bar.getNavigationNode().getLinks())
      {
         if (StringUtils.isNotBlank(link.getUrl())
            && checkForValid(navigationViewMode, NavigationViewMode.MEGAMENUNAVIGATION.getCode()))
         {
            link.setUrl(getCMSLinkUrl(link, navigationViewMode));
         }

      }
   }

   private String getCMSLinkUrl(final CMSLinkComponentViewData link,
      final NavigationViewMode navigationViewMode)
   {
      String resolvedURL = StringUtils.EMPTY;
      if (checkForValid(navigationViewMode, NavigationViewMode.MEGAMENUNAVIGATION.getCode()))
      {
         resolvedURL = link.getUrl();
      }
      return resolvedURL;
   }

   private boolean checkForValid(final NavigationViewMode navigationViewMode, final String code)
   {
      return navigationViewMode != null && navigationViewMode.getCode().equalsIgnoreCase(code);
   }

}
