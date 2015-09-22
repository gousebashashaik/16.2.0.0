/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;


/**
 * @author niranjani.r
 *
 */
public class BackToSearchResultsUrlResolver extends AbstractUrlResolver<SearchResultsRequestData>
{
    @Resource
    private ConfigurationService configurationService;

    @Resource
    private SessionService sessionService;

    protected String context = "";
    private static final String PACKAGES = "/packages";
    private static final String AND = "&";
    private static final String EQUALS = "=";
    private static final String QUESTION = "?";
    private static final String AIRPORTS = "airports[]";
    private static final String UNITS = "units[]";
    private static final String WHEN = "when";
    private static final String UNTIL = "until";
    private static final String FLEXIBILITY = "flexibility";
    private static final String FLEXIBLEDAYS = "flexibleDays";
    private static final String NOOFADULTS = "noOfAdults";
    private static final String NOOFSENIORS = "noOfSeniors";
    private static final String NOOFCHIDREN = "noOfChildren";
    private static final String CHILDRENAGE = "childrenAge";
    private static final String DURATION = "duration";
    private static final String FIRST = "first";
    private static final String SEARCHREQUESTTYPE = "searchRequestType";
    private static final String SEARCHPARAMETER = "sp";
    private static final String MULTISELECT = "multiSelect";
    private static final String COLON = "%3A";
    private static final String INS = "ins";






    private static final String SORT = "Sort";


    /*
     * This methods resolves the url for back to search in case of main search.
     */
    @Override
    public String resolve(final SearchResultsRequestData source)
    {
        final StringBuilder url = new StringBuilder();
        url.append(QUESTION).append(AIRPORTS).append(EQUALS).append(getAirportsCode(source.getAirports()));
        url.append(AND).append(UNITS).append(EQUALS).append(getWhereTo(source.getUnits()));
        url.append(AND).append(WHEN).append(EQUALS).append(String.valueOf(source.getWhen()));
        url.append(AND).append(UNTIL).append(EQUALS).append(String.valueOf(source.getUntil()));
        url.append(AND).append(FLEXIBILITY).append(EQUALS).append(source.isFlexibility());
        url.append(AND).append(FLEXIBLEDAYS).append(EQUALS).append(source.getFlexibleDays());
        url.append(AND).append(NOOFADULTS).append(EQUALS).append(source.getNoOfAdults());
        url.append(AND).append(NOOFSENIORS).append(EQUALS).append(source.getNoOfSeniors());
        url.append(AND).append(NOOFCHIDREN).append(EQUALS).append(source.getNoOfChildren());
        url.append(AND).append(CHILDRENAGE).append(EQUALS).append(getChildrenAgeAsString(source.getChildrenAge()));
        url.append(AND).append(DURATION).append(EQUALS).append(String.valueOf(source.getDuration()));
        url.append(AND).append(FIRST).append(EQUALS).append(String.valueOf(source.getFirst()));
        url.append(AND).append(SEARCHREQUESTTYPE).append(EQUALS).append(getSearchRequestType(source));
        url.append(AND).append(SEARCHPARAMETER).append(EQUALS).append(Boolean.TRUE.toString());
        url.append(AND).append(MULTISELECT).append(EQUALS).append(Boolean.TRUE.toString());
      final BrandDetails brandDetails = sessionService.getCurrentSession().getAttribute("brandDetails");
        if (brandDetails != null && StringUtils.equalsIgnoreCase(brandDetails.getSiteName(), "thomson"))
        {
            url.append(AND).append(MULTISELECT).append(EQUALS).append(Boolean.TRUE.toString());
        }

        return getContext() + PACKAGES + url.toString();
    }

    public String getContext()
    {

        final BrandDetails brandDetails = sessionService.getCurrentSession().getAttribute("brandDetails");

        if (brandDetails != null && (StringUtils.equalsIgnoreCase(brandDetails.getSiteName(), "thomson")))
        {
            context = configurationService.getConfiguration().getString("thweb.webroot");
        }
        else if (brandDetails != null && (StringUtils.equalsIgnoreCase(brandDetails.getSiteName(), "firstchoice")))
        {
            context = configurationService.getConfiguration().getString("tuiweb.webroot");
        }
        return context;
    }

    private String getAirportsCode(final List<AirportData> airports)
    {
        final StringBuilder airportcodes = new StringBuilder();

        String pipe = "";
        if (airports != null && !airports.isEmpty())
        {
            for (final AirportData airport : airports)
            {
                airportcodes.append(pipe).append(airport.getId());
                pipe = "|";
            }
        }
        return airportcodes.toString();
    }

    private String getWhereTo(final List<UnitData> whereToList)
    {
        final StringBuilder whereTo = new StringBuilder("");
        String pipe = "";
        if (whereToList != null && !whereToList.isEmpty())
        {
            for (final UnitData unit : whereToList)
            {
                whereTo.append(pipe).append(unit.getId()).append(COLON).append(unit.getType());
                pipe = "|";
            }
        }
        return whereTo.toString();
    }

    private String getChildrenAgeAsString(final List<Integer> childrenAge)
    {
        final StringBuilder ages = new StringBuilder("");
        String comma = "";
        if (childrenAge != null && !childrenAge.isEmpty())
        {
            for (final Integer age : childrenAge)
            {
                ages.append(comma).append(age.intValue());
                comma = ",";
            }
        }
        return ages.toString();
    }

    private String getSearchRequestType(final SearchResultsRequestData request)
    {
        if (request.getSearchRequestType() != null && !request.getSearchRequestType().equals(SORT))
        {
            return request.getSearchRequestType();
        }
        return INS;
    }

}
