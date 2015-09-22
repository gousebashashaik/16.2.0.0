/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.FilterComponentModel;
import uk.co.portaltech.travel.model.FilterPanelComponentModel;
import uk.co.portaltech.travel.model.FilterSectionComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.model.FilterSectionComponent;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.MainFilterRequest;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;

public class FilterSectionService
{

   @Resource
   private ComponentFacade componentFacade;

   @Resource
   private SessionService sessionService;

   private static final TUILogUtils LOG = new TUILogUtils("FilterSectionService");

   private static final String FILTER_PANEL_COMPONENT_UID = "WF_COM_022";

   private static final String FILTER_PANEL_COMPONENT_UID_MOBILE = "WF_COM_022-1";

   @Resource
   private ViewSelector viewSelector;

   private static final String PIPE = "|";

   private static final String BUDGET_TOTAL = "budgettotal";

   /**
    * Find Filter Section and filter Components required for page
    *
    * @param filterComponent
    * @return List<FilterSectionComponent> - Contains sorted filter section with valid filter
    *         components for page
    */
   public List<FilterSectionComponent> getSectionComponent(
      final List<FilterComponentModel> filterComponent)
   {

      final List<FilterSectionComponent> sectionComponents =
         new ArrayList<FilterSectionComponent>();

      final List<String> validFilterComponents = new ArrayList<String>();

      for (final FilterComponentModel validcomponents : filterComponent)
      {
         validFilterComponents.add(validcomponents.getUid());
      }

      final FilterPanelComponentModel component = getFilterPanelComponent();

      if (component != null)
      {
         populateSectionComponents(sectionComponents, validFilterComponents,
            component.getFilterSectionComponent());
         Collections.sort(sectionComponents);
      }

      return sectionComponents;

   }

   /**
    * Add only valid filter Components for the page to Filter section
    *
    * @param sectionComponents
    * @param validFilterComponents
    * @param filterSectionComponent
    */
   private void populateSectionComponents(final List<FilterSectionComponent> sectionComponents,
      final List<String> validFilterComponents,
      final List<FilterSectionComponentModel> filterSectionComponent)
   {
      for (final FilterSectionComponentModel filterSectionComponentModel : filterSectionComponent)
      {

         final FilterSectionComponent filterSection = new FilterSectionComponent();
         filterSection.setName(filterSectionComponentModel.getName());
         filterSection.setUid(filterSectionComponentModel.getUid());
         filterSection.setPosition(filterSectionComponentModel.getViewPosition());

         addValidFilterComponent(filterSectionComponentModel.getFilterComponents(),
            validFilterComponents, filterSection.getFilterComponent());
         sectionComponents.add(filterSection);

      }
   }

   /**
    * Get Filter Section Component from wcms
    */
   private FilterPanelComponentModel getFilterPanelComponent()
   {
      FilterPanelComponentModel component = null;
      try
      {
         if (viewSelector.checkIsMobile())
         {
            component =
               (FilterPanelComponentModel) componentFacade
                  .getComponent(FILTER_PANEL_COMPONENT_UID_MOBILE);
         }
         else
         {
            component =
               (FilterPanelComponentModel) componentFacade.getComponent(FILTER_PANEL_COMPONENT_UID);
         }
      }
      catch (final NoSuchComponentException e)
      {
         LOG.error("error while fetching FilterPanelComponentModel components...", e);
      }

      return component;
   }

   /**
    * Validate filter components with filter components required with page
    *
    * @param filterSectionComponent
    * @param validFilterComponents
    * @param filters
    */
   private void addValidFilterComponent(final List<FilterComponentModel> filterSectionComponent,
      final List<String> validFilterComponents, final List<FilterComponentModel> filters)
   {
      for (final FilterComponentModel filterSectionComponentModel : filterSectionComponent)
      {
         if (validFilterComponents.contains(filterSectionComponentModel.getUid()))
         {
            filters.add(filterSectionComponentModel);
         }
      }

   }

