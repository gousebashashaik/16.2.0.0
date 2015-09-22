/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.fj.book.view.data.PackageViewData;


/**
 * The Class LaplandViewDataPopulator.
 *
 * @author samantha.gd
 */
public class LaplandViewDataPopulator implements Populator<BasePackage, PackageViewData>
{

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage source, final PackageViewData target) throws ConversionException
    {
        final List<HighLights> listOfHighlight = source.getListOfHighlights();
        if (CollectionUtils.isNotEmpty(listOfHighlight) && listOfHighlight.contains(HighLights.LAPLAND_DAYTRIP))
        {
            target.setLaplandDayTrip(true);
        }

    }

}
