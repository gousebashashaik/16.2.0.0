/**
 *
 */
package uk.co.portaltech.tui.web.url.builders;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.tui.utils.ConfigurationUtils;
import uk.co.portaltech.tui.utils.PackageIdGenerator;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author
 *
 */
public final class FlightOptionRequestBuilder
{

   private static final int TWO = 2;

   private static final String PHOENIX_PACKAGE_ID = "p";

   private static final String COLON = ":";

   private static final String UNDERSCORE = "_";

   private static final String EQUAL = "=";

   private static final String AMP = "&";

   private static final String BOOK_MARK = "sttrkr";

   private static final int SEVEN = 7;

   private static final int SEVENTEEN = 17;

   private static final int DEFAULT_PAX_CONFIG = 9;

   private static final String COMMA = ",";

   private static final String TRUE = "true";

   private static final String HYBRIS_FLIGHT_OPTIONS_PAGE =
      "/holiday/book/flightoptions?JSESSIONID_FC=";

   private static final String SMARTPHONE = "Smartphone";

   private static final String JSESSIONID = "httpSessionId";

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private ConfigurationUtils configurationUtils;

   @Resource
   private SessionService sessionService;

   private FlightOptionRequestBuilder()
   {

   }

   /**
    * This method is populating flight option url.
    *
    * @param searchPanelComponentModel
    *
    * @return String
    *
    */
   public String builder(final BookFlowAccommodationViewData source,
      final SearchPanelComponentModel searchPanelComponentModel,
      final SearchResultsRequestData requestData)
   {
      final String server =
         configurationService.getConfiguration().getString("iscape.flight.options.url",
            "http://www.firstchoice.co.uk/fcsun/page/flightoptions/flightoptions.page?");

      String departureDate = "";
      if (StringUtils.isNotEmpty(source.getSearchRequestData().getDepartureDate()))
      {
         departureDate = source.getSearchRequestData().getDepartureDate();
      }
      else
      {
         departureDate = source.getSearchRequestData().getWhen();
      }

      return (new StringBuilder(server)
         .append("boardBasisPackageNumber")
         .append(EQUAL)
         .append(
            getBoardBasisPackageNumber(source.getPackageData().getAccommodation().getRooms().get(0)
               .getBoardBasisCode(), source.getPackageData()))
         .append(appendRequestFrom())
         .append(AMP)
         .append(BOOK_MARK)
         .append(EQUAL)
         .append("mthyr")
         .append(COLON)
         .append(getMOnthAndYear(departureDate.split("-")))
         .append(UNDERSCORE)
         .append("durT")
         .append(COLON)
         .append(setDurationType(source.getPackageData().getDuration()))
         .append(UNDERSCORE)
         .append("ls")
         .append(COLON)
         .append(Boolean.FALSE)
         .append(UNDERSCORE)
         .append("tuidesc")
         .append(COLON)
         .append(
            source.getPackageData().getAccommodation().getLocation().getDestination().getCode())
         .append(UNDERSCORE)
         .append("day")
         .append(COLON)
         .append(departureDate.split("-")[0])
         .append(UNDERSCORE)
         .append("mps")
         .append(COLON)
         .append(getMaximumPartySize(searchPanelComponentModel))
         .append(UNDERSCORE)
         .append("sda")
         .append(COLON)
         .append(TRUE)
         .append(UNDERSCORE)
         .append("pconfig")
         .append(COLON)
         .append(setPartyConfiguration(source, searchPanelComponentModel, requestData))
         .append(UNDERSCORE)
         .append("tchd")
         .append(COLON)
         .append(source.getSearchRequestData().getChildCount())
         .append(UNDERSCORE)
         .append("rating")
         .append(COLON)
         .append(0)
         .append(UNDERSCORE)
         .append("dess")
         .append(COLON)
         .append(TRUE)
         .append(UNDERSCORE)
         .append("act")
         .append(COLON)
         .append(0)
         .append(UNDERSCORE)
         .append("jsen")
         .append(COLON)
         .append(Boolean.TRUE)
         .append(UNDERSCORE)
         .append("attrstr")
         .append(COLON)
         .append("||||||||null|null")
         .append(UNDERSCORE)
         .append("tinf")
         .append(COLON)
         .append(source.getSearchRequestData().getInfantCount())
         .append(UNDERSCORE)
         .append("mnth")
         .append(COLON)
         .append((source.getSearchRequestData().getWhen().split("-"))[1])
         .append(UNDERSCORE)
         .append("bc")
         .append(COLON)
         .append(SEVENTEEN)
         .append(UNDERSCORE)
         .append("margindt")
         .append(COLON)
         .append(SEVEN)
         .append(UNDERSCORE)
         .append("tadt")
         .append(COLON)
         .append(source.getSearchRequestData().getNoOfAdults())
         .append(UNDERSCORE)
         .append("numr")
         .append(COLON)
         .append(Integer.toString(source.getPackageData().getAccommodation().getRooms().size()))
         .append(UNDERSCORE)
         .append("depm")
         .append(COLON)
         .append(SEVEN)
         .append(UNDERSCORE)
         .append("dur")
         .append(COLON)
         .append(source.getPackageData().getDuration())
         .append(UNDERSCORE)
         .append("dtx")
         .append(COLON)
         .append(source.getSearchRequestData().getFlexibleDays())
         .append(UNDERSCORE)
         .append("dxsel")
         .append(COLON)
         .append(0)
         .append(UNDERSCORE)
         .append("dac")
         .append(COLON)
         .append(
            source.getPackageData().getItinerary().getOutbounds().get(0).getDepartureAirportCode())
         .append(UNDERSCORE).append("loct").append(COLON).append(0).append(UNDERSCORE)
         .append("tsnr").append(COLON).append(source.getSearchRequestData().getNoOfSeniors())
         .append(UNDERSCORE).append("year").append(COLON)
         .append((source.getSearchRequestData().getWhen().split("-"))[TWO]).append(UNDERSCORE)
         .append("dta").append(COLON).append(Boolean.FALSE).append(UNDERSCORE).append("uc")
         .append(COLON).append(source.getPackageData().getAccommodation().getCode())
         .append(UNDERSCORE).append("pid").append(COLON)
         .append(PackageIdGenerator.generateIscapePackageId(source.getPackageData()))
         .append(UNDERSCORE).append("sd").append(COLON).append(setSelectedDate(source
         .getPackageData().getItinerary().getDepartureDate().split("-")))).append(AMP)
         .append(PHOENIX_PACKAGE_ID).append(EQUAL).append(source.getPackageData().getPackageId())
         .append(AMP).append("tracsId").append(EQUAL)
         .append(source.getPackageData().getTracsPackageId()).append(AMP).append("cAge")
         .append(EQUAL).append(appendChildrenAge(source.getSearchRequestData())).append(AMP)
         .append("flexibility").append(EQUAL).append(source.getSearchRequestData().isFlexibility())
         .toString();
   }

