/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import uk.co.portaltech.travel.enums.BoardBasis;
import uk.co.portaltech.travel.model.FacilityTypeModel;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.travel.services.facility.FacilityTypeService;
import uk.co.portaltech.travel.thirdparty.endeca.FacetOption;
import uk.co.portaltech.travel.thirdparty.endeca.FacetValue;
import uk.co.portaltech.tui.constants.FilterConstants;
import uk.co.portaltech.tui.utils.ConfigurationUtils;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.CommonData;
import uk.co.portaltech.tui.web.view.data.CommonFilterData;
import uk.co.portaltech.tui.web.view.data.DestinationMainData;
import uk.co.portaltech.tui.web.view.data.DestinationOptionData;
import uk.co.portaltech.tui.web.view.data.FilterData;
import uk.co.portaltech.tui.web.view.data.FilterPanel;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.portaltech.tui.web.view.data.SliderData;
import uk.co.portaltech.tui.web.view.data.SliderMainData;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author EXTCS5
 *
 */
@SuppressWarnings("boxing")
public class FilterSelectorPopulator implements Populator<EndecaSearchResult, SearchResultsViewData>
{

   private static final String UNDER_SCORE = "_";

   private static final String REG_EXP = "\\|";

   private static final String DO = "DO_";

   private static final String DH = "DH_";

   private static final String AND = "AND";

   private final TUILogUtils log = new TUILogUtils("FilterSelectorPopulator");

   @Resource(name = "facilityType")
   private FacilityTypeService facilityTypeService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource(name = "mainStreamTravelLocationService")
   private MainStreamTravelLocationService mstravelLocationService;

   @Resource
   private AirportService airportService;

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private ConfigurationUtils configurationUtils;

   @Resource
   private ViewSelector viewSelector;

