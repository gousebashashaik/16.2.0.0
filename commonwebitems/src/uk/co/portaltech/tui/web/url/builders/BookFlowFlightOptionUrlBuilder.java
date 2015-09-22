/**
 *
 */
package uk.co.portaltech.tui.web.url.builders;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.tui.utils.ConfigurationUtils;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

/**
 * @author laxmibai.p
 *
 */
public class BookFlowFlightOptionUrlBuilder implements RequestBuilder
{

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private ConfigurationUtils configurationUtils;

   @Resource
   private SessionService sessionService;

   @Resource
   ViewSelector viewSelector;

   private static final String COLON = ":";

   private static final String EQUAL = "=";

   private static final String AMP = "&";

   private static final String COMMA = ",";

   private static final String FLIGHT_OPTIONS_PAGE = "/flightoptions?";

   private static final String HYBRIS_FLIGHT_OPTIONS_PAGE =
      "/destinations/book/flightoptions?JSESSIONID_TH=";

   private static final String SMARTPHONE = "Smartphone";

   private static final String JSESSIONID = "httpSessionId";

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.requestbuilder.RequestBuilder#build(uk.co.portaltech
    * .tui.web.view.data.BookFlowAccommodationViewData,
    * uk.co.portaltech.travel.model.SearchPanelComponentModel,
    * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public String builder(final BookFlowAccommodationViewData source,
      final SearchPanelComponentModel searchPanelComponentModel)
   {

      String contextPath = "";
      final String sessionId = sessionService.getAttribute(JSESSIONID);
      if (viewSelector.checkIsMobile())
      {
         if ((StringUtils.equalsIgnoreCase(configurationUtils.getMoovwebSwitchTH(), "on"))
            && (StringUtils.equalsIgnoreCase(
               configurationService.getConfiguration().getString(
                  "filterThirdPartyFlights.mobile.th", "true"), "true"))
            && !(isThirdPartyFlight(source)) && (sessionService.getAttribute(SMARTPHONE) != null))
         {
            contextPath =
               configurationService.getConfiguration().getString("flight.options.moovweb.url")
                  + configurationService.getConfiguration().getString("thwebContextRoot");
         }
         else
         {
            return HYBRIS_FLIGHT_OPTIONS_PAGE + sessionId;
         }
      }
      else
      {
         contextPath = configurationService.getConfiguration().getString("thwebContextRoot");
      }

      String departureDate = "";
      boolean flexibility;

      if (StringUtils.isNotEmpty(source.getPackageData().getItinerary().getDepartureDate()))
      {
         departureDate = source.getPackageData().getItinerary().getDepartureDate();
         flexibility = false;
      }
      else if (StringUtils.isNotEmpty(source.getSearchRequestData().getDepartureDate()))
      {
         departureDate = source.getSearchRequestData().getDepartureDate();
         flexibility = source.getSearchRequestData().isFlexibility();
      }
      else
      {
         departureDate = source.getSearchRequestData().getWhen();
         flexibility = source.getSearchRequestData().isFlexibility();
      }

      return (new StringBuilder(contextPath + FLIGHT_OPTIONS_PAGE).append("productCode").append(
         EQUAL).append(source.getSearchRequestData().getProductCode()))
         .append(AMP)
         .append("noOfAdults")
         .append(EQUAL)
         .append(source.getSearchRequestData().getNoOfAdults())
         .append(AMP)
         .append("noOfChildren")
         .append(EQUAL)
         .append(source.getSearchRequestData().getNoOfChildren())
         /*
          * .append(AMP) .append("noOfSeniors")
          * .append(source.getSearchRequestData().getNoOfSeniors())
          */
         .append(AMP)
         .append("childrenAge")
         .append(EQUAL)
         .append(appendChildrenAge(source.getSearchRequestData()))
         .append(AMP)
         .append("duration")
         .append(EQUAL)
         .append(source.getPackageData().getDuration())
         .append(AMP)
         .append("flexibleDays")
         .append(EQUAL)
         .append(source.getSearchRequestData().getFlexibleDays())
         .append(AMP)
         .append("airports[]")
         .append(EQUAL)
         .append(appendAirports(source.getSearchRequestData().getAirports()))
         .append(AMP)
         .append("units[]")
         .append(EQUAL)
         .append(appendUnits(source.getSearchRequestData().getUnits()))
         .append(AMP)
         .append("flexibility")
         .append(EQUAL)
         .append(flexibility)
         .append(AMP)
         .append("when")
         .append(EQUAL)
         .append(departureDate)
         .append(AMP)
         .append("flightSortBy")
         .append(EQUAL)
         .append("airport")
         .append(AMP)
         .append("dayTripSearch")
         .append(EQUAL)
         // .append(appenddayTripSearch(source.getSearchRequestData().getUnits()))
         .append(AMP)
         .append("packageId")
         .append(EQUAL)
         .append(source.getPackageData().getPackageId())
         .append(AMP)
         .append("flightOptions")
         .append(EQUAL)
         .append("true")
         .append(AMP)
         .append("searchRequestType")
         .append(EQUAL)
         .append("ins")
         .append(AMP)
         .append("index")
         .append(EQUAL)
         .append(source.getPackageData().getIndex())
         .append(AMP)
         .append("finPos")
         .append(EQUAL)
         .append(source.getFinPos())
         .append(AMP)
         .append("boardBasisCode")
         .append(EQUAL)
         .append(
            setBoardBasisCode(source.getPackageData().getAlternateBoard(), source
               .getSearchRequestData().getSelectedBoardBasis())).append(AMP).append("brandType")
         .append(EQUAL).append(source.getPackageData().getBrandType())
         .append(setRoomsData(source, searchPanelComponentModel)).toString();
   }

