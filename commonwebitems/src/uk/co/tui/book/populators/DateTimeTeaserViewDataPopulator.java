/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import uk.co.portaltech.commons.DateUtils;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.view.data.teasers.DepartureDateTeaserViewData;


/**
 * @author samantha.gd
 *
 */
public class DateTimeTeaserViewDataPopulator implements Populator<BasePackage, DepartureDateTeaserViewData>
{

    /** Date offset */
    private static final int DATE_OFFSET = 5;

    /** The package component service. */
    @Resource
    private PackageComponentService packageComponentService;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage source, final DepartureDateTeaserViewData target)
    {
        final Date dateTime = (packageComponentService.getFlightItinerary(source)).getOutBound().get(0).getSchedule()
                .getDepartureDate();
        final String formattedDate = DateUtils.customFormatFlightDate(dateTime);
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        target.setDate(formatter.format(dateTime));

        target.setDisplayDate(formattedDate.substring(0, formattedDate.length() - DATE_OFFSET));
        target.setPackageId(source.getId());
        target.setPerPersonPrice(source.getPrice().getRate().getAmount());
        target.setTotalPrice(source.getPrice().getAmount().getAmount());
    }

}