   /**
    * This method is populating flight option url.
    *
    * @param HolidayFinderComponentModel
    */
   public String builder(final BookFlowAccommodationViewData source,
      final HolidayFinderComponentModel holidayFinderComponentModel,
      final SearchResultsRequestData requestData)
   {

      String server = "";
      final String sessionId = sessionService.getAttribute(JSESSIONID);

      if ((StringUtils.equalsIgnoreCase(configurationUtils.getMoovwebSwitchFC(), "on"))
         && (StringUtils.equalsIgnoreCase(
            configurationService.getConfiguration().getString("filterThirdPartyFlights.mobile.fc",
               "true"), "true")) && !(isThirdPartyFlight(source))
         && (sessionService.getAttribute(SMARTPHONE) != null))
      {
         server =
            configurationService.getConfiguration().getString("iscape.flight.options.moovweb.url",
               "http://m.firstchoice.co.uk/fcsun/page/flightoptions/flightoptions.page?");
      }
      else
      {
         return HYBRIS_FLIGHT_OPTIONS_PAGE + sessionId;
      }
      String departureDate = "";
      if (StringUtils.isNotEmpty(source.getSearchRequestData().getDepartureDate()))
      {
         departureDate = source.getSearchRequestData().getDepartureDate();
      }
      else
      {
         departureDate = source.getSearchRequestData().getWhen();
      }

      return (new StringBuilder(server)
         .append("boardBasisPackageNumber")
         .append(EQUAL)
         .append(
            getBoardBasisPackageNumber(source.getPackageData().getAccommodation().getRooms().get(0)
               .getBoardBasisCode(), source.getPackageData()))
         .append(appendRequestFrom())
         .append(AMP)
         .append(BOOK_MARK)
         .append(EQUAL)
         .append("mthyr")
         .append(COLON)
         .append(getMOnthAndYear(departureDate.split("-")))
         .append(UNDERSCORE)
         .append("durT")
         .append(COLON)
         .append(setDurationType(source.getPackageData().getDuration()))
         .append(UNDERSCORE)
         .append("ls")
         .append(COLON)
         .append(Boolean.FALSE)
         .append(UNDERSCORE)
         .append("tuidesc")
         .append(COLON)
         .append(
            source.getPackageData().getAccommodation().getLocation().getDestination().getCode())
         .append(UNDERSCORE)
         .append("day")
         .append(COLON)
         .append(departureDate.split("-")[0])
         .append(UNDERSCORE)
         .append("mps")
         .append(COLON)
         .append(getMaximumPartySize(holidayFinderComponentModel))
         .append(UNDERSCORE)
         .append("sda")
         .append(COLON)
         .append(TRUE)
         .append(UNDERSCORE)
         .append("pconfig")
         .append(COLON)
         .append(setPartyConfiguration(source, holidayFinderComponentModel, requestData))
         .append(UNDERSCORE)
         .append("tchd")
         .append(COLON)
         .append(source.getSearchRequestData().getChildCount())
         .append(UNDERSCORE)
         .append("rating")
         .append(COLON)
         .append(0)
         .append(UNDERSCORE)
         .append("dess")
         .append(COLON)
         .append(TRUE)
         .append(UNDERSCORE)
         .append("act")
         .append(COLON)
         .append(0)
         .append(UNDERSCORE)
         .append("jsen")
         .append(COLON)
         .append(Boolean.TRUE)
         .append(UNDERSCORE)
         .append("attrstr")
         .append(COLON)
         .append("||||||||null|null")
         .append(UNDERSCORE)
         .append("tinf")
         .append(COLON)
         .append(source.getSearchRequestData().getInfantCount())
         .append(UNDERSCORE)
         .append("mnth")
         .append(COLON)
         .append((source.getSearchRequestData().getWhen().split("-"))[1])
         .append(UNDERSCORE)
         .append("bc")
         .append(COLON)
         .append(SEVENTEEN)
         .append(UNDERSCORE)
         .append("margindt")
         .append(COLON)
         .append(SEVEN)
         .append(UNDERSCORE)
         .append("tadt")
         .append(COLON)
         .append(source.getSearchRequestData().getNoOfAdults())
         .append(UNDERSCORE)
         .append("numr")
         .append(COLON)
         .append(Integer.toString(source.getPackageData().getAccommodation().getRooms().size()))
         .append(UNDERSCORE)
         .append("depm")
         .append(COLON)
         .append(SEVEN)
         .append(UNDERSCORE)
         .append("dur")
         .append(COLON)
         .append(source.getPackageData().getDuration())
         .append(UNDERSCORE)
         .append("dtx")
         .append(COLON)
         .append(source.getSearchRequestData().getFlexibleDays())
         .append(UNDERSCORE)
         .append("dxsel")
         .append(COLON)
         .append(0)
         .append(UNDERSCORE)
         .append("dac")
         .append(COLON)
         .append(
            source.getPackageData().getItinerary().getOutbounds().get(0).getDepartureAirportCode())
         .append(UNDERSCORE).append("loct").append(COLON).append(0).append(UNDERSCORE)
         .append("tsnr").append(COLON).append(source.getSearchRequestData().getNoOfSeniors())
         .append(UNDERSCORE).append("year").append(COLON)
         .append((source.getSearchRequestData().getWhen().split("-"))[TWO]).append(UNDERSCORE)
         .append("dta").append(COLON).append(Boolean.FALSE).append(UNDERSCORE).append("uc")
         .append(COLON).append(source.getPackageData().getAccommodation().getCode())
         .append(UNDERSCORE).append("pid").append(COLON)
         .append(PackageIdGenerator.generateIscapePackageId(source.getPackageData()))
         .append(UNDERSCORE).append("sd").append(COLON).append(setSelectedDate(source
         .getPackageData().getItinerary().getDepartureDate().split("-")))).append(AMP)
         .append(PHOENIX_PACKAGE_ID).append(EQUAL).append(source.getPackageData().getPackageId())
         .append(AMP).append("tracsId").append(EQUAL)
         .append(source.getPackageData().getTracsPackageId()).append(AMP).append("cAge")
         .append(EQUAL).append(appendChildrenAge(source.getSearchRequestData())).append(AMP)
         .append("flexibility").append(EQUAL).append(source.getSearchRequestData().isFlexibility())
         .toString();
   }

