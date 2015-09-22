/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.async.logging.TUILogUtils;


/**
 * This class is used to validate the Search Request
 *
 * @author chandrasekhar.v
 *
 */
public class SearchRequestValidator
{

    private static final TUILogUtils LOG = new TUILogUtils("SearchRequestValidator");

    //Search Results Page
    private static final String AIRPORTS = "airports[]";
    private static final String UNITS = "units[]";
    private static final String WHEN = "when";
    private static final String UNTIL = "until";
    private static final String FLEXIBILITY = "flexibility";
    private static final String SEARCH_REQUEST_TYPE = "searchRequestType";
    private static final String SP = "sp";
    private static final String CHILDREN_AGE = "childrenAge";
    private static final String FLEXIBLE_DAYS = "flexibleDays";
    private static final String NO_OF_ADULTS = "noOfAdults";
    private static final String NO_OF_SENIORS = "noOfSeniors";
    private static final String NO_OF_CHILDREN = "noOfChildren";
    private static final String DURATION = "duration";
    private static final String FIRST = "first";
    private static final String BACKTOSEARCHRSULTS = "BacktoResults";

    //Accommodation Details Page
    private static final String PRODUCT_CODE = "productCode";
    private static final String TAB = "tab";
    private static final String PACKAGE_ID = "packageId";
    private static final String INDEX = "index";
    private static final String FIN_POS = "finPos";
    private static final String ISCAPE_REQUEST = "requestFrom";

    //Flight option page

    private static final String FLIGHT_SORTBY = "flightSortBy";
    private static final String AIRPORT = "airport";
    private static final String FLIGHTOPTIONS = "flightOptions";
    private static final String BOARDBASIS_CODE = "boardBasisCode";
    private static final String BRANDTYPE = "brandType";
    private static final String MULTI_SELECT = "multiSelect";

    //Search Results Page
    private static final String PATTERN_FOR_AIRPORTS = "([a-zA-Z|])*";
    private static final String PATTERN_FOR_UNITS = "([a-zA-Z0-9// |:_-])*";
    private static final String PATTERN_FOR_WHEN = "^[\\d]{2}-[\\d]{2}-[\\d]{4}$";
    private static final String PATTERN_FOR_UNTIL = "^[\\d]{2}-[\\d]{2}-[\\d]{4}$";
    private static final String PATTERN_FOR_FLEXIBILITY = "(true|false|TRUE|FALSE)*";
    private static final String PATTERN_FOR_SEARCH_REQUEST_TYPE = "[a-zA-Z]*";
    private static final String PATTERN_FOR_SP = "(true|false|TRUE|FALSE)*";
    private static final String PATTERN_FOR_CHILDREN_AGE = "([0-9]+([,][0-9]+)*)*[,]*";
    private static final String PATTERN_FOR_NUMBERS = "[0-9]*";
    private static final String PATTERN_FOR_PRODUCT = "[0-9a-zA-Z]*";
    private static final String PATTERN_FOR_BACK_TO_SEARCH = "([a-zA-Z|])*";

    //Accommodation Details Page
    private static final String PATTERN_FOR_TAB = "[a-zA-Z-_0-9]*";
    private static final String PATTERN_FOR_PACKAGE_ID = "[a-zA-Z0-9\\-/]*";

    private static final String PATTERN_FOR_ISCAPE_REQUEST = "[a-zA-Z]*";

    //FlightOptionPage

    private static final String PATTERN_FOR_FLIGHTSORTBY = PATTERN_FOR_AIRPORTS;
    private static final String PATTERN_FOR_AIRPORT = PATTERN_FOR_AIRPORTS;

    private static final String PATTERN_FOR_FLIGHTOPTIONS = "(true|false|TRUE|FALSE)*";
    private static final String PATTERN_FOR_BOARDBASISCODE = "([A-Z|])*";
    private static final String PATTERN_FOR_BRANDTYPE = PATTERN_FOR_AIRPORTS;

    private boolean flag = true;

