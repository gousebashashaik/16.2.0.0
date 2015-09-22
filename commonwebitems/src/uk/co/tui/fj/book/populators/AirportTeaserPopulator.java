/**
 *
 */
package uk.co.tui.fj.book.populators;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import de.hybris.platform.commerceservices.converter.Populator;

import javax.annotation.Resource;

import uk.co.portaltech.travel.services.AirportService;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.th.book.view.data.teasers.AirportTeaserViewData;


/**
 * The Class AirportTeaserPopulator.
 *
 * @author samantha.gd
 */
public class AirportTeaserPopulator implements Populator<BasePackage, AirportTeaserViewData>
{

    /** The airport service. */
    @Resource
    private AirportService airportService;

    /** The package component service. */
    @Resource
    private PackageComponentService packageComponentService;

    /**
     * @return the airportService
     */
    public AirportService getAirportService()
    {
        return airportService;
    }

    /**
     * @param airportService
     *           the airportService to set
     */
    public void setAirportService(final AirportService airportService)
    {
        this.airportService = airportService;
    }


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage source, final AirportTeaserViewData target)
    {
        final String airportCode = packageComponentService.getFlightItinerary(source).getOutBound().get(0).getDepartureAirport()
                .getCode();
        target.setAirportCode(airportCode);
        if (isNotEmpty(airportCode))
        {
            target.setAirportName(airportService.getAirportByCode(airportCode).getName());
        }
        target.setPackageId(source.getId());
        target.setPerPersonPrice(source.getPrice().getRate().getAmount());
        target.setTotalPrice(source.getPrice().getAmount().getAmount());
    }

}
