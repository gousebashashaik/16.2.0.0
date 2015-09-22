/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.criteria.FlightFilterCriteria;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.services.PackageComponentService;


/**
 * The Class FlightFilterCriteriaPopulator.
 *
 * @author samantha.gd
 */
public class FlightFilterCriteriaPopulator implements Populator<BasePackage, FlightFilterCriteria>
{

    /** The package component service. */
    @Resource
    private PackageComponentService packageComponentService;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage packageModel, final FlightFilterCriteria filterCriteria) throws ConversionException
    {
        final Leg legModel = packageComponentService.getFlightItinerary(packageModel).getOutBound().get(0);
        filterCriteria.setSelectedDate(legModel.getSchedule().getDepartureDate());
        filterCriteria.setSelectedDepartureAirport(legModel.getDepartureAirport().getCode());
        filterCriteria.setSelectedDuration(packageModel.getDuration().intValue());
        filterCriteria.setPackageId(packageModel.getId());
    }

}