   /**
    * Addition of new param -children age which will include child age + infant age
    */
   private String appendChildrenAge(final SearchResultsRequestData requestData)
   {

      if (requestData != null && CollectionUtils.isNotEmpty(requestData.getChildrenAge()))
      {

         return org.springframework.util.StringUtils.collectionToDelimitedString(
            requestData.getChildrenAge(), COMMA);
      }
      return StringUtils.EMPTY;
   }

   /**
    * Hybris should send requestFrom variable to iscape to distinguish the request mode sent from
    * either hybris or iscape in flight option page * configuration iscapeRedirect.requestFrom is
    * added -- As iscape is not ready to accept the new parameter
    *
    * @return String
    */
   private String appendRequestFrom()
   {
      final boolean iscapeRedirect =
         configurationService.getConfiguration().getBoolean("iscapeRedirect.requestFrom", true);
      if (iscapeRedirect)
      {
         final StringBuilder sb = new StringBuilder();
         return sb.append(AMP).append("requestFrom").append(EQUAL).append("phoenix").toString();
      }
      return StringUtils.EMPTY;
   }

   /**
    * @param searchResultViewData
    *
    */
   private String getBoardBasisPackageNumber(final String boardbasisCode,
      final SearchResultViewData searchResultViewData)
   {
      final StringBuilder sb = new StringBuilder();
      if (StringUtils.isNotEmpty(boardbasisCode))
      {
         sb.append(boardbasisCode).append("|");
      }
      sb.append(PackageIdGenerator.generateIscapePackageId(searchResultViewData));

      return sb.toString();

   }

