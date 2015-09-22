/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;
import uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData;

/**
 * @author gagan
 *
 */
public class CMSNavigationNodePopulator implements
   Populator<CMSNavigationNodeModel, CMSNavigationNodeViewData>
{

   @Resource
   private Converter<CMSNavigationNodeModel, CMSNavigationNodeViewData> cmsNavigationNodeConverter;

   @Resource
   private Converter<CMSLinkComponentModel, CMSLinkComponentViewData> cmsLinkComponentConverter;

   private Populator<CMSLinkComponentModel, CMSLinkComponentViewData> cmsLinkComponentPopulator;

   // @Autowired

   // @Autowired

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final CMSNavigationNodeModel sourceModel,
      final CMSNavigationNodeViewData targetData) throws ConversionException
   {
      targetData.setVisible(sourceModel.isVisible());

      final String languageCode = "en";
      Locale locale = null;
      if (StringUtils.isNotBlank(languageCode))
      {
         locale = new Locale(languageCode);
      }
      targetData.setTitle(sourceModel.getTitle(locale));

      final List<CMSLinkComponentModel> cmsLinkcomponentModelList = sourceModel.getLinks();
      if (cmsLinkcomponentModelList != null && !cmsLinkcomponentModelList.isEmpty())
      {
         final List<CMSLinkComponentViewData> cmsLinkcomponentViewDataList =
            new ArrayList<CMSLinkComponentViewData>();
         for (final CMSLinkComponentModel cmsLinkComponentModel : cmsLinkcomponentModelList)
         {
            final CMSLinkComponentViewData cmsLinkComponentViewData =
               cmsLinkComponentConverter.convert(cmsLinkComponentModel);
            cmsLinkComponentPopulator.populate(cmsLinkComponentModel, cmsLinkComponentViewData);
            cmsLinkcomponentViewDataList.add(cmsLinkComponentViewData);
         }
         targetData.setLinks(cmsLinkcomponentViewDataList);
      }

      final List<CMSNavigationNodeModel> childNavigationNodes = sourceModel.getChildren();
      if (childNavigationNodes != null && !childNavigationNodes.isEmpty())
      {
         final List<CMSNavigationNodeViewData> childNavigationNodeViewDataList =
            new ArrayList<CMSNavigationNodeViewData>();
         for (final CMSNavigationNodeModel cmsNavigationNodeModelData : childNavigationNodes)
         {
            final CMSNavigationNodeViewData cmsNavigationNodeViewData =
               cmsNavigationNodeConverter.convert(cmsNavigationNodeModelData);
            populate(cmsNavigationNodeModelData, cmsNavigationNodeViewData);
            childNavigationNodeViewDataList.add(cmsNavigationNodeViewData);
         }
         targetData.setChildren(childNavigationNodeViewDataList);
      }

      populateParentNodeViewData(sourceModel, targetData, locale);
   }

   /**
    * @param sourceModel
    * @param targetData
    * @param locale
    */
   private void populateParentNodeViewData(final CMSNavigationNodeModel sourceModel,
      final CMSNavigationNodeViewData targetData, final Locale locale)
   {
      final CMSNavigationNodeModel parentNavigationNodeModel = sourceModel.getParent();
      if (parentNavigationNodeModel != null)
      {
         final CMSNavigationNodeViewData parentNavigationViewData =
            cmsNavigationNodeConverter.convert(parentNavigationNodeModel);
         parentNavigationViewData.setVisible(parentNavigationNodeModel.isVisible());
         parentNavigationViewData.setTitle(parentNavigationNodeModel.getTitle(locale));
         targetData.setParent(parentNavigationViewData);
      }

      final List<CMSNavigationNodeModel> childNavigationNodeModelList = sourceModel.getChildren();
      if (CollectionUtils.isNotEmpty(childNavigationNodeModelList))
      {
         final List<CMSNavigationNodeViewData> childNavigationNodeViewDataList =
            targetData.getChildren();
         for (int i = 0; i < childNavigationNodeModelList.size(); i++)
         {
            populateParentNodeViewData(childNavigationNodeModelList.get(i),
               childNavigationNodeViewDataList.get(i), locale);
         }
      }

   }

   /**
    * @return the cmsLinkComponentPopulator
    */
   public Populator<CMSLinkComponentModel, CMSLinkComponentViewData> getCmsLinkComponentPopulator()
   {
      return cmsLinkComponentPopulator;
   }

   /**
    * @param cmsLinkComponentPopulator the cmsLinkComponentPopulator to set
    */
   public void setCmsLinkComponentPopulator(
      final Populator<CMSLinkComponentModel, CMSLinkComponentViewData> cmsLinkComponentPopulator)
   {
      this.cmsLinkComponentPopulator = cmsLinkComponentPopulator;
   }

}
