/**
 *
 */
package uk.co.tui.fo.book.url.builders;

import static uk.co.tui.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;

/**
 * @author prashant.godi
 *
 */
public class SearchResultsPageUrlBuilder
{

   @Resource
   private SessionService sessionService;

   private static final String EQUALS = "=";

   private static final String AND = "&";

   private static final String FLYINGFROM = "flyingFrom[]";

   private static final String FLYINGTO = "flyingTo[]";

   private static final String DEPDATE = "depDate";

   private static final String RETURNDATE = "returnDate";

   private static final String SEL_DEPDATE = "selDepDate";

   private static final String SEL_RETURNDATE = "selArrDate";

   private static final String TAB_ID = "tabId";

   private static final String ADULTS = "adults";

   private static final String CHILDREN = "children";

   private static final String INFANTS = "infants";

   private static final String INFANTAGE = "infantAge";

   private static final String ISONEWAY = "isOneWay";

   private static final String CHILDAGE = "childAge";

   private static final String ISFLEXIBLE = "isFlexible";

   private static final String SEARCH = "search";

   private static final String QUESTION = "?";

   private static final String Y = "Y";

   private static final String EMPTY = "";

   private static final String SEARCH_TYPE = "searchType";

   private static final String SELECTED = "selected";

   private static final String LATESTCRITERIA = "flightsearchcriteria";

   private static final String BACK_URL_SEARCH_TYPE = "backtosearch";

   /**
    * To construct the book flow accommodation page url from the current cart package model.
    *
    * @return the String
    */
   public String getSearchResultsBookFlowPageUrl()
   {

      final StringBuilder searchResultsPageUrl = new StringBuilder();

      final FlightSearchCriteria searchParameterInSession =
         sessionService.getAttribute(LATESTCRITERIA);
      if (searchParameterInSession != null)
      {
         if (searchParameterInSession.getSearchCriteriaType().equals(SELECTED)
            || searchParameterInSession.getSearchCriteriaType().equals(BACK_URL_SEARCH_TYPE))
         {
            searchResultsPageUrl.append(SEARCH).append(QUESTION);
            searchResultsPageUrl.append(FLYINGFROM).append(EQUALS)
               .append(commaSeparatedList(searchParameterInSession.getDepartureAirportCode(), "|"))
               .append(AND);
            searchResultsPageUrl.append(FLYINGTO).append(EQUALS)
               .append(commaSeparatedList(searchParameterInSession.getArrivalAirportCode(), "|"))
               .append(AND);
            searchResultsPageUrl.append(DEPDATE).append(EQUALS)
               .append(searchParameterInSession.getDepartureDate()).append(AND);
            getReturnDate(searchResultsPageUrl, searchParameterInSession);
            searchResultsPageUrl.append(ADULTS).append(EQUALS)
               .append(searchParameterInSession.getAdultCount()).append(AND);
            searchResultsPageUrl.append(CHILDREN).append(EQUALS)
               .append(searchParameterInSession.getChildCount()).append(AND);
            searchResultsPageUrl.append(INFANTS).append(EQUALS)
               .append(searchParameterInSession.getInfantCount()).append(AND);
            getInfantAges(searchResultsPageUrl, searchParameterInSession);

            searchResultsPageUrl.append(ISONEWAY).append(EQUALS)
               .append(searchParameterInSession.isOneWay()).append(AND);
            getChildAges(searchResultsPageUrl, searchParameterInSession);
            searchResultsPageUrl.append(SEARCH_TYPE).append(EQUALS).append(BACK_URL_SEARCH_TYPE)
               .append(AND);

            searchResultsPageUrl.append(SEL_DEPDATE).append(EQUALS)
               .append(searchParameterInSession.getSelDepDate()).append(AND);
            getSelReturnDate(searchResultsPageUrl, searchParameterInSession);
            searchResultsPageUrl.append(TAB_ID).append(EQUALS)
               .append(searchParameterInSession.getTabId()).append(AND);

            searchResultsPageUrl.append(ISFLEXIBLE).append(EQUALS).append(Y);

            searchParameterInSession.setBackIdentity("browserback");
            sessionService.setAttribute(LATESTCRITERIA, searchParameterInSession);

         }
         else
         {
            searchResultsPageUrl.append(searchParameterInSession.getBackDealsIdentity());
         }

      }
      return searchResultsPageUrl.toString();
   }

   /**
    * @param searchResultsPageUrl
    * @param searchParameterInSession
    */
   private void getReturnDate(final StringBuilder searchResultsPageUrl,
      final FlightSearchCriteria searchParameterInSession)
   {
      if (searchParameterInSession.getReturnDate() != null)
      {
         searchResultsPageUrl.append(RETURNDATE).append(EQUALS)
            .append(searchParameterInSession.getReturnDate()).append(AND);
      }
      else
      {
         searchResultsPageUrl.append(RETURNDATE).append(EQUALS).append(EMPTY).append(AND);
      }
   }

   /**
    * @param searchResultsPageUrl
    * @param searchParameterInSession
    */
   private void getSelReturnDate(final StringBuilder searchResultsPageUrl,
      final FlightSearchCriteria searchParameterInSession)
   {
      if (searchParameterInSession.getReturnDate() != null)
      {
         searchResultsPageUrl.append(SEL_RETURNDATE).append(EQUALS)
            .append(searchParameterInSession.getSelArrDate()).append(AND);
      }
      else
      {
         searchResultsPageUrl.append(SEL_RETURNDATE).append(EQUALS).append(EMPTY).append(AND);
      }
   }

   /**
    * @param searchResultsPageUrl
    * @param searchParameterInSession
    */
   private void getChildAges(final StringBuilder searchResultsPageUrl,
      final FlightSearchCriteria searchParameterInSession)
   {
      if (searchParameterInSession.getChildages() != null)
      {
         searchResultsPageUrl.append(CHILDAGE).append(EQUALS)
            .append(getAges(searchParameterInSession.getChildages())).append(AND);
      }
      else
      {
         searchResultsPageUrl.append(CHILDAGE).append(EQUALS).append(EMPTY).append(AND);
      }
   }

   /**
    * @param searchResultsPageUrl
    * @param searchParameterInSession
    */
   private void getInfantAges(final StringBuilder searchResultsPageUrl,
      final FlightSearchCriteria searchParameterInSession)
   {
      if (searchParameterInSession.getInfantAges() != null)
      {
         searchResultsPageUrl.append(INFANTAGE).append(EQUALS)
            .append(getAges(searchParameterInSession.getInfantAges())).append(AND);
      }
      else
      {
         searchResultsPageUrl.append(INFANTAGE).append(EQUALS).append(EMPTY).append(AND);
      }
   }

   /**
    * Gets the child ages.
    *
    * @return the child ages
    */
   @SuppressWarnings(BOXING)
   private StringBuilder getAges(final List<String> ages)
   {
      return StringUtil.commaSeparatedList(ages);
   }

   /**
    * @param list
    * @return string builder
    */
   private static <L> StringBuilder commaSeparatedList(final List<L> list, final String delimeter)
   {
      final StringBuilder builder = new StringBuilder();

      if (CollectionUtils.isNotEmpty(list))
      {
         int cnt = 1;
         for (final L element : list)
         {
            builder.append(element);
            if (cnt < list.size())
            {
               builder.append(delimeter);
            }
            ++cnt;
         }
      }
      return builder;
   }

}