   /**
    *
    * @param all
    */
   private String setSelectedDate(final String[] all)
   {
      return (new StringBuilder(all[0]).append("/").append(all[1]).append("/").append(all[TWO]))
         .toString();
   }

   /**
    * Duration formation
    *
    * @param duration
    */
   private String setDurationType(final int duration)
   {
      final StringBuilder sb = new StringBuilder().append(duration).append("/");
      if (duration % SEVEN == 0)
      {
         return sb.append("w").toString();
      }
      return sb.append("n").toString();
   }

   /**
    * Month and year population
    *
    * @param all
    */
   private String getMOnthAndYear(final String[] all)
   {
      return (new StringBuilder(all[1]).append("/").append(all[TWO])).toString();
   }

   /**
    *
    * @param searchPanelComponentModel
    */
   @SuppressWarnings("boxing")
   private int getMaximumPartySize(final SearchPanelComponentModel searchPanelComponentModel)
   {
      if (searchPanelComponentModel != null && searchPanelComponentModel.getMaxNoOfAdult() != null)
      {
         return searchPanelComponentModel.getMaxNoOfAdult();
      }
      else
      {
         return DEFAULT_PAX_CONFIG;
      }
   }

   /**
    *
    * @param holidayFinderComponentModel
    */
   private int getMaximumPartySize(final HolidayFinderComponentModel holidayFinderComponentModel)
   {
      if (holidayFinderComponentModel != null
         && holidayFinderComponentModel.getMaxNoOfAdult() != null)
      {
         return holidayFinderComponentModel.getMaxNoOfAdult();
      }
      else
      {
         return DEFAULT_PAX_CONFIG;
      }
   }

