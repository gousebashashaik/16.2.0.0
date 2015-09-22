/**
 *
 */
package uk.co.portaltech.tui.web.url.builders;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.MerchandisedHoliday;
import uk.co.portaltech.travel.model.results.PaxDetail;
import uk.co.portaltech.travel.services.accommodation.MainstreamAccommodationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.MerchandiserViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.BookFlowAccommadationException;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author shyamaprasada.vs
 *
 */
public class BookFlowAccomPageUrlBuilder
{

   /**
     *
     */
   private static final String INDEX = "index";

   @Resource
   private TUIUrlResolver<ProductModel> bookAccommodationUrlResolver;

   @Resource
   private MainstreamAccommodationService accommodationService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private SessionService sessionService;

   private static final String BOARBBASIS = "ChangedBoardBasis";

   private static final String BB = "bb";

   private static final String EQUALS = "=";

   private static final String AND = "&";

   private static final String FROM = "airports[]";

   private static final String WHERE_TO = "units[]";

   private static final String IS_FLEXIBLE = "flexibility";

   private static final String FLEXIBLE_DAYS = "flexibleDays";

   private static final String DURATION = "duration";

   private static final String ADULTS = "noOfAdults";

   private static final String SENIORS = "noOfSeniors";

   private static final String CHILDREN = "noOfChildren";

   private static final String CHILDREN_AGE = "childrenAge";

   private static final String WHEN = "when";

   private static final String PACKAGE_ID = "packageId";

   private static final String BRAND_TYPE = "brandType";

   private static final String MULTI_SELECT = "multiSelect";

   private static final String DEALS_PAGE = "dp";

   private static final String TRUE = "t";

   private static final String COLON = ":";

   private static final TUILogUtils LOG = new TUILogUtils("BookFlowAccomPageUrlBuilder");

   public void build(final SearchResultsRequestData searchParameter,
      final SearchResultsViewData searchResultsViewData,
      final SearchPanelComponentModel searchPanelComponentModel)
      throws BookFlowAccommadationException
   {
      AccommodationModel accomModel = null;
      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandTypePKS = brandDetails.getRelevantBrands();
      for (final SearchResultViewData result : searchResultsViewData.getHolidays())
      {
         accomModel =
            accommodationService.getAccomodationByCodeAndCatalogVersion(result.getAccommodation()
               .getCode(), cmsSiteService.getCurrentCatalogVersion(), brandTypePKS);
         if (accomModel == null)
         {
            LOG.error("No accommodation model found for " + result.getAccommodation().getCode());
            throw new BookFlowAccommadationException("3006");
         }
         setAccomBookFlowPageUrl(searchParameter, accomModel, result, searchPanelComponentModel);

      }
   }

   public void build(final SearchResultsRequestData searchParameter,
      final SearchResultViewData searchResultViewData, final AccommodationModel accommodationModel,
      final SearchPanelComponentModel searchPanelComponentModel)
      throws BookFlowAccommadationException
   {

      setAccomBookFlowPageUrl(searchParameter, accommodationModel, searchResultViewData,
         searchPanelComponentModel);
   }

   /**
    * created for inventory group -- redirection from deals page to book flow accom page As there is
    * no searchParameter available-- new method had to be created
    *
    * @param merchandiserViewData *
    */
   public void build(final MerchandisedHoliday holiday, final AccommodationModel accom,
      final SearchResultViewData results, final MerchandiserViewData merchandiserViewData)
   {
      setAccomBookFlowPageUrl(holiday, accom, results, merchandiserViewData);
   }

