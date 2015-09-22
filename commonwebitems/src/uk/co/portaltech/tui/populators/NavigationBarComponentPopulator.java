/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;
import uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData;
import uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData;

/**
 * @author gagan
 *
 */
public class NavigationBarComponentPopulator implements Populator<NavigationBarComponentModel, NavigationBarComponentViewData>{


    //@Autowired



    @Resource
    private Converter<CMSNavigationNodeModel, CMSNavigationNodeViewData>                 cmsNavigationNodeConverter;

    private Populator<CMSNavigationNodeModel, CMSNavigationNodeViewData>                 cmsNavigationNodePopulator;

    @Resource
    private Converter<CMSLinkComponentModel, CMSLinkComponentViewData>                   cmsLinkComponentConverter;

    private Populator<CMSLinkComponentModel, CMSLinkComponentViewData>                   cmsLinkComponentPopulator;


    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(NavigationBarComponentModel sourceModel, NavigationBarComponentViewData targetData) throws ConversionException {

        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        targetData.setStyleClass(sourceModel.getStyleClass());
        targetData.setWrapAfter(sourceModel.getWrapAfter());
        targetData.setDropDownLayout(sourceModel.getDropDownLayout());
        targetData.setName(sourceModel.getName());
        CMSNavigationNodeModel navigationNodeModel = sourceModel.getNavigationNode();
        if(navigationNodeModel != null) {
            CMSNavigationNodeViewData navigationNode = cmsNavigationNodeConverter.convert(navigationNodeModel);
            cmsNavigationNodePopulator.populate(navigationNodeModel, navigationNode);
            targetData.setNavigationNode(navigationNode);
        }

        CMSLinkComponentModel cmsLinkComponentModel = sourceModel.getLink();
        if(cmsLinkComponentModel != null) {
            CMSLinkComponentViewData cmsLinkComponentViewData = cmsLinkComponentConverter.convert(cmsLinkComponentModel);
            cmsLinkComponentPopulator.populate(cmsLinkComponentModel, cmsLinkComponentViewData);
            targetData.setLink(cmsLinkComponentViewData);
        }

    }

    /**
     * @return the cmsNavigationNodePopulator
     */
    public Populator<CMSNavigationNodeModel, CMSNavigationNodeViewData> getCmsNavigationNodePopulator() {
        return cmsNavigationNodePopulator;
    }

    /**
     * @param cmsNavigationNodePopulator the cmsNavigationNodePopulator to set
     */
    public void setCmsNavigationNodePopulator(Populator<CMSNavigationNodeModel, CMSNavigationNodeViewData> cmsNavigationNodePopulator) {
        this.cmsNavigationNodePopulator = cmsNavigationNodePopulator;
    }

    /**
     * @return the cmsLinkComponentPopulator
     */
    public Populator<CMSLinkComponentModel, CMSLinkComponentViewData> getCmsLinkComponentPopulator() {
        return cmsLinkComponentPopulator;
    }

    /**
     * @param cmsLinkComponentPopulator the cmsLinkComponentPopulator to set
     */
    public void setCmsLinkComponentPopulator(Populator<CMSLinkComponentModel, CMSLinkComponentViewData> cmsLinkComponentPopulator) {
        this.cmsLinkComponentPopulator = cmsLinkComponentPopulator;
    }

}
