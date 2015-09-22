/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.tui.components.model.NavigationComponentModel;
import uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData;
import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;


/**
 * @author gagan
 *
 */
public class NavigationComponentPopulator implements Populator<NavigationComponentModel, NavigationComponentViewData>
{



    private Populator<NavigationBarComponentModel, NavigationBarComponentViewData> navigationBarComponentPopulator;

    @Resource
    private Converter<NavigationBarComponentModel, NavigationBarComponentViewData> navigationBarComponentConverter;


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final NavigationComponentModel source, final NavigationComponentViewData target)
            throws ConversionException
    {
        final Collection<NavigationBarComponentModel> navigationBarComponents = source.getNavigationBarComponents();
        if (CollectionUtils.isNotEmpty(navigationBarComponents))
        {
            final List<NavigationBarComponentViewData> navigationBarCompViewDataList = new ArrayList<NavigationBarComponentViewData>();
            for (final NavigationBarComponentModel navigationBarComponentModel : navigationBarComponents)
            {
                final NavigationBarComponentViewData navigationBarComponentViewData = navigationBarComponentConverter
                        .convert(navigationBarComponentModel);
                navigationBarComponentPopulator.populate(navigationBarComponentModel, navigationBarComponentViewData);
                navigationBarCompViewDataList.add(navigationBarComponentViewData);
            }
            target.setNavigationBarComponents(navigationBarCompViewDataList);
        }
        target.setNavigationViewMode(source.getNavigationViewMode());

    }

    /**
     * @return the navigationBarComponentPopulator
     */
    public Populator<NavigationBarComponentModel, NavigationBarComponentViewData> getNavigationBarComponentPopulator()
    {
        return navigationBarComponentPopulator;
    }


    /**
     * @param navigationBarComponentPopulator
     *           the navigationBarComponentPopulator to set
     */
    public void setNavigationBarComponentPopulator(
            final Populator<NavigationBarComponentModel, NavigationBarComponentViewData> navigationBarComponentPopulator)
    {
        this.navigationBarComponentPopulator = navigationBarComponentPopulator;
    }


}