   private static final int HOLIDAY_TYPE_NUM = 3;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    *
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final EndecaSearchResult source, final SearchResultsViewData target) throws ConversionException
   {

      final Map<String, Boolean> visibility = new HashMap<String, Boolean>();
      // this is done as population of visibility is during population of
      // rating.
      visibility.put(FilterConstants.FCRATINGFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.FALSE);

      final List<FacetOption> facets = source.getFacets();

      final FilterPanel filterPanel = new FilterPanel();
      target.setFilterPanel(filterPanel);
      final String price = target.getFilterPanel().getPriceView();
      if (price != null && !price.isEmpty())
      {
         filterPanel.setPriceView(price);
      }
      else
      {
         filterPanel.setPriceView("pp");

      }

      // General options
      final List generalOptionsFilter = new ArrayList();
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      final FilterData generalOptions = new FilterData();
      generalOptions.setName(FilterConstants.GENERAL_OPTIONS);
      generalOptions.setFilters(generalOptionsFilter);

      filterPanel.setRepaint(source.isRepaintFilter());

      // Accommodation option filters
      final List accommodationFilter = new ArrayList();
      final FilterData accommodation = new FilterData();
      accommodation.setName(FilterConstants.ACCOMMODATION_OPTIONS);
      accommodation.setFilters(accommodationFilter);

      final List<SliderData> ratingFilters = new ArrayList<SliderData>();

      final SliderMainData rating = new SliderMainData();
      rating.setName(FilterConstants.RATINGS);
      rating.setFilters(ratingFilters);
      filterPanel.setRating(rating);

      final List destinationFilters = new ArrayList();
      final DestinationOptionData destinationOptions = new DestinationOptionData();
      destinationOptions.setName(FilterConstants.DESTINATION_OPTIONS);
      destinationOptions.setFilters(destinationFilters);
      filterPanel.setDestinationOptions(destinationOptions);

      // FlightOptions
      final List departureFilterData = new ArrayList();
      final FilterData flightOption = new FilterData();
      flightOption.setName(FilterConstants.FLIGHT_OPTIONS);
      flightOption.setFilters(departureFilterData);

      // Temperature options
      final List<SliderData> temperatureFilters = new ArrayList<SliderData>();

      final SliderMainData temperatureOptions = new SliderMainData();
      temperatureOptions.setName(FilterConstants.TEMPERATURE);
      temperatureOptions.setFilters(temperatureFilters);
      filterPanel.setTemperatureOptions(temperatureOptions);

      // BUdget options

      final List<SliderData> budgetTotalFilters = new ArrayList<SliderData>();
      final List<SliderData> budgetPPFilters = new ArrayList<SliderData>();

      final SliderMainData budgetOptions = new SliderMainData();
      budgetOptions.setName(FilterConstants.BUDGET_TOTAL);
      budgetOptions.setFilters(budgetTotalFilters);
      filterPanel.setTotalbudget(budgetOptions);

      final SliderMainData budgetPPOptions = new SliderMainData();
      budgetPPOptions.setName(FilterConstants.BUDGET_PP);
      budgetPPOptions.setFilters(budgetPPFilters);
      filterPanel.setPpbudget(budgetPPOptions);

      // Add Best For features
      final List<CommonData> bestForFilters = new ArrayList<CommonData>();
      final CommonFilterData bestFor = new CommonFilterData();
      bestFor.setName(FilterConstants.BESTFOR_NAME);
      bestFor.setType(FilterConstants.CHECKBOX);
      bestFor.setId(FilterConstants.BESTFOR_ID);
      bestFor.setFilterType(AND);
      bestFor.setValues(bestForFilters);

      // Accomm Type Adding --start
      final List<CommonData> accommTypeFilters = new ArrayList<CommonData>();
      final CommonFilterData accommType = new CommonFilterData();
      accommType.setName(FilterConstants.ACCOMM_TYPE_NAME);
      accommType.setType(FilterConstants.CHECKBOX);
      accommType.setId(FilterConstants.ACCOMM_TYPE_ID);
      accommType.setFilterType("OR");
      accommType.setValues(accommTypeFilters);

      // Accomm Type Adding --End

      // Add all features

      final List<CommonData> featureFilters = new ArrayList<CommonData>();

      final CommonFilterData featureData = new CommonFilterData();
      featureData.setName(FilterConstants.FEATURES);
      featureData.setType(FilterConstants.CHECKBOX);
      featureData.setId(FilterConstants.FEATURES_ID);
      featureData.setFilterType(AND);
      featureData.setValues(featureFilters);

      if (facetEnabled(FilterConstants.NOOFROOMFILTER))
      {
         visibility.put(FilterConstants.NOOFROOMFILTER, Boolean.TRUE);
      }
      // Add HolidayType

      CommonFilterData outgoingTime = null;
      CommonFilterData incomingTime = null;
      CommonFilterData airport = null;
      for (final FacetOption facet : facets)
      {
         if (facetValueNotEmpty(FilterConstants.TRATING_MIN_CODE, facet))
         {
            // While enabling this block, we need to check whether trip
            // advisor
            // rating is been enabled, if so
            // visibility map has to be updated with the appropriate value.
            // [This comment is been considered and coded]
            populateTRatingIfEnabled(visibility, ratingFilters, facet);

         }
         else if (facetValueNotEmpty(FilterConstants.TARATING_CODE, facet))
         {
            populateTRatingIfEnabled(visibility, ratingFilters, facet);
         }

         if (facetValueNotEmpty(FilterConstants.FCRATING_CODE, facet))
         {
            populateRatingIfEnabled(visibility, ratingFilters, facet);

         }
         else if (facetValueNotEmpty(FilterConstants.TEMPERATURE_CODE, facet))
         {
            populateTemperature(visibility, temperatureFilters, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.BUDGET_TOTAL_CODE, facet))
         {
            populateTotalBudget(visibility, budgetTotalFilters, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.BUDGET_PP_CODE, facet))
         {
            populatePPBudget(visibility, budgetPPFilters, facet);
         }

         else if (featureMatches(facet) && CollectionUtils.isNotEmpty(facet.getFacetValues()))
         {
            populateFeatures(visibility, featureFilters, facet);

         }
         else if (facetValueNotEmpty(FilterConstants.DESTINATION_CODE, facet))
         {
            populateDestinationOptions(visibility, filterPanel, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.ACCOMM_TYPE_CODE, facet))
         {
            populateAccommodationTypes(visibility, accommTypeFilters, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.DEPARTURE_CODE, facet))
         {
            airport = populateDepAirport(visibility, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.OUTBOUND_CODE, facet))
         {
            outgoingTime = populateOutGoingTime(visibility, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.INBOUND_CODE, facet))
         {
            incomingTime = populateIncomingTime(visibility, facet);
         }

         // General Filter Sections

         else if (facetValueNotEmpty(FilterConstants.BOARDBASIS_CODE, facet))
         {
            populateBoardBasis(visibility, generalOptionsFilter, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.HOLIDAY_OPERATOR_ID, facet))
         {
            populateHolidayOperator(visibility, generalOptionsFilter, facet, source.getBrandCode());
         }
         else if (facetValueNotEmpty(FilterConstants.HOLIDAY_TYPE_ID, facet))
         {
            populateHolidayTypeFilter(visibility, generalOptionsFilter, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.COLLECTION_CODE, facet))
         {
            populateCollectionFilters(visibility, generalOptionsFilter, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.BESTFOR_CODE, facet))
         {
            populateBestFor(visibility, bestForFilters, facet);
         }

      }

      generalOptionsFilter.removeAll(Collections.singletonList(null));
      updateGeneralOptions(filterPanel, generalOptionsFilter, generalOptions, bestFor);

      // If flight options' filters are not empty
      updateDepartureFilterData(departureFilterData, outgoingTime, incomingTime, airport);
      updateFlightOptions(filterPanel, departureFilterData, flightOption);
      // Accomm Type added--start
      updateAccommodationOptions(filterPanel, accommodationFilter, accommodation, featureData, bestFor, accommType);
      filterPanel.setFilterVisibility(visibility);

   }

   public void populateMobileFilterData(final EndecaSearchResult source, final SearchResultsViewData target)
         throws ConversionException
   {

      final Map<String, Boolean> visibility = new HashMap<String, Boolean>();
      // this is done as population of visibility is during population of
      // rating.
      visibility.put(FilterConstants.FCRATINGFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.FALSE);

      final List<FacetOption> facets = source.getFacets();

      final FilterPanel filterPanel = new FilterPanel();
      target.setFilterPanel(filterPanel);

      // General options
      final List generalOptionsFilter = new ArrayList();
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      generalOptionsFilter.add(null);
      final FilterData generalOptions = new FilterData();
      generalOptions.setName(FilterConstants.GENERAL_OPTIONS);
      generalOptions.setFilters(generalOptionsFilter);

      filterPanel.setRepaint(source.isRepaintFilter());

      // Accommodation option filters
      final List accommodationFilter = new ArrayList();
      final FilterData accommodation = new FilterData();
      accommodation.setName(FilterConstants.ACCOMMODATION_OPTIONS);
      accommodation.setFilters(accommodationFilter);

      final List<SliderData> ratingFilters = new ArrayList<SliderData>();

      final SliderMainData rating = new SliderMainData();
      rating.setName(FilterConstants.RATINGS);
      rating.setFilters(ratingFilters);
      filterPanel.setRating(rating);

      // FlightOptions
      final List departureFilterData = new ArrayList();
      final FilterData flightOption = new FilterData();
      flightOption.setName(FilterConstants.FLIGHT_OPTIONS);
      flightOption.setFilters(departureFilterData);

      // BUdget options

      final List<SliderData> budgetTotalFilters = new ArrayList<SliderData>();
      final List<SliderData> budgetPPFilters = new ArrayList<SliderData>();

      final SliderMainData budgetOptions = new SliderMainData();
      budgetOptions.setName(FilterConstants.BUDGET_TOTAL);
      budgetOptions.setFilters(budgetTotalFilters);
      filterPanel.setTotalbudget(budgetOptions);

      final SliderMainData budgetPPOptions = new SliderMainData();
      budgetPPOptions.setName(FilterConstants.BUDGET_PP);
      budgetPPOptions.setFilters(budgetPPFilters);
      filterPanel.setPpbudget(budgetPPOptions);

      // Accomm Type Adding --start
      final List<CommonData> accommTypeFilters = new ArrayList<CommonData>();
      final CommonFilterData accommType = new CommonFilterData();
      accommType.setName(FilterConstants.ACCOMM_TYPE_NAME);
      accommType.setType(FilterConstants.CHECKBOX);
      accommType.setId(FilterConstants.ACCOMM_TYPE_ID);
      accommType.setFilterType("OR");
      accommType.setValues(accommTypeFilters);

      // Accomm Type Adding --End

      // Add all features

      final List<CommonData> featureFilters = new ArrayList<CommonData>();

      final CommonFilterData featureData = new CommonFilterData();
      featureData.setName(FilterConstants.FEATURES);
      featureData.setType(FilterConstants.CHECKBOX);
      featureData.setId(FilterConstants.FEATURES_ID);
      featureData.setFilterType(AND);
      featureData.setValues(featureFilters);

      // Add Best For features
      final List<CommonData> bestForFilters = new ArrayList<CommonData>();
      final CommonFilterData bestFor = new CommonFilterData();
      bestFor.setName(FilterConstants.BESTFOR_NAME);
      bestFor.setType(FilterConstants.CHECKBOX);
      bestFor.setId(FilterConstants.BESTFOR_ID);
      bestFor.setFilterType(AND);
      bestFor.setValues(bestForFilters);

      CommonFilterData outgoingTime = null;
      CommonFilterData incomingTime = null;
      CommonFilterData airport = null;
      for (final FacetOption facet : facets)
      {
         if (facetValueNotEmpty(FilterConstants.BUDGET_TOTAL_CODE, facet))
         {
            populateTotalBudget(visibility, budgetTotalFilters, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.BUDGET_PP_CODE, facet))
         {
            populatePPBudget(visibility, budgetPPFilters, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.DESTINATION_CODE, facet))
         {
            populateDestinationOptions(visibility, filterPanel, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.ACCOMM_TYPE_CODE, facet))
         {
            populateAccommodationTypes(visibility, accommTypeFilters, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.BESTFOR_CODE, facet))
         {
            populateBestFor(visibility, bestForFilters, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.COLLECTION_CODE, facet))
         {
            populateCollectionFilters(visibility, generalOptionsFilter, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.BOARDBASIS_CODE, facet))
         {
            populateBoardBasis(visibility, generalOptionsFilter, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.DEPARTURE_CODE, facet))
         {
            airport = populateDepAirport(visibility, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.OUTBOUND_CODE, facet))
         {
            outgoingTime = populateOutGoingTime(visibility, facet);
         }

         else if (facetValueNotEmpty(FilterConstants.INBOUND_CODE, facet))
         {
            incomingTime = populateIncomingTime(visibility, facet);
         }
         else if (facetValueNotEmpty(FilterConstants.TRATING_MIN_CODE, facet))
         {
            // While enabling this block, we need to check whether trip
            // advisor
            // rating is been enabled, if so
            // visibility map has to be updated with the appropriate value.
            // [This comment is been considered and coded]
            populateTRatingIfEnabled(visibility, ratingFilters, facet);

         }
         else if (facetValueNotEmpty(FilterConstants.TARATING_CODE, facet))
         {
            populateTRatingIfEnabled(visibility, ratingFilters, facet);
         }

         if (facetValueNotEmpty(FilterConstants.FCRATING_CODE, facet))
         {
            populateRatingIfEnabled(visibility, ratingFilters, facet);

         }

      }

      generalOptionsFilter.removeAll(Collections.singletonList(null));
      updateGeneralOptions(filterPanel, generalOptionsFilter, generalOptions, bestFor);
      updateDepartureFilterData(departureFilterData, outgoingTime, incomingTime, airport);
      updateFlightOptions(filterPanel, departureFilterData, flightOption);
      updateAccommodationOptions(filterPanel, accommodationFilter, accommodation, featureData, bestFor, accommType);
      filterPanel.setFilterVisibility(visibility);

   }

   /**
    * @param visibility
    * @param ratingFilters
    * @param facet
    */
   private void populateTRatingIfEnabled(final Map<String, Boolean> visibility, final List<SliderData> ratingFilters,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.TRIPADVISORRATINGFILTER))
      {
         visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.TRUE);
         getTripAdvisorRating(ratingFilters, facet, visibility);
      }
   }

   /**
    * @param filterPanel
    * @param generalOptionsFilter
    * @param generalOptions
    */
   private void updateGeneralOptions(final FilterPanel filterPanel, final List generalOptionsFilter,
         final FilterData generalOptions, final CommonFilterData bestFor)
   {
      if (CollectionUtils.isNotEmpty(bestFor.getValues()))
      {
         generalOptionsFilter.add(bestFor);
      }

      // If accommodation options' filters are not empty
      if (CollectionUtils.isNotEmpty(generalOptionsFilter))
      {
         filterPanel.setGeneralOptions(generalOptions);
      }
   }

   /**
    * @param filterPanel
    * @param departureFilterData
    * @param flightOption
    */
   private void updateFlightOptions(final FilterPanel filterPanel, final List departureFilterData, final FilterData flightOption)
   {
      if (CollectionUtils.isNotEmpty(departureFilterData))
      {
         filterPanel.setFlightOptions(flightOption);
      }
   }

   /**
    * @param filterPanel
    * @param accommodationFilter
    * @param accommodation
    */
   private void updateAccommodationOptions(final FilterPanel filterPanel, final List accommodationFilter,
         final FilterData accommodation, final CommonFilterData featureData, final CommonFilterData bestFor,
         final CommonFilterData accommTypeFilter)
   {
      // If Bestfor are not empty add it to Accommodation option
      if (viewSelector.checkIsMobile() && CollectionUtils.isNotEmpty(bestFor.getValues()))
      {
         accommodationFilter.add(bestFor);
      }

      // Accomm Type --Start
      if (CollectionUtils.isNotEmpty(accommTypeFilter.getValues()))
      {
         accommodationFilter.add(accommTypeFilter);
      }

      // If Features are not empty add it to Accommodation option
      if (CollectionUtils.isNotEmpty(featureData.getValues()))
      {
         accommodationFilter.add(featureData);
      }

      // Accomm Type --End
      // If accommodation options' filters are not empty
      if (CollectionUtils.isNotEmpty(accommodationFilter))
      {
         filterPanel.setAccommodationOptions(accommodation);
      }
   }

   /**
    * @param departureFilterData
    * @param outgoingTime
    * @param incomingTime
    * @param airport
    */
   private void updateDepartureFilterData(final List departureFilterData, final CommonFilterData outgoingTime,
         final CommonFilterData incomingTime, final CommonFilterData airport)
   {
      if (airport != null)
      {
         departureFilterData.add(airport);
      }
      if (outgoingTime != null)
      {
         departureFilterData.add(outgoingTime);
      }
      if (incomingTime != null)
      {
         departureFilterData.add(incomingTime);
      }
   }

   /**
    * @param facet
    * @return true / false
    */
   private boolean featureMatches(final FacetOption facet)
   {
      return facetOptCheck(facet) || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000S")
            || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000E");
   }

   private boolean facetOptCheck(final FacetOption facet)
   {
      return StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000H")
            || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000O")
            || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000P");
   }

   /**
    * @param visibility
    * @param facet
    */
   private CommonFilterData populateIncomingTime(final Map<String, Boolean> visibility, final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.DEPCOMINGBACKFILTER))
      {
         visibility.put(FilterConstants.DEPCOMINGBACKFILTER, Boolean.TRUE);
         return getDepartureTimings(facet, FilterConstants.INBOUND_TEXT, FilterConstants.INBOUND_INDICATOR);
      }
      return null;
   }

   /**
    * @param visibility
    * @param facet
    */
   private CommonFilterData populateOutGoingTime(final Map<String, Boolean> visibility, final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.DEPGOINGOUTFILTER))
      {
         visibility.put(FilterConstants.DEPGOINGOUTFILTER, Boolean.TRUE);
         return getDepartureTimings(facet, FilterConstants.OUTBOUNND_TEXT, FilterConstants.OUTBOUNND_INDICATOR);
      }
      return null;
   }

   /**
    * @param visibility
    * @param facet
    */
   private CommonFilterData populateDepAirport(final Map<String, Boolean> visibility, final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.DEPAIRPORTFILTER))
      {
         visibility.put(FilterConstants.DEPAIRPORTFILTER, Boolean.TRUE);
         return getDeparturePoints(facet);
      }
      return null;
   }

   // Accomm Type--Start
   /**
    * @param visibility
    * @param acommodationTypeFilters
    * @param facet
    * @description Checks whether accommodation type facet enabled or not. If facet is enabled then it will populate
    *              accommodation type
    */
   private void populateAccommodationTypes(final Map<String, Boolean> visibility, final List acommodationTypeFilters,
         final FacetOption facet)
   {

      visibility.put(FilterConstants.ACCOM_TYPE_FILTER, Boolean.TRUE);
      getAccommodationTypes(acommodationTypeFilters, facet);
   }

   // Accomm Type--End
   /**
    * @param visibility
    * @param bestForFilters
    * @param facet
    */
   private void populateBestFor(final Map<String, Boolean> visibility, final List bestForFilters, final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.BESTFORFILTER))
      {
         visibility.put(FilterConstants.BESTFORFILTER, Boolean.TRUE);
         getBestFor(bestForFilters, facet);
      }
   }

   /**
    * @param visibility
    * @param generalOptionsFilter
    * @param facet
    */
   private void populateBoardBasis(final Map<String, Boolean> visibility, final List generalOptionsFilter, final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.BOARDBASISFILTER))
      {
         visibility.put(FilterConstants.BOARDBASISFILTER, Boolean.TRUE);
         getBoardBasis(generalOptionsFilter, facet);
      }
   }

   /**
    * @param visibility
    * @param filterPanel
    * @param facet
    */
   private void populateDestinationOptions(final Map<String, Boolean> visibility, final FilterPanel filterPanel,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.DESTOPTIONSFILTER))
      {
         visibility.put(FilterConstants.DESTOPTIONSFILTER, Boolean.TRUE);
         getDestinationOption(filterPanel, facet);
      }
   }

   /**
    * @param visibility
    * @param featureFilters
    * @param facet
    */
   private void populateFeatures(final Map<String, Boolean> visibility, final List<CommonData> featureFilters,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.FEATURESFILTER))
      {
         visibility.put(FilterConstants.FEATURESFILTER, Boolean.TRUE);
         getFeatures(featureFilters, facet);
      }
   }

   /**
    * @param visibility
    * @param budgetPPFilters
    * @param facet
    */
   private void populatePPBudget(final Map<String, Boolean> visibility, final List<SliderData> budgetPPFilters,
         final FacetOption facet)
   {
      if (visibility.containsKey(FilterConstants.BUDGETFILTER))
      {
         if (visibility.get(FilterConstants.BUDGETFILTER).booleanValue())
         {
            getBudget(budgetPPFilters, facet, "budgetpp");
         }
      }
      else
      {
         if (facetEnabled(FilterConstants.BUDGETFILTER))
         {
            visibility.put(FilterConstants.BUDGETFILTER, Boolean.TRUE);
            getBudget(budgetPPFilters, facet, "budgetpp");
         }

      }
   }

   /**
    * @param visibility
    * @param budgetTotalFilters
    * @param facet
    */
   private void populateTotalBudget(final Map<String, Boolean> visibility, final List<SliderData> budgetTotalFilters,
         final FacetOption facet)
   {
      if (visibility.containsKey(FilterConstants.BUDGETFILTER))
      {
         if (visibility.get(FilterConstants.BUDGETFILTER).booleanValue())
         {
            getBudget(budgetTotalFilters, facet, "budgettotal");
         }
      }
      else
      {
         if (facetEnabled(FilterConstants.BUDGETFILTER))
         {
            visibility.put(FilterConstants.BUDGETFILTER, Boolean.TRUE);
            getBudget(budgetTotalFilters, facet, "budgettotal");
         }

      }

   }

   /**
    * @param visibility
    * @param temperatureFilters
    * @param facet
    */
   private void populateTemperature(final Map<String, Boolean> visibility, final List<SliderData> temperatureFilters,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.TEMPERATUREFILTER))
      {
         visibility.put(FilterConstants.TEMPERATUREFILTER, Boolean.TRUE);
         getTemperature(temperatureFilters, facet, visibility);
      }
   }

   /**
    * @param visibility
    * @param ratingFilters
    * @param facet
    */
   private void populateRatingIfEnabled(final Map<String, Boolean> visibility, final List<SliderData> ratingFilters,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.FCRATINGFILTER))
      {
         visibility.put(FilterConstants.FCRATINGFILTER, Boolean.TRUE);
         getFCRating(ratingFilters, facet, visibility);
      }
   }

   /**
    *
    * @param facetName
    * @param facet
    * @return true / false
    */
   private boolean facetValueNotEmpty(final String facetName, final FacetOption facet)
   {
      return facetName.equals(facet.getFacetOptionName()) && CollectionUtils.isNotEmpty(facet.getFacetValues());
   }

   /**
    * Get Product range(Holiday type)
    *
    * @param generalOptionsFilter
    * @param facet
    */
   private void getHolidayType(final List generalOptionsFilter, final FacetOption facet)
   {
      final List<CommonData> holidayFilters = new ArrayList<CommonData>();

      final CommonFilterData holidaytype = new CommonFilterData();
      holidaytype.setName(FilterConstants.HOLIDAYTYPENAME);
      holidaytype.setId(FilterConstants.HOLIDAYTYPEID);
      holidaytype.setValues(holidayFilters);
      holidaytype.setType(FilterConstants.CHECKBOX);
      holidaytype.setValues(holidayFilters);
      holidaytype.setFilterType("OR");

      generalOptionsFilter.add(HOLIDAY_TYPE_NUM, holidaytype);
      CommonData filterValue = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         filterValue = new CommonData();
         filterValue.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
         filterValue.setId("HT_" + HtmlUtils.htmlEscape(facetValue.getLabel()));
         if ("Twin Centre".equalsIgnoreCase(facetValue.getLabel()))
         {
            filterValue.setValue(HtmlUtils.htmlEscape("Twin_MultiCentre"));
         }
         else
         {
            filterValue.setValue(HtmlUtils.htmlEscape(facetValue.getLabel()));
         }
         filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
         holidayFilters.add(filterValue);
      }

   }

   /**
    * Get Board Basis
    *
    * @param generalOptionsFilter
    * @param facet
    */
   private void getBoardBasis(final List generalOptionsFilter, final FacetOption facet)
   {
      final List<CommonData> boardBasisFilter = new ArrayList<CommonData>();

      final CommonFilterData boardBasis = new CommonFilterData();
      boardBasis.setName(FilterConstants.BOARDBASISNAME);
      boardBasis.setId(FilterConstants.BOARDBASISID);
      boardBasis.setValues(boardBasisFilter);
      boardBasis.setType(FilterConstants.CHECKBOX);
      boardBasis.setValues(boardBasisFilter);
      boardBasis.setFilterType("OR");
      generalOptionsFilter.add(0, boardBasis);
      CommonData filterValue = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         if (facetValue.getCode().length() == TWO)
         {
            filterValue = new CommonData();
            filterValue.setName(BoardBasis.valueOf(facetValue.getCode()).getValue());
            filterValue.setId(facetValue.getCode());
            filterValue.setValue(facetValue.getCode());
            filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            filterValue.setTooltip(configurationUtils.getBoardBasisToolTip(facetValue.getCode()));
            boardBasisFilter.add(filterValue);
         }
      }

   }

   /**
    * Get FC rating
    *
    * @param ratingFilters
    * @param facet
    */
   private void getFCRating(final List<SliderData> ratingFilters, final FacetOption facet, final Map<String, Boolean> visibility)
   {
      final List<FacetValue> facetValues = facet.getFacetValues();
      if (checkFacetValues(facetValues))
      {
         final SliderData minimumFc = new SliderData();

         minimumFc.setId(FilterConstants.FCRATINGID);
         minimumFc.setName(FilterConstants.FCRATINGNAME);
         minimumFc.setType(FilterConstants.SLIDER);
         minimumFc.setTrackType("minRange");
         minimumFc.setCode(facet.getFacetCode());
         final List<Double> values = new ArrayList<Double>();
         final List<Double> limit = new ArrayList<Double>();
         Double min = null;
         Double max = null;
         // fixed bug 5268
         min = Math.floor(Double.parseDouble(facetValues.get(0).getMin()));
         max = Math.floor(Double.parseDouble(facetValues.get(0).getMax()));

         // For single value slider only minimum value is required

         values.add(min);
         limit.add(min);
         limit.add(max);
         minimumFc.setLimit(limit);
         minimumFc.setValues(values);

         final List<Long> range1 = new ArrayList<Long>();
         range1.add(Long.valueOf(FilterConstants.MINRANGE));

         range1.add(Long.valueOf(FilterConstants.MAXRANGE));
         minimumFc.setSteps(FilterConstants.RATINGSTEPS);
         ratingFilters.add(minimumFc);
         minimumFc.setRange(range1);
      }
      else
      {
         // this is set to false because data is not there
         visibility.put(FilterConstants.FCRATINGFILTER, false);
      }

   }

   /**
    * Get temperature facet
    *
    * @param ratingFilters
    * @param facet
    */
   private void getTemperature(final List<SliderData> ratingFilters, final FacetOption facet,
         final Map<String, Boolean> visibility)
   {
      final List<FacetValue> facetValues = facet.getFacetValues();
      if (checkFacetValues(facetValues))
      {
         final SliderData temerature = new SliderData();

         temerature.setId(FilterConstants.TEMPERATUREID);
         temerature.setName(FilterConstants.TEMPERATURENAME);
         temerature.setType(FilterConstants.SLIDER);
         temerature.setCode(facet.getFacetCode());
         final List<Double> values = new ArrayList<Double>();
         Double min = null;
         Double max = null;

         min = Double.parseDouble(facet.getFacetValues().get(0).getMin());
         max = Double.parseDouble(facet.getFacetValues().get(0).getMax());

         values.add(min);
         values.add(max);
         temerature.setValues(values);

         temerature.setSteps(FilterConstants.TEMPRATINGSTEPS);
         ratingFilters.add(temerature);
         final List<Long> range1 = new ArrayList<Long>();
         range1.add(Long.valueOf(FilterConstants.TEMPMINRANGE));
         range1.add(Long.valueOf(FilterConstants.TEMPMAXRANGE));
         temerature.setRange(range1);
      }
      else
      {
         visibility.put(FilterConstants.TEMPERATUREFILTER, Boolean.TRUE);
      }

   }

   /**
    * Get departure timings
    *
    * @param facet
    * @param name
    * @param indicator
    */
   private CommonFilterData getDepartureTimings(final FacetOption facet, final String name, final String indicator)
   {
      final List<CommonData> departureList = new ArrayList<CommonData>();
      final CommonFilterData departureTime = new CommonFilterData();
      departureTime.setName(name);
      departureTime.setId(facet.getFacetCode());
      departureTime.setType(FilterConstants.CHECKBOX);
      departureTime.setFilterType(AND);
      departureTime.setValues(departureList);
      CommonData slot = null;
      final Map<String, String> departureDescription = getDepartureTime();
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         slot = new CommonData();
         slot.setId(facetValue.getValue() + indicator);
         slot.setValue(facetValue.getValue() + indicator);
         slot.setName(departureDescription.get(facetValue.getValue()));
         departureList.add(slot);
      }
      return departureTime;
   }

   /**
    * Get departure points
    *
    * @param facet
    */
   private CommonFilterData getDeparturePoints(final FacetOption facet)
   {
      final List<CommonData> departurePoints = new ArrayList<CommonData>();
      final CommonFilterData departure = new CommonFilterData();
      departure.setName(FilterConstants.DEPARTURE_POINTS);
      departure.setId(facet.getFacetCode());
      departure.setType(FilterConstants.CHECKBOX);
      departure.setFilterType("OR");
      departure.setValues(departurePoints);
      CommonData depPt = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         depPt = new CommonData();
         depPt.setId(facetValue.getValue());
         depPt.setValue(facetValue.getValue());
         depPt.setName(airportService.getAirportName(cmsSiteService.getCurrentCatalogVersion(), facetValue.getValue()));
         departurePoints.add(depPt);
      }
      return departure;
   }

   /**
    * Get Best For options
    *
    * @param bestForFilters
    * @param facet
    */
   private void getBestFor(final List bestForFilters, final FacetOption facet)
   {
      final Map bestForMap = getBestForMap();
      CommonData best = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         best = new CommonData();
         best.setId("BF_" + facetValue.getCode());
         best.setValue(facetValue.getCode());
         if (null != bestForMap.get(facetValue.getCode()))
         {
            best.setName(bestForMap.get(facetValue.getCode()).toString());
            best.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            bestForFilters.add(best);
         }

      }

   }

   // Accomm Type --Start

   /**
    * Get Best For options
    *
    * @param accommodationFilters
    * @param facet
    * @description This method takes the data from FacetsOption and set it to the CommonData & and each common data
    *              object is added to accommodation filter list.
    */
   private void getAccommodationTypes(final List accommodationFilters, final FacetOption facet)
   {
      CommonData accomm = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         accomm = new CommonData();
         accomm.setId("AT_" + facetValue.getLabel());
         accomm.setValue(facetValue.getLabel());
         accomm.setName(facetValue.getLabel());
         accomm.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
         accommodationFilters.add(accomm);
      }

   }

   // Accomm Type --End

   /**
    * Get all features
    *
    * @param featureFilters
    * @param facet
    */
   private void getFeatures(final List featureFilters, final FacetOption facet)
   {

      CommonData feature = null;
      FacilityTypeModel facilityTypeModel = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         facilityTypeModel = facilityTypeService.getFacilityTypeForCode(cmsSiteService.getCurrentCatalogVersion(),
               facetValue.getCode());
         feature = new CommonData();
         feature.setId("FT_" + facetValue.getCode());
         feature.setValue(facetValue.getCode());
         feature.setName(facilityTypeModel != null ? facilityTypeModel.getName() : facetValue.getCode());
         feature.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
         feature.setCategoryCode(facet.getFacetCode());
         if (facilityTypeModel != null)
         {
            featureFilters.add(feature);
         }

      }

   }

   private String roundingPriceValue(final String s)
   {
      if (s != null && s.length() > 0)
      {
         final BigDecimal bd = new BigDecimal(s).setScale(0, RoundingMode.CEILING);
         return bd.toString();
      }
      else
      {
         return "";
      }
   }

   private void getBudget(final List<SliderData> budgetTotalFilters, final FacetOption facet, final String budget)
   {
      final SliderData budgetTotal = new SliderData();

      budgetTotal.setId(budget);
      budgetTotal.setName(budget);
      budgetTotal.setType(FilterConstants.SLIDER);
      budgetTotal.setCode(facet.getFacetCode());
      budgetTotal.setTrackType("maxRange");
      final List<Double> values = new ArrayList<Double>();
      final List<Double> limit = new ArrayList<Double>();
      Double min = null;
      Double max = null;
      if (CollectionUtils.isNotEmpty(facet.getFacetValues()))
      {
         min = Double.parseDouble(roundingPriceValue(facet.getFacetValues().get(0).getMin()));
         max = Double.parseDouble(roundingPriceValue(facet.getFacetValues().get(0).getMax()));
      }

      values.add(max);
      budgetTotal.setValues(values);

      limit.add(min);
      limit.add(max);
      budgetTotal.setLimit(limit);
      budgetTotal.setSteps(FilterConstants.BUDGETSTEPS);
      if (viewSelector.checkIsMobile())
      {
         budgetTotal.setSteps(FilterConstants.BUDGETSTEPSFORMOBILE);
      }
      budgetTotalFilters.add(budgetTotal);
      final List<Double> range1 = new ArrayList<Double>();
      range1.add(min);
      range1.add(max);
      budgetTotal.setRange(range1);
      if (max != null)
      {
         budgetTotal.setMaxValue(max.toString());
      }

   }

   /**
    * Get destination options
    *
    * @param filterPanel
    * @param facet
    */
   private void getDestinationOption(final FilterPanel filterPanel, final FacetOption facet)
   {
      final List<CommonFilterData> commonFilterList = new ArrayList<CommonFilterData>();

      final DestinationOptionData destinationsOptions = new DestinationOptionData();
      destinationsOptions.setName(FilterConstants.DESTINATION_OPTIONS);
      destinationsOptions.setFilters(commonFilterList);
      filterPanel.setDestinationOptions(destinationsOptions);
      final CommonFilterData commonFilterData = new CommonFilterData();
      commonFilterData.setName(FilterConstants.DESTINATIONS);
      commonFilterData.setId(FilterConstants.DESTINATION_ID);
      commonFilterData.setType(FilterConstants.CHECKBOX);
      commonFilterData.setFilterType("OR");
      commonFilterList.add(commonFilterData);
      final List<DestinationMainData> destMainList = createDestinationList(facet);
      commonFilterData.setValues(destMainList);
   }

   /**
    * Creating destination list in hierarchical structure
    *
    * @param facet
    * @return destMainList
    */
   private List<DestinationMainData> createDestinationList(final FacetOption facet)
   {
      // Creating hierarchical destination option structure
      final Map<DestinationMainData, ArrayList<DestinationMainData>> destinationMap = new HashMap<DestinationMainData, ArrayList<DestinationMainData>>();
      DestinationMainData destination = null;
      DestinationMainData parent = null;
      String code = null;
      // incrementior i used to generate unique ids for UI

      if (CollectionUtils.isNotEmpty(facet.getFacetValues()))
      {
         // commenting to resolve DE39422

         for (final FacetValue facetValue : facet.getFacetValues())
         {
            parent = new DestinationMainData();
            parent.setId((facetValue.getParentCode() != null) ? DO + facetValue.getParentCode() : DO);
            parent.setValue(facetValue.getParentCode());
            parent.setName((facetValue.getParentCode() != null) ? mstravelLocationService.getDestinationName(facetValue
                  .getParentCode()) : "");
            if (destinationMap.containsKey(parent))
            {
               destination = new DestinationMainData();
               final ArrayList<DestinationMainData> entries = destinationMap.get(parent);
               destination.setNoAccommodations(facetValue.getCount());
               if (facetValue.getCode() != null)
               {
                  if (facetValue.getCode().contains("|"))
                  {
                     code = facetValue.getCode().split(REG_EXP)[1];
                     destination.setId(parent.getId() + UNDER_SCORE + DH + code);
                     destination.setValue(facetValue.getCode());
                     destination.setName(HtmlUtils.htmlEscape(facetValue.getLabel().split(REG_EXP)[1]));

                  }
                  else
                  {
                     code = facetValue.getCode();
                     destination.setId(parent.getId() + UNDER_SCORE + DO + code);
                     destination.setValue(code);
                     destination.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
                  }
               }

               entries.add(destination);
               destinationMap.put(parent, entries);
            }
            else
            {
               final ArrayList<DestinationMainData> entries = new ArrayList<DestinationMainData>();
               destination = new DestinationMainData();
               destination.setNoAccommodations(facetValue.getCount());
               if (facetValue.getCode() != null)
               {
                  if (facetValue.getCode().contains("|"))
                  {
                     code = facetValue.getCode().split(REG_EXP)[1];
                     destination.setId(parent.getId() + UNDER_SCORE + DH + code);
                     destination.setValue(facetValue.getCode());
                     destination.setName(HtmlUtils.htmlEscape(facetValue.getLabel().split(REG_EXP)[1]));
                  }
                  else
                  {
                     code = facetValue.getCode();
                     destination.setId(parent.getId() + UNDER_SCORE + DO + code);
                     destination.setValue(code);
                     destination.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
                  }
               }
               entries.add(destination);
               destinationMap.put(parent, entries);
            }
         }
      }

      final List<DestinationMainData> destMainList = new ArrayList<DestinationMainData>();
      final Iterator it = destinationMap.entrySet().iterator();
      while (it.hasNext())
      {
         final Map.Entry pairs = (Map.Entry) it.next();
         final DestinationMainData key = (DestinationMainData) pairs.getKey();
         destMainList.add(key);
         key.setChildren((List<DestinationMainData>) pairs.getValue());
      }
      return destMainList;
   }

   private Map getBestForMap()
   {
      final Map bestFor = new HashMap();
      final String bestForValuesStr = configurationService.getConfiguration().getString("holiday.search.bestfor.categories");
      if (StringUtils.isNotEmpty(bestForValuesStr))
      {
         final String[] categoryValuesArry = StringUtils.split(bestForValuesStr, ",");
         if (categoryValuesArry.length > 0)
         {
            for (final String bestForValues : categoryValuesArry)
            {

               bestFor.put(StringUtils.split(bestForValues, ':')[0], StringUtils.split(bestForValues, ':')[1]);
            }
         }
      }
      return bestFor;

   }

   private Map getDepartureTime()
   {
      final Map<String, String> departureTime = new HashMap<String, String>();

      departureTime.put("A", "Morning" + FilterConstants.SPACE + "-" + FilterConstants.SPACE + "06:00-11:59");
      departureTime.put("B", "Afternoon" + FilterConstants.SPACE + "-" + FilterConstants.SPACE + "12:00-17:59");
      departureTime.put("C", "Evening" + FilterConstants.SPACE + "-" + FilterConstants.SPACE + "18:00-20:59");
      departureTime.put("D", "Night" + FilterConstants.SPACE + "-" + FilterConstants.SPACE + "21:00-05:59");

      return departureTime;

   }

   private boolean facetEnabled(final String facetName)
   {
      try
      {
         return cmsComponentService.getAbstractCMSComponent(facetName).getVisible().booleanValue();
      }
      catch (final CMSItemNotFoundException e)
      {
         log.error(e.getMessage(), e);
      }
      return false;
   }

   /**
    * Get TripAdvisorRating
    *
    * @param ratingFilters
    * @param facet
    */
   private void getTripAdvisorRating(final List<SliderData> ratingFilters, final FacetOption facet,
         final Map<String, Boolean> visibility)
   {
      final List<FacetValue> facetValues = facet.getFacetValues();
      if (checkFacetValues(facetValues))
      {
         final SliderData minimumTripAdvisor = new SliderData();

         minimumTripAdvisor.setId(FilterConstants.TARATINGID);
         minimumTripAdvisor.setName(FilterConstants.TARATINGNAME);
         minimumTripAdvisor.setType(FilterConstants.SLIDER);
         minimumTripAdvisor.setTrackType(FilterConstants.TRACKTYPE);
         minimumTripAdvisor.setCode(facet.getFacetCode());
         final List<Double> values = new ArrayList<Double>();
         final List<Double> limit = new ArrayList<Double>();

         Double min = null;
         Double max = null;
         min = Math.floor(Double.parseDouble(facet.getFacetValues().get(0).getMin()));
         max = Math.floor(Double.parseDouble(facet.getFacetValues().get(0).getMax()));
         // For single value slider only minimum value is required
         values.add(min);

         limit.add(min);
         limit.add(max);
         minimumTripAdvisor.setLimit(limit);
         minimumTripAdvisor.setValues(values);
         minimumTripAdvisor.setSteps(FilterConstants.RATINGSTEPS);
         ratingFilters.add(minimumTripAdvisor);

         final List<Long> range1 = new ArrayList<Long>();
         range1.add(Long.valueOf(FilterConstants.MINRANGE));
         range1.add(Long.valueOf(FilterConstants.MAXRANGE));
         minimumTripAdvisor.setRange(range1);
      }
      else
      {
         visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.FALSE);

      }

   }

   /**
    * @param facetValues
    * @return checkFacetValues
    */
   private boolean checkFacetValues(final List<FacetValue> facetValues)
   {
      return CollectionUtils.isNotEmpty(facetValues) && facetValues.get(0).getMax() != null
            && facetValues.get(0).getMin() != null;
   }

   /**
    * @param visibility
    * @param generalOptionsFilter
    * @param facet
    */
   private void populateHolidayOperator(final Map<String, Boolean> visibility, final List holidayOperatorFilter,
         final FacetOption facet, final String brandType)
   {
      if (facetEnabled(FilterConstants.HOLIDAYOPERATORFILTER))
      {
         visibility.put(FilterConstants.HOLIDAYOPERATORFILTER, true);
         getHolidayOperator(holidayOperatorFilter, facet, brandType);
      }
   }

   /**
    * @param visibility
    * @param generalOptionsFilter
    * @param facet
    */
   private void populateHolidayTypeFilter(final Map<String, Boolean> visibility, final List holidayOperatorFilter,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.HOLIDAYTYPEFILTER))
      {
         visibility.put(FilterConstants.HOLIDAYTYPEFILTER, Boolean.TRUE);
         getHolidayType(holidayOperatorFilter, facet);
      }
   }

   // To Do For Getting the Holiday Operator
   /**
    * Get Product range(Holiday type)
    *
    * @param generalOptionsFilter
    * @param facet
    */
   private void getHolidayOperator(final List holidayOperatorFilter, final FacetOption facet, final String brandType)
   {
      final List<CommonData> holidayOperatorFilters = new ArrayList<CommonData>();

      final CommonFilterData holidayOperator = new CommonFilterData();
      holidayOperator.setName(FilterConstants.HOLIDAY_OPERATOR_NAME);
      holidayOperator.setId(FilterConstants.HOLIDAY_OPERATOR_CODE);
      holidayOperator.setValues(holidayOperatorFilters);
      holidayOperator.setType(FilterConstants.CHECKBOX);
      holidayOperator.setFilterType(AND);
      CommonData filterValue = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         // D is the brand code for TI(Thomson Ireland) from endeca and
         // considered to be a thomson holiday
         // and at a time we get either T or D but not both
         filterValue = new CommonData();
         if ("T".equalsIgnoreCase(HtmlUtils.htmlEscape(facetValue.getCode())))
         {
            filterValue.setName("Thomson");
            filterValue.setId("TH");
            filterValue.setValue("T");
            filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            holidayOperatorFilters.add(filterValue);
         }
         else if ("F".equalsIgnoreCase(HtmlUtils.htmlEscape(facetValue.getCode())) && !("TH".equalsIgnoreCase(brandType)))
         {
            filterValue.setName("First Choice");
            filterValue.setId("FC");
            filterValue.setValue(facetValue.getCode());
            filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            holidayOperatorFilters.add(filterValue);
         }
         else if ("D".equalsIgnoreCase(HtmlUtils.htmlEscape(facetValue.getCode())))
         {
            filterValue.setName("Thomson");
            filterValue.setId("TI");
            filterValue.setValue(facetValue.getCode());
            filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            holidayOperatorFilters.add(filterValue);
         }
         // E is for Falcon. Added to show holiday operator as falcon in
         // filters for search results.
         else if ("E".equalsIgnoreCase(HtmlUtils.htmlEscape(facetValue.getCode())))
         {
            filterValue.setName("Falcon");
            filterValue.setId("FJ");
            filterValue.setValue(facetValue.getCode());
            filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            holidayOperatorFilters.add(0, filterValue);
         }
      }
      holidayOperatorFilter.add(1, holidayOperator);
   }

   /**
    * @param visibility
    * @param generalOptionsFilter
    * @param facet
    */
   private void populateCollectionFilters(final Map<String, Boolean> visibility, final List generalOptionsFilter,
         final FacetOption facet)
   {
      if (facetEnabled(FilterConstants.COLLECTIONFILTER))
      {
         visibility.put(FilterConstants.COLLECTIONFILTER, Boolean.TRUE);
         getCollectionFilter(generalOptionsFilter, facet);
      }
   }

   /**
    * Get Product range(Holiday type)
    *
    * @param generalOptionsFilter
    * @param facet
    */
   private void getCollectionFilter(final List generalOptionsFilter, final FacetOption facet)
   {
      final List<CommonData> collectionsFiltersList = new ArrayList<CommonData>();

      final CommonFilterData collectionFilter = new CommonFilterData();
      collectionFilter.setName(FilterConstants.COLLECTION_NAME);
      collectionFilter.setId(FilterConstants.COLLECTION_ID);
      collectionFilter.setValues(collectionsFiltersList);
      collectionFilter.setType(FilterConstants.CHECKBOX);
      collectionFilter.setValues(collectionsFiltersList);
      collectionFilter.setFilterType("OR");
      generalOptionsFilter.add(TWO, collectionFilter);
      CommonData collectionFilterValue = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         collectionFilterValue = new CommonData();
         collectionFilterValue.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
         collectionFilterValue.setId("CL_" + facetValue.getCode());
         collectionFilterValue.setValue(HtmlUtils.htmlEscape(facetValue.getLabel()));
         collectionFilterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
         collectionsFiltersList.add(collectionFilterValue);
      }

   }

}