   public void createSearchFilterMap(final HolidayViewData viewData)
   {

      final Map<String, String> filterSearchMap = new HashMap();
      final SearchResultsRequestData searchResultsRequestData = viewData.getSearchRequest();

      if (searchResultsRequestData.getFilters() != null)
      {
         final MainFilterRequest mainFilterRequest = searchResultsRequestData.getFilters();

         if (viewData.getSearchResult() != null)
         {
            filterSearchMap.put("Results",
               String.valueOf(viewData.getSearchResult().getEndecaResultsCount()));
         }

         // Creating For Best For
         if (mainFilterRequest.getBestfor() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> bestForFilterRequestList =
               mainFilterRequest.getBestfor();
            filterSearchMap.put("bestfor", trimPipe(createFilterString(bestForFilterRequestList)));
         }

         // Setting For Holiday Type
         if (mainFilterRequest.getHolidayType() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> holidayFilterRequestList =
               mainFilterRequest.getHolidayType();

            filterSearchMap.put("holidayType",
               trimPipe(createFilterString(holidayFilterRequestList)));
         }

         // Setting Features
         if (mainFilterRequest.getFeatures() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> featureForFilterRequestList =
               mainFilterRequest.getFeatures();
            filterSearchMap.put("features",
               trimPipe(createFilterString(featureForFilterRequestList)));
         }

         // Setting Destination Options
         if (mainFilterRequest.getDestinations() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> destinationForFilterRequestList =
               mainFilterRequest.getDestinations();
            final String destinationCodes = "";
            final StringBuilder sbDestinationCodes = new StringBuilder(destinationCodes);
            final String getParentPackageName = "";
            final StringBuilder sbGetParentPackageName = new StringBuilder(getParentPackageName);
            for (final uk.co.portaltech.tui.web.view.data.FilterRequest filterRequest : destinationForFilterRequestList)
            {

               if (!StringUtils.contains(getParentPackageName, filterRequest.getParent()))
               {

                  sbGetParentPackageName.append(filterRequest.getParent());
                  sbGetParentPackageName.append("|");
               }
               if (filterRequest.getValue() != null
                  && StringUtils.isNotEmpty(filterRequest.getValue()) && filterRequest.isSelected())
               {

                  sbDestinationCodes.append("L");
                  sbDestinationCodes.append(filterRequest.getValue());
                  sbDestinationCodes.append(PIPE);

               }

            }
            filterSearchMap.put("productF", trimPipe(sbGetParentPackageName.toString()));
            filterSearchMap.put("destinations", trimPipe(sbDestinationCodes.toString()));
         }

         if (mainFilterRequest.getBoardBasis() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> boardBasisFilterRequestList =
               mainFilterRequest.getBoardBasis();
            filterSearchMap.put("boardBasis",
               trimPipe(createFilterString(boardBasisFilterRequestList)));
         }

         if (mainFilterRequest.getHolidayOperator() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> holidayOperatorFilterRequestList =
               mainFilterRequest.getHolidayOperator();
            filterSearchMap.put("holidayOperator",
               trimPipe(createFilterString(holidayOperatorFilterRequestList)));
         }

         if (mainFilterRequest.getAccommodationType() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> accommodationTypeFilterRequestList =
               mainFilterRequest.getAccommodationType();
            filterSearchMap.put("accommodationType",
               trimPipe(createFilterString(accommodationTypeFilterRequestList)));
         }

         if (mainFilterRequest.getCollections() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> collectionsTypeFilterRequestList =
               mainFilterRequest.getCollections();
            filterSearchMap.put("collections",
               trimPipe(createFilterString(collectionsTypeFilterRequestList)));
         }

         if (mainFilterRequest.getHolidayType() != null)
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> holidayTypeFilterRequestList =
               mainFilterRequest.getHolidayType();
            filterSearchMap.put("holidayType",
               trimPipe(createFilterString(holidayTypeFilterRequestList)));
         }

         if (mainFilterRequest.getCollections() != null && viewData.getSearchResult() != null
            && viewData.getSearchResult().getSiteBrand() != null
            && "TH".equals(viewData.getSearchResult().getSiteBrand()))
         {
            final List<uk.co.portaltech.tui.web.view.data.FilterRequest> productTypeFilterRequestList =
               mainFilterRequest.getCollections();
            filterSearchMap.put("productF",
               trimPipe(createFilterString(productTypeFilterRequestList)));
         }

         filterMapCreator(filterSearchMap, mainFilterRequest);

      }
      sessionService.getCurrentSession().setAttribute("filterMap", filterSearchMap);

   }

   /**
    * @param filterSearchMap
    * @param mainFilterRequest
    */
   private void filterMapCreator(final Map<String, String> filterSearchMap,
      final MainFilterRequest mainFilterRequest)
   {
      // Setting Departure Points
      if (mainFilterRequest.getDeparturePoints() != null)
      {
         final List<uk.co.portaltech.tui.web.view.data.FilterRequest> departureForFilterRequestList =
            mainFilterRequest.getDeparturePoints();
         filterSearchMap.put("departurePoints",
            trimPipe(createFilterString(departureForFilterRequestList)));
      }

      // Setting Departure Out Time
      if (mainFilterRequest.getOutslots() != null)
      {
         final List<uk.co.portaltech.tui.web.view.data.FilterRequest> departureOutSlotForFilterRequestList =
            mainFilterRequest.getOutslots();
         filterSearchMap.put("outslots",
            trimPipe(createFilterString(departureOutSlotForFilterRequestList)));
      }

      // Setting Departure Coming Back Time
      if (mainFilterRequest.getInslots() != null)
      {
         final List<uk.co.portaltech.tui.web.view.data.FilterRequest> departureInSlotForFilterRequestList =
            mainFilterRequest.getInslots();
         filterSearchMap.put("inslots",
            trimPipe(createFilterString(departureInSlotForFilterRequestList)));
      }

      // Setting Rating F Value
      if (mainFilterRequest.getFcRating() != null && mainFilterRequest.getFcRating().isChanged())
      {
         filterSearchMap.put("fcRating", mainFilterRequest.getFcRating().getMin());
      }
      else
      {
         filterSearchMap.put("fcRating", null);
      }

      // Setting Budget Values
      if (mainFilterRequest.getBudgettotal() != null && mainFilterRequest.getBudgetpp() != null)
      {

         if (mainFilterRequest.getBudgettotal().isChanged()
            || mainFilterRequest.getBudgetpp().isChanged())
         {
            filterSearchMap.put(BUDGET_TOTAL, mainFilterRequest.getBudgettotal().getMin());
         }
         else if (mainFilterRequest.getBudgetpp() != null
            && mainFilterRequest.getBudgetpp().isChanged())
         {
            filterSearchMap.put(BUDGET_TOTAL, mainFilterRequest.getBudgettotal().getMin());
         }
         else
         {
            filterSearchMap.put(BUDGET_TOTAL, null);
         }
      }
      else
      {
         filterSearchMap.put(BUDGET_TOTAL, null);
      }

      // Setting Trip Advisor Rating
      if (mainFilterRequest.getTripadvisorrating() != null
         && mainFilterRequest.getTripadvisorrating().isChanged())
      {
         filterSearchMap.put("taRatingF", mainFilterRequest.getTripadvisorrating().getMin());
      }
      else
      {
         filterSearchMap.put("taRatingF", null);
      }
   }

   private String trimPipe(final String sourceString)
   {
      String trimString = "";
      if (sourceString != null && StringUtils.isNotEmpty(sourceString))
      {
         trimString = sourceString.substring(0, sourceString.length() - 1);
      }
      return trimString;
   }

   private String createFilterString(
      final List<uk.co.portaltech.tui.web.view.data.FilterRequest> featureForFilterRequestList)
   {
      final String convertedString = "";
      final StringBuilder sbConvertedString = new StringBuilder(convertedString);
      if (featureForFilterRequestList != null)
      {
         for (final uk.co.portaltech.tui.web.view.data.FilterRequest filterRequest : featureForFilterRequestList)
         {
            if (filterRequest.getName() != null && StringUtils.isNotEmpty(filterRequest.getName())
               && filterRequest.isSelected())
            {
               sbConvertedString.append(filterRequest.getName());
               sbConvertedString.append(PIPE);
            }

         }
      }
      return sbConvertedString.toString();
   }

}