    private static Pattern[] patterns = new Pattern[]
    {
            // Script fragments
            Pattern.compile(".*<script>(.*?)</script>.*", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile(".*src[\r\n]*=[\r\n]*\\\'(.*?)\\\'*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile(".*src[\r\n]*=[\r\n]*\\\"(.*?)\\\"*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile(".*</script>.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*<script(.*?)>.*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile(".*eval\\((.*?)\\).*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile(".*expression\\((.*?)\\).*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile(".*javascript:.*", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile(".*vbscript:.*", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile(".*onload(.*?)=.*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL) };

    public boolean validate(final HttpServletRequest request)
    {

        flag = validateParameterValues(request);

        findMatcher(request.getParameter(AIRPORTS), PATTERN_FOR_AIRPORTS);
        findMatcher(request.getParameter(UNITS), PATTERN_FOR_UNITS);
        findMatcher(request.getParameter(WHEN), PATTERN_FOR_WHEN);
        findMatcher(request.getParameter(UNTIL), PATTERN_FOR_UNTIL);
        findMatcher(request.getParameter(FLEXIBILITY), PATTERN_FOR_FLEXIBILITY);
        findMatcher(request.getParameter(CHILDREN_AGE), PATTERN_FOR_CHILDREN_AGE);
        findMatcher(request.getParameter(SEARCH_REQUEST_TYPE), PATTERN_FOR_SEARCH_REQUEST_TYPE);
        findMatcher(request.getParameter(SP), PATTERN_FOR_SP);
        findMatcher(request.getParameter(DURATION), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(FIRST), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(FLEXIBLE_DAYS), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(NO_OF_SENIORS), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(NO_OF_ADULTS), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(NO_OF_CHILDREN), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(MULTI_SELECT), PATTERN_FOR_SP);
        findMatcher(request.getParameter(BACKTOSEARCHRSULTS), PATTERN_FOR_BACK_TO_SEARCH);

        //Accommodation Details Page
        findMatcher(request.getParameter(PRODUCT_CODE), PATTERN_FOR_PRODUCT);
        findMatcher(request.getParameter(TAB), PATTERN_FOR_TAB);
        findMatcher(request.getParameter(INDEX), PATTERN_FOR_NUMBERS);
        findMatcher(request.getParameter(FIN_POS), PATTERN_FOR_PRODUCT);
        findMatcher(request.getParameter(PACKAGE_ID), PATTERN_FOR_PACKAGE_ID);
        findMatcher(request.getParameter(ISCAPE_REQUEST), PATTERN_FOR_ISCAPE_REQUEST);

        //FlightOption Page
        findMatcher(request.getParameter(FLIGHT_SORTBY), PATTERN_FOR_FLIGHTSORTBY);
        findMatcher(request.getParameter(AIRPORT), PATTERN_FOR_AIRPORT);
        findMatcher(request.getParameter(FLIGHTOPTIONS), PATTERN_FOR_FLIGHTOPTIONS);
        findMatcher(request.getParameter(BOARDBASIS_CODE), PATTERN_FOR_BOARDBASISCODE);
        findMatcher(request.getParameter(BRANDTYPE), PATTERN_FOR_BRANDTYPE);
        return flag;
    }

    /**
     *
     */
    @SuppressWarnings("deprecation")
    private void findMatcher(final String source, final String regexPattern)
    {

        if (flag && StringUtils.isNotBlank(source))
        {
            try
            {

                final String decodedString = URLDecoder.decode(source, "UTF-8");
                final Pattern pattern = Pattern.compile(regexPattern);
                final Matcher matcher = pattern.matcher(decodedString);
                flag = matcher.matches();

            }
            catch (final UnsupportedEncodingException e)
            {

                LOG.error("> Error While decoding the Source..", e);
            }
        }
    }

    public boolean validateParameterValues(final HttpServletRequest request)
    {

        final Map params = request.getParameterMap();
        final Iterator i = params.entrySet().iterator();



        while (i.hasNext())
        {
            final Map.Entry<String, String[]> entry = (Entry<String, String[]>) i.next();

            final String key = entry.getKey();
            String value = (entry.getValue())[0];

            if (flag && StringUtils.isNotBlank(key))
            {
                // Avoid null characters
                value = value.replaceAll("\0", "");
                for (final Pattern scriptPattern : patterns)
                {
                    final Matcher matcherValue = scriptPattern.matcher(value);
                    final Matcher matcherKey = scriptPattern.matcher(key);
                    if (matcherValue.matches() || matcherKey.matches())
                    {
                        return false;
                    }
                }
            }
        }


        return flag;
    }

}
