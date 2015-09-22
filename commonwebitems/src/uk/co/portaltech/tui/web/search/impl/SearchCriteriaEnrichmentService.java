/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2013 hybris AG All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with hybris.
 */

/**
 *
 */
package uk.co.portaltech.tui.web.search.impl;

import static uk.co.portaltech.commons.DateUtils.addDuration;
import static uk.co.portaltech.commons.DateUtils.subtractDuration;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.helper.AirportHelper;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.search.EnrichmentService;
import uk.co.portaltech.tui.web.view.data.RoomAllocation;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

@SuppressWarnings("boxing")
public class SearchCriteriaEnrichmentService implements
   EnrichmentService<SearchPanelComponentModel, SearchResultsRequestData>
{

   /**
     *
     */
   private static final String SINGLE_ACCOMMODATION_SEARCH = "singleAccommodation.search";

   @Resource
   private AirportHelper airportHelper;

   @Resource
   private TypeService typeService;

   @Resource
   private SessionService sessionService;

   @Resource
   private TUIConfigService tuiConfigService;

   private static final String HOLIDAY_INDEX = "holidayIndex";

   private static final String DURATION = "duration";

   @Resource
   private ConfigurationService configurationService;

   private static final String SMERCH = "smerch";

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Override
   public void enrich(final SearchPanelComponentModel component,
      final SearchResultsRequestData searchParameter)
   {

      if (StringUtils.isEmpty(searchParameter.getDepartureDate()))
      {
         searchParameter.setDepartureDate(searchParameter.getWhen());
      }

      if (searchParameter.getFlexibleDays() <= 0)
      {
         searchParameter.setFlexibleDays(component.getFlexibleDays());
      }

      final int infantCount =
         getInfantCount(searchParameter.getChildrenAge(), component.getInfantAge());
      searchParameter.setInfantCount(infantCount);

      if (searchParameter.getNoOfChildren() > 0)
      {
         searchParameter.setChildCount(searchParameter.getNoOfChildren() - infantCount);
      }

      // Filter Child ages

      searchParameter.setChildAges(searchParameter.getChildrenAge());
      if (StringUtils.isNotEmpty(searchParameter.getSearchType())
         && StringUtils.equalsIgnoreCase(searchParameter.getSearchType(), SMERCH))
      {
         if (StringUtils.isNotEmpty((String) sessionService.getAttribute(DURATION)))
         {
            searchParameter.setSmerchDuration((String) sessionService.getAttribute(DURATION));
            sessionService.removeAttribute(DURATION);
         }
         else
         {
            searchParameter.setSmerchDuration(null);
         }
      }

      if (isSingleAccom(searchParameter))
      {
         searchParameter.setSingleAccomSearch(true);
      }
      updatePaginationLimit(component, searchParameter);

      // update when and until based on flexibility chosen by user
      createSearchDates(searchParameter);

      // this is temporary fix for FC UI needs to provide the child airports
      if (checkIfBrandFC() && (StringUtils.isNotEmpty(searchParameter.getSearchRequestType()))
         && !(StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), "ins")))
      {
         searchParameter.setAirports(airportHelper.getAirports(searchParameter));
      }
      // The holiday indexes have to be removed on every initial search.
      if (MapUtils.isEmpty(searchParameter.getRecomData()))
      {
         removeHolidayIndex(searchParameter);
         // recommendation call u shouldn't remove/set indexes in session.
      }
      // Determine party composition in case of room allocation
      if ((StringUtils.isNotEmpty(searchParameter.getSearchRequestType()))
         && (StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), "rooms")))
      {
         updatePartCompositionForRooms(component, searchParameter);
      }

      if (!(StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), "rooms"))
         && CollectionUtils.isNotEmpty(searchParameter.getRooms()))
      {
         int totalInfantCount = 0;

         for (final RoomAllocation room : searchParameter.getRooms())
         {
            final int infantsPerRoom =
               getInfantCount(room.getChildrenAge(), component.getInfantAge());
            room.setInfantCount(infantsPerRoom);
            totalInfantCount = totalInfantCount + infantsPerRoom;
         }
      }

   }

   /**
    * @return FC brand
    */
   private boolean checkIfBrandFC()
   {
      return StringUtils.equalsIgnoreCase(BrandType.FC.getCode(), tuiUtilityService.getSiteBrand());
   }

   /**
    * @param searchParameter
    * @return isSingleAccom
    */
   private boolean isSingleAccom(final SearchResultsRequestData searchParameter)
   {
      return Boolean.parseBoolean(tuiConfigService.getConfigValue(SINGLE_ACCOMMODATION_SEARCH,
         "true"))
         && CollectionUtils.size(searchParameter.getUnits()) == 1
         && (checkAccomType(searchParameter.getUnits().get(0).getType()));
   }

   private void removeHolidayIndex(final SearchResultsRequestData searchParameter)
   {

      if (!searchParameter.isFlightOptions()
         && (StringUtils.equalsIgnoreCase("ins", searchParameter.getSearchRequestType()))
         && (sessionService.getAttribute(HOLIDAY_INDEX) != null))
      {
         sessionService.removeAttribute(HOLIDAY_INDEX);
      }
      handleRemoveHoliday(searchParameter);
   }

   /**
    * @param searchParameter
    */
   private void handleRemoveHoliday(final SearchResultsRequestData searchParameter)
   {
      if (viewSelector.checkIsMobile() && !searchParameter.isFlightOptions()
         && (StringUtils.equalsIgnoreCase("Duration", searchParameter.getSearchRequestType()))
         && (sessionService.getAttribute(HOLIDAY_INDEX) != null))
      {
         sessionService.removeAttribute(HOLIDAY_INDEX);
      }
   }

   /**
    * @param value
    *
    */
   private boolean checkAccomType(final String value)
   {
      final Iterator<ItemModel> itr =
         typeService.getEnumerationTypeForCode("AccommodationType").getValues().iterator();

      while (itr.hasNext())
      {
         final EnumerationValueModel model = (EnumerationValueModel) itr.next();
         if (model.getCode().equalsIgnoreCase(value))
         {
            return true;
         }

      }
      if ("DayTrip".equalsIgnoreCase(value) || "LAP_HOTEL".equalsIgnoreCase(value))
      {
         return true;
      }

      return false;
   }

   /**
    * @param component
    * @param searchParameter
    */

   private void updatePaginationLimit(final SearchPanelComponentModel component,
      final SearchResultsRequestData searchParameter)
   {
      if (searchParameter.getLast() == 0)
      {
         searchParameter.setLast(component.getLast());
      }
      if (searchParameter.getOffset() == 0)
      {
         searchParameter.setOffset(component.getOffset());
      }
   }

   /**
    * @param component
    * @param searchParameter
    */

   private void updatePaginationLimitForHolidayFinder(final HolidayFinderComponentModel component,
      final SearchResultsRequestData searchParameter)
   {
      if (searchParameter.getLast() == 0)
      {
         searchParameter.setLast(component.getLast());
      }
      searchParameter.setOffset(component.getOffset());
   }

   private int getInfantCount(final List<Integer> childrenAge, final int infantAge)
   {
      int infantCount = 0;

      if (CollectionUtils.isNotEmpty(childrenAge))
      {
         for (final Integer age : childrenAge)
         {
            if (age < infantAge)
            {
               infantCount++;

            }
         }
      }
      return infantCount;
   }

   /**
    * This method modifies the search Request data based on flexibility chosen by user
    *
    * @param searchParameter
    */
   private void createSearchDates(final SearchResultsRequestData searchParameter)
   {
      if (!searchParameter.isFlexibility())
      {
         searchParameter.setWhen(searchParameter.getWhen());
         searchParameter.setUntil(searchParameter.getWhen());
      }
      else if (searchParameter.isFlexibility() && StringUtils.isEmpty(searchParameter.getUntil()))
      {
         searchParameter.setUntil(addDuration(searchParameter.getWhen(),
            searchParameter.getFlexibleDays()));
         searchParameter.setWhen(subtractDuration(searchParameter.getWhen(),
            searchParameter.getFlexibleDays()));
         // Added below condition for inventory switch to @com , mainly for boundary condition of
         // dates
         populateAtcomSwitchDate(searchParameter);

      }
      // this is when UI will provide when and until
      else
      {
         searchParameter.setWhen(searchParameter.getWhen());
         searchParameter.setUntil(searchParameter.getUntil());
         // Added below condition for inventory switch to @com , mainly for boundary condition of
         // dates
         populateAtcomSwitchDate(searchParameter);
      }
   }

   /**
    * Conditon for population
    *
    * if difference between departure date and switch date falls between flexible days the when and
    * until has to be restricted
    *
    * @param searchParameter
    */
   public void populateAtcomSwitchDate(final SearchResultsRequestData searchParameter)
   {

      final LocalDate departureDate = DateUtils.toDate(searchParameter.getDepartureDate());
      final String siteBrand = tuiUtilityService.getSiteBrand();

      final LocalDate switchDate =
         DateUtils.toDate(configurationService.getConfiguration().getString(
            siteBrand + CommonwebitemsConstants.DOT
               + CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015"));

      if ((DateUtils.isEqualOrAfter(departureDate, switchDate))
         && (DateUtils.subtractDates(departureDate, switchDate) <= searchParameter
            .getFlexibleDays()))
      {
         searchParameter.setWhen(DateUtils.format(switchDate));
      }
      else if ((DateUtils.isBefore(departureDate, switchDate))
         && (DateUtils.subtractDates(switchDate, departureDate) <= searchParameter
            .getFlexibleDays()))
      {
         searchParameter.setUntil(DateUtils.format(switchDate));
      }

   }

   /**
    * This method updates party composition based on room allocation
    *
    * @param component
    * @param searchParameter
    */
   private void updatePartCompositionForRooms(final SearchPanelComponentModel component,
      final SearchResultsRequestData searchParameter)
   {
      int noOfAdults = 0;
      int noOfSeniors = 0;
      int childrenCount = 0;
      int infantCount = 0;

      final List<Integer> childrenAge = new ArrayList<Integer>();
      for (final RoomAllocation room : searchParameter.getRooms())
      {
         final int infantsPerRoom = getInfantCount(room.getChildrenAge(), component.getInfantAge());
         noOfAdults = noOfAdults + room.getNoOfAdults();
         noOfSeniors = noOfSeniors + room.getNoOfSeniors();
         childrenCount = childrenCount + room.getNoOfChildren();
         room.setInfantCount(infantsPerRoom);
         infantCount = infantCount + infantsPerRoom;

         childrenAge.addAll(room.getChildrenAge());
      }

      searchParameter.setNoOfAdults(noOfAdults);
      searchParameter.setNoOfSeniors(noOfSeniors);
      searchParameter.setChildCount(childrenCount - infantCount);

      searchParameter.setChildAges(childrenAge);

   }

   @Override
   public void enrichForHolidayFinder(final HolidayFinderComponentModel component,
      final SearchResultsRequestData searchParameter)
   {

      handleEnrichHoliday(component, searchParameter);

      final int infantCount =
         getInfantCount(searchParameter.getChildrenAge(), component.getInfantAge());
      searchParameter.setInfantCount(infantCount);

      if (searchParameter.getNoOfChildren() > 0)
      {
         searchParameter.setChildCount(searchParameter.getNoOfChildren() - infantCount);
      }

      // Filter Child ages

      searchParameter.setChildAges(searchParameter.getChildrenAge());

      if (isSingleAccom(searchParameter))
      {
         searchParameter.setSingleAccomSearch(true);
      }
      updatePaginationLimitForHolidayFinder(component, searchParameter);

      // update when and until based on flexibility chosen by user
      createSearchDates(searchParameter);

      handleHolidayIndex(component, searchParameter);

   }

   /**
    * @param component
    * @param searchParameter
    */
   private void handleEnrichHoliday(final HolidayFinderComponentModel component,
      final SearchResultsRequestData searchParameter)
   {
      if (StringUtils.isEmpty(searchParameter.getDepartureDate()))
      {
         searchParameter.setDepartureDate(searchParameter.getWhen());
      }

      if (searchParameter.getFlexibleDays() <= 0)
      {
         searchParameter.setFlexibleDays(component.getFlexibleDays());
      }
   }

   /**
    * @param component
    * @param searchParameter
    */
   private void handleHolidayIndex(final HolidayFinderComponentModel component,
      final SearchResultsRequestData searchParameter)
   {
      if ((StringUtils.isNotEmpty(searchParameter.getSearchRequestType()))
         && !(StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), "ins")))
      {
         searchParameter.setAirports(airportHelper.getAirports(searchParameter));
      }
      // The holiday indexes have to be removed on every initial search.
      removeHolidayIndex(searchParameter);
      // Determine party composition in case of room allocation
      if ((StringUtils.isNotEmpty(searchParameter.getSearchRequestType()))
         && (StringUtils.equalsIgnoreCase(searchParameter.getSearchRequestType(), "rooms")))
      {
         updatePartCompositionForRoomsForHolidayFinder(component, searchParameter);
      }

   }

   /**
    * This method updates party composition based on room allocation
    *
    * @param component
    * @param searchParameter
    */

   private void updatePartCompositionForRoomsForHolidayFinder(
      final HolidayFinderComponentModel component, final SearchResultsRequestData searchParameter)
   {

      int noOfAdults = 0;
      int noOfSeniors = 0;
      int childrenCount = 0;
      int infantCount = 0;

      final List<Integer> childrenAge = new ArrayList<Integer>();
      for (final RoomAllocation room : searchParameter.getRooms())
      {

         final int infantsPerRoom = getInfantCount(room.getChildrenAge(), component.getInfantAge());
         noOfAdults = noOfAdults + room.getNoOfAdults();
         noOfSeniors = noOfSeniors + room.getNoOfSeniors();
         childrenCount = childrenCount + room.getNoOfChildren();
         room.setInfantCount(infantsPerRoom);
         infantCount = infantCount + infantsPerRoom;

         childrenAge.addAll(room.getChildrenAge());
      }

      searchParameter.setNoOfAdults(noOfAdults);
      searchParameter.setNoOfSeniors(noOfSeniors);
      searchParameter.setChildCount(childrenCount - infantCount);

      searchParameter.setChildAges(childrenAge);

   }

}