   /**
    * created for inventory group -- redirection from deals page to book flow accom page
    *
    * @param resultsViewData
    */
   private void setAccomBookFlowPageUrl(final MerchandisedHoliday holiday,
      final AccommodationModel accom, final SearchResultViewData results,
      final MerchandiserViewData merchandiserViewData)
   {

      final StringBuilder accomPageUrl = new StringBuilder();

      accomPageUrl.append(bookAccommodationUrlResolver.resolve(accom));

      accomPageUrl.append(ADULTS).append(EQUALS).append(merchandiserViewData.getNoOfAdults())
         .append(AND);
      accomPageUrl.append(CHILDREN).append(EQUALS).append(merchandiserViewData.getNoOfChildren())
         .append(AND);
      accomPageUrl.append(CHILDREN_AGE).append(EQUALS)
         .append(merchandiserViewData.getChildrenAge()).append(AND);
      accomPageUrl.append(DURATION).append(EQUALS).append(holiday.getStayPeriod()).append(AND);
      accomPageUrl.append(FLEXIBLE_DAYS).append(EQUALS).append(StringUtils.EMPTY).append(AND);
      accomPageUrl.append(FROM).append(EQUALS)
         .append(results.getItinerary().getOutbounds().get(0).getDepartureAirportCode())
         .append(AND);
      if ((sessionService.getAttribute("flexibility") != null))
      {
         accomPageUrl.append(IS_FLEXIBLE).append(EQUALS)
            .append((sessionService.getAttribute("flexibility"))).append(AND);
      }
      else
      {
         accomPageUrl.append(IS_FLEXIBLE).append(EQUALS).append(false).append(AND);
      }
      accomPageUrl.append(SENIORS).append(EQUALS).append(merchandiserViewData.getNoOfSeniors())
         .append(AND);
      accomPageUrl.append(WHEN).append(EQUALS)
         .append(results.getItinerary().getOutbounds().get(0).getSchedule().getDepartureDate())
         .append(AND);
      accomPageUrl.append(WHERE_TO).append(EQUALS).append(results.getAccommodation().getCode())
         .append(COLON).append(results.getAccommodation().getAccomType()).append(AND);
      accomPageUrl.append(PACKAGE_ID).append(EQUALS).append(results.getPackageId()).append(AND);
      accomPageUrl.append(MULTI_SELECT).append(EQUALS).append("true").append(AND);
      accomPageUrl.append(INDEX).append(EQUALS).append(results.getIndex());
      if (StringUtils.isNotEmpty(holiday.getDealsPackageInfo().getBrand()))
      {
         accomPageUrl.append(AND).append(BRAND_TYPE).append(EQUALS)
            .append(holiday.getDealsPackageInfo().getBrand());
      }
      accomPageUrl.append(AND).append(DEALS_PAGE).append(EQUALS).append(TRUE);
      results.getAccommodation().setUrl(accomPageUrl.toString());
   }

   /**
    * This method constructs the request url which represents the accommodation details page url in
    * Book Flow
    *
    * @param searchParameter
    * @param accom
    * @param result
    */

   private void setAccomBookFlowPageUrl(final SearchResultsRequestData searchParameter,
      final AccommodationModel accom, final SearchResultViewData result,
      final SearchPanelComponentModel searchPanelComponentModel)
   {

      final StringBuilder accomPageUrl = new StringBuilder();

      accomPageUrl.append(bookAccommodationUrlResolver.resolve(accom));

      accomPageUrl.append(ADULTS).append(EQUALS).append(searchParameter.getNoOfAdults())
         .append(AND);
      accomPageUrl.append(CHILDREN).append(EQUALS).append(searchParameter.getNoOfChildren())
         .append(AND);
      accomPageUrl.append(CHILDREN_AGE).append(EQUALS)
         .append(getChildrenAgeAsString(searchParameter.getChildrenAge())).append(AND);
      accomPageUrl.append(DURATION).append(EQUALS).append(getDuration(result)).append(AND);
      accomPageUrl.append(FLEXIBLE_DAYS).append(EQUALS).append(searchParameter.getFlexibleDays())
         .append(AND);
      if (searchParameter.getAirportList() != null
         && !CollectionUtils.isEmpty(searchParameter.getAirportList()))
      {
         accomPageUrl.append(FROM).append(EQUALS)
            .append(getAirports(searchParameter.getAirportList(), "Rec")).append(AND);
      }
      else
      {
         accomPageUrl.append(FROM).append(EQUALS)
            .append(getAirports(searchParameter.getAirports())).append(AND);
      }

      accomPageUrl.append(IS_FLEXIBLE).append(EQUALS).append(searchParameter.isFlexibility())
         .append(AND);
      accomPageUrl.append(SENIORS).append(EQUALS).append(searchParameter.getNoOfSeniors())
         .append(AND);
      accomPageUrl.append(WHEN).append(EQUALS)
         .append(result.getItinerary().getOutbounds().get(0).getSchedule().getDepartureDate())
         .append(AND);
      if (searchParameter.getUnitsList() != null
         && !CollectionUtils.isEmpty(searchParameter.getUnitsList()))
      {
         accomPageUrl.append(WHERE_TO).append(EQUALS)
            .append(getWhereTo(searchParameter.getUnitsList(), "Rec")).append(AND);
      }
      else
      {
         accomPageUrl.append(WHERE_TO).append(EQUALS)
            .append(getWhereTo(searchParameter.getUnits())).append(AND);
      }

      accomPageUrl.append(PACKAGE_ID).append(EQUALS).append(result.getPackageId()).append(AND);
      accomPageUrl.append(INDEX).append(EQUALS).append(result.getIndex()).append(AND);
      if (CollectionUtils.isNotEmpty(searchParameter.getUnits()))
      {
         accomPageUrl.append(MULTI_SELECT).append(EQUALS)
            .append(String.valueOf(searchParameter.getUnits().get(0).isMultiSelect()));
      }
      else
      {
         // this is done if anywhere search is done
         accomPageUrl.append(MULTI_SELECT).append(EQUALS).append("true");
      }
      accomPageUrl.append(AND).append(BRAND_TYPE).append(EQUALS).append(result.getBrandType())
         .append(setRoomsData(searchParameter, result, searchPanelComponentModel));
      setBoardBasisForURL(accomPageUrl);

      result.getAccommodation().setUrl(accomPageUrl.toString());
   }