   /**
    * @param source
    * @param searchPanelComponentModel
    * @return
    */
   @SuppressWarnings("boxing")
   private Object setRoomsData(final BookFlowAccommodationViewData source,
      final SearchPanelComponentModel searchPanelComponentModel)
   {
      final StringBuilder room = new StringBuilder("");

      room.append(AMP).append("room").append(EQUAL);
      for (int i = 0; i < source.getPackageData().getAccommodation().getRooms().size(); i++)
      {

         final StringBuilder party = new StringBuilder();
         for (final PaxDetail pax : source.getPackageData().getAccommodation().getRooms().get(i)
            .getOccupancy().getPaxDetail())
         {
            if (isValidSearchPanelModel(searchPanelComponentModel)
               && pax.getAge() <= searchPanelComponentModel.getMaxChildAge())
            {
               party.append(pax.getAge()).append(",");
            }
         }

         final StringBuilder noOfParty =
            new StringBuilder()
               .append(i + 1)
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getAdults())
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getSeniors())
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getChildren())
               .append("|")
               .append(
                  source.getPackageData().getAccommodation().getRooms().get(i).getOccupancy()
                     .getInfant()).append("|").append(party).append("-");
         room.append(noOfParty);

      }

      return StringUtils.removeEnd(room.toString(), "-");
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

   private String setBoardBasisCode(final List<BoardBasisType> boardBasisList,
      final String selectedBoardBasis)
   {
      if (StringUtils.isNotBlank(selectedBoardBasis))
      {
         return selectedBoardBasis;
      }
      for (final BoardBasisType boardBasis : boardBasisList)
      {
         if (boardBasis.isDefaultBoardBasis())
         {
            return boardBasis.getBoardbasisCode();
         }
      }

      return StringUtils.EMPTY;
   }

   /**
    * Addition of new param -children age which will include child age + infant age
    */
   private String appendAirports(final List<AirportData> airports)
   {
      final StringBuilder airportList = new StringBuilder("");
      String pipe = "";

      for (final AirportData airport : airports)
      {
         airportList.append(pipe).append(airport.getId());
         pipe = "|";
      }

      return airportList.toString();
   }

   /**
    * Addition of new param -children age which will include child age + infant age
    */
   private String appendUnits(final List<UnitData> units)
   {
      final StringBuilder whereTo = new StringBuilder("");
      String pipe = "";

      for (final UnitData unit : units)
      {
         whereTo.append(pipe).append(unit.getId()).append(COLON).append(unit.getType());
         pipe = "|";
      }
      return whereTo.toString();
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

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
    * .tui.web.view.data.BookFlowAccommodationViewData,
    * uk.co.portaltech.travel.model.SearchPanelComponentModel,
    * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public String builder(final BookFlowAccommodationViewData source,
      final SearchPanelComponentModel searchPanelComponentModel,
      final SearchResultsRequestData requestData)
   {

      return null;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
    * .tui.web.view.data.SearchResultViewData,
    * uk.co.portaltech.travel.model.SearchPanelComponentModel,
    * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public void builder(final SearchResultViewData resultsview,
      final SearchPanelComponentModel searchPanelComponent,
      final SearchResultsRequestData searchParameter)
   {
      // Do nothing because an overridden method.
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
    * .tui.web.view.data.BookFlowAccommodationViewData,
    * uk.co.portaltech.travel.model.HolidayFinderComponentModel)
    */
   @Override
   public String builder(final BookFlowAccommodationViewData source,
      final HolidayFinderComponentModel holidayFinderComponent)
   {

      return null;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
    * .tui.web.view.data.BookFlowAccommodationViewData,
    * uk.co.portaltech.travel.model.HolidayFinderComponentModel,
    * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public String builder(final BookFlowAccommodationViewData source,
      final HolidayFinderComponentModel holidayFinderComponent,
      final SearchResultsRequestData requestData)
   {

      return null;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.web.url.builders.RequestBuilder#builder(uk.co.portaltech
    * .tui.web.view.data.SearchResultViewData,
    * uk.co.portaltech.travel.model.HolidayFinderComponentModel,
    * uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)
    */
   @Override
   public void builder(final SearchResultViewData resultsview,
      final HolidayFinderComponentModel holidayFinderComponent,
      final SearchResultsRequestData searchParameter)
   {
      // Do nothing because an overridden method.
   }

   private boolean isThirdPartyFlight(final BookFlowAccommodationViewData source)
   {
      return (source.getPackageData().getItinerary().getOutbounds().get(0).getExternalInventory()
         .booleanValue()) || (source.getPackageData().getItinerary().getInbounds().get(0)
         .getExternalInventory().booleanValue());

   }
}