   @SuppressWarnings("boxing")
   private String setPartyConfiguration(final BookFlowAccommodationViewData source,
      final SearchPanelComponentModel searchPanelComponentModel,
      final SearchResultsRequestData resultRequestData)
   {
      final StringBuilder partyConfiguration = new StringBuilder();
      // Changed To Send senoirs count as Pax.
      int totalAdults = resultRequestData.getNoOfAdults();

      for (int i = 0; i < source.getPackageData().getAccommodation().getRooms().size(); i++)
      {
         int adultsCounts = 0, seniorsCounts = 0;

         for (final PaxDetail pax : source.getPackageData().getAccommodation().getRooms().get(i)
            .getOccupancy().getPaxDetail())
         {

            if (totalAdults > 0 && pax.getAge() > searchPanelComponentModel.getMaxChildAge())
            {
               adultsCounts++;
               totalAdults--;
            }
            else if (totalAdults <= 0 && pax.getAge() > searchPanelComponentModel.getMaxChildAge())
            {
               seniorsCounts++;
            }
         }

         final StringBuilder party = new StringBuilder();
         for (final PaxDetail pax : source.getPackageData().getAccommodation().getRooms().get(i)
            .getOccupancy().getPaxDetail())
         {
            if (isValidSearchPanelModel(searchPanelComponentModel)
               && pax.getAge() <= searchPanelComponentModel.getMaxChildAge()
               && pax.getAge() >= searchPanelComponentModel.getInfantAge())
            {
               party.append(pax.getAge()).append("-");
            }
         }
         final StringBuilder noOfParty =
            new StringBuilder()
               .append(i + 1)
               .append("|")
               .append(adultsCounts)
               .append("|")
               .append(seniorsCounts)
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getChildren())
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getInfant()).append("|").append(party).append("/");
         partyConfiguration.append(noOfParty);

      }
      return partyConfiguration.toString();
   }

   /**
    * partyConfiguration for HolidayFinderComponentModel
    */
   @SuppressWarnings("boxing")
   private String setPartyConfiguration(final BookFlowAccommodationViewData source,
      final HolidayFinderComponentModel holidayFinderComponentModel,
      final SearchResultsRequestData resultRequestData)
   {
      final StringBuilder partyConfiguration = new StringBuilder();
      // Changed To Send senoirs count as Pax.
      int totalAdults = resultRequestData.getNoOfAdults();

      for (int i = 0; i < source.getPackageData().getAccommodation().getRooms().size(); i++)
      {
         int adultsCounts = 0, seniorsCounts = 0;

         for (final PaxDetail pax : source.getPackageData().getAccommodation().getRooms().get(i)
            .getOccupancy().getPaxDetail())
         {

            if (totalAdults > 0 && pax.getAge() > holidayFinderComponentModel.getMaxChildAge())
            {
               adultsCounts++;
               totalAdults--;
            }
            else if (totalAdults <= 0
               && pax.getAge() > holidayFinderComponentModel.getMaxChildAge())
            {
               seniorsCounts++;
            }
         }

         final StringBuilder party = new StringBuilder();
         for (final PaxDetail pax : source.getPackageData().getAccommodation().getRooms().get(i)
            .getOccupancy().getPaxDetail())
         {
            if (isValidSearchPanelModel(holidayFinderComponentModel)
               && pax.getAge() <= holidayFinderComponentModel.getMaxChildAge()
               && pax.getAge() >= holidayFinderComponentModel.getInfantAge())
            {
               party.append(pax.getAge()).append("-");
            }
         }
         final StringBuilder noOfParty =
            new StringBuilder()
               .append(i + 1)
               .append("|")
               .append(adultsCounts)
               .append("|")
               .append(seniorsCounts)
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getChildren())
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getInfant()).append("|").append(party).append("/");
         partyConfiguration.append(noOfParty);

      }
      return partyConfiguration.toString();
   }

   /**
    * Validating searchPanel model
    *
    * @param searchPanelComponentModel
    */
   private boolean isValidSearchPanelModel(final SearchPanelComponentModel searchPanelComponentModel)
   {
      return searchPanelComponentModel != null
         && searchPanelComponentModel.getMinChildAge() != null
         && searchPanelComponentModel.getMaxChildAge() != null;
   }

   /**
    * Validating HolidayFinderComponentmodel
    *
    * @param holidayFinderComponentModel
    */
   private boolean isValidSearchPanelModel(
      final HolidayFinderComponentModel holidayFinderComponentModel)
   {
      return holidayFinderComponentModel != null
         && holidayFinderComponentModel.getMinChildAge() != null
         && holidayFinderComponentModel.getMaxChildAge() != null;
   }

   private boolean isThirdPartyFlight(final BookFlowAccommodationViewData source)
   {
      final boolean outbound =
         source.getPackageData().getItinerary().getOutbounds().get(0).getExternalInventory() == Boolean.TRUE
            ? true : false;
      final boolean inbound =
         source.getPackageData().getItinerary().getInbounds().get(0).getExternalInventory() == Boolean.TRUE
            ? true : false;

      return outbound || inbound;
   }

}