   private void setBoardBasisForURL(final StringBuilder accomURL)
   {
      final String bbCode = sessionService.getAttribute(BOARBBASIS);
      if (bbCode != null && !bbCode.isEmpty())
      {
         accomURL.append(AND).append(BB).append(EQUALS).append(bbCode);
      }
   }

   /**
    * @param airportList
    * @return String
    */
   private String getAirports(final List<String> airportList, final String name)
   {
      LOG.debug("getting airports:" + name);
      final StringBuilder airports = new StringBuilder("");
      String pipe = "";

      for (final String airport : airportList)
      {
         airports.append(pipe).append(airport);
         pipe = "|";
      }

      return airports.toString();
   }

   private String getAirports(final List<AirportData> airportsList)
   {
      final StringBuilder airports = new StringBuilder("");
      String pipe = "";

      for (final AirportData airport : airportsList)
      {
         airports.append(pipe).append(airport.getId());
         pipe = "|";
      }

      return airports.toString();
   }

   /**
    * @param searchParameter
    * @return
    */
   @SuppressWarnings("boxing")
   private String setRoomsData(final SearchResultsRequestData searchParameter,
      final SearchResultViewData result, final SearchPanelComponentModel searchPanelComponentModel)
   {

      final StringBuilder room = new StringBuilder("");

      if (CollectionUtils.isNotEmpty(searchParameter.getRooms()))
      {

         room.append(AND).append("room").append(EQUALS);

         for (int i = 0; i < result.getAccommodation().getRooms().size(); i++)
         {

            final StringBuilder party = new StringBuilder();
            for (final PaxDetail pax : result.getAccommodation().getRooms().get(i).getOccupancy()
               .getPaxDetail())
            {
               if (isValidSearchPanelModel(searchPanelComponentModel)
                  && pax.getAge() <= searchPanelComponentModel.getMaxChildAge())
               {
                  party.append(pax.getAge()).append(",");
               }
            }
            final StringBuilder noOfParty =
               new StringBuilder().append(i + 1).append("|")
                  .append(result.getAccommodation().getRooms().get(i).getOccupancy().getAdults())
                  .append("|")
                  .append(result.getAccommodation().getRooms().get(i).getOccupancy().getSeniors())
                  .append("|")
                  .append(result.getAccommodation().getRooms().get(i).getOccupancy().getChildren())
                  .append("|")
                  .append(result.getAccommodation().getRooms().get(i).getOccupancy().getInfant())
                  .append("|").append(party).append("-");
            room.append(noOfParty);

         }
      }
      return room.toString();
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

   private String getChildrenAgeAsString(final List<Integer> childrenAge)
   {
      final StringBuilder ages = new StringBuilder("");
      String comma = "";
      for (final Integer age : childrenAge)
      {
         ages.append(comma).append(age.intValue());
         comma = ",";
      }
      return ages.toString();
   }

   @SuppressWarnings("deprecation")
   private String getWhereTo(final List<UnitData> whereToList)
   {
      final StringBuilder whereTo = new StringBuilder("");
      String pipe = "";

      for (final UnitData unit : whereToList)
      {
         whereTo.append(pipe).append(unit.getId()).append(COLON).append(unit.getType());
         pipe = "|";
      }
      return whereTo.toString();
   }

   /**
    * @param unitsList
    * @return String
    */
   @SuppressWarnings("deprecation")
   private String getWhereTo(final List<String> unitsList, final String rec)
   {
      LOG.debug("get Where To" + rec);
      final StringBuilder whereTo = new StringBuilder("");
      String pipe = "";

      for (final String unit : unitsList)
      {

         whereTo.append(pipe).append(unit);
         pipe = "|";
      }
      return whereTo.toString();
   }

   private String getDuration(final SearchResultViewData result)
   {

      return String.valueOf(result.getDuration());
   }
}
