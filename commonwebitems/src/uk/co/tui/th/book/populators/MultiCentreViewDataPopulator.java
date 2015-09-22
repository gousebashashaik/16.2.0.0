/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.util.List;

import javax.annotation.Resource;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.services.data.MultiCentreData;
import uk.co.tui.services.item.LinkedItemService;
import uk.co.tui.th.book.view.data.MultiCentreViewData;
import uk.co.tui.th.book.view.data.PackageViewData;


/**
 * The Class MultiCentreViewDataPopulator.
 *
 * @author samantha.gd
 */
public class MultiCentreViewDataPopulator implements Populator<BasePackage, PackageViewData>
{

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object, java.lang.Object)
     */
    /** The linked item service. */
    @Resource(name = "mainStreamTrvelLinkedItemService")
    private LinkedItemService linkedItemService;

    /** The package component service. */
    @Resource
    private PackageComponentService packageComponentService;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage source, final PackageViewData target)
    {
        if (source.getListOfHighlights().contains(HighLights.MULTI_CENTRE))
        {
            populateMultiCentreData(linkedItemService.getMultiCentreData(packageComponentService.getStay(source).getCode()), target
                    .getAccomViewData().get(0).getMultiCentreData());
        }
    }

    /**
     * Populate multi centre data.
     *
     * @param source
     *           the source
     * @param target
     *           the target
     */
    private void populateMultiCentreData(final List<MultiCentreData> source, final List<MultiCentreViewData> target)
    {
        for (final MultiCentreData eachData : source)
        {
            final MultiCentreViewData viewData = new MultiCentreViewData();
            viewData.setDuration(eachData.getDuration());
            viewData.setImageUrl(eachData.getImageUrl());
            viewData.setLocation((String) eachData.getLocations().values().iterator().next());
            viewData.setName(eachData.getName());
            target.add(viewData);
        }

    }

}
