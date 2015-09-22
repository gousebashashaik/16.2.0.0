/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AnalyticsParamsModel;
import uk.co.portaltech.tui.facades.AnalyticsFacade;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;

/**
 *
 */
public class DefaultAnalyticFacade implements AnalyticsFacade
{

   @Resource
   private Populator<Object, Map<String, WebAnalytics>> searchResultsAnalyticsPopulator;

   @Resource
   private Populator<Object, Map<String, WebAnalytics>> accommodationAnalyticsPopulator;

   @Resource
   private Populator<Object, Map<String, WebAnalytics>> bookFlowAnalyticsPopulator;

   @Resource
   private Populator<Object, Map<String, WebAnalytics>> displayBookingAnalyticsPopulator;

   @Resource
   private Populator<Object, Map<String, WebAnalytics>> managePaymentConfirmationAnalyticsPopulator;

   private static final String ACCOMODATION_PAGE = "bookFlowAccommodationOverviewPage";

   private static final String ACCOMODATION_PAGE_MOBILE =
      "bookFlowAccommodationOverviewPage_Mobile";

   private static final String SEARCH_RESULTS_PAGE_LABEL = "searchresultspage";

   private static final String NEW_SEARCH_RESULTS_PAGE_LABEL = "newSearchresultspage";

   private static final String SEARCH_RESULTS_PAGE_LABEL_MOBILE = "MobileSearchresultspage";

   private static final String SINGLE_ACCOM_RESULTS_PAGE_LABEL = "singleaccomsearchresultspage";

   private static final String NO_RESULTS_PAGE_LABEL = "noresultspage";

   private static final String NO_RESULTS_PAGE_LABEL_MOBILE = "noResultsMobile";

   private static final String VILLA_ACCOMODATION_PAGE = "bookFlowAccommodationOverviewPage_Villa";

   private static final String CRUISE_ACCOMODATION_PAGE =
      "bookFlowAccommodationOverviewPage_Cruise";

   private static final String VILLA_ACCOMODATION_PAGE_MOBILE = "bookFlowVillaOverviewPage_Mobile";

   private static final String FLIGHT_ANALYTICS = "flightOptionsPage";

   private static final String ROOM_ANALYTICS = "roomOptionsPage";

   private static final String EXTRA_ANALYTICS = "extraOptionsPage";

   private static final String PASSENGERS_ANALYTICS = "passengerDetailsPage";

   private String smerchSession;

   private static final String CONFIRMATION_ANALYTICS = "confirmationpage";

   private static final String SINGLE_ACCOM_RESULTS_PAGE_LABEL_MOBILE =
      "MobileSingleAccomSearchresultspage";

   private static final String DISPLAY_BOOKING_PAGE = "fc_customerManageDisplayBookingPage";

   private static final String MANAGE_PAYMENT_CONFIRMATION_PAGE =
      "fc_managePaymentConfirmationPage";

   @Resource
   private SessionService sessionService;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.AnalyticsFacade#createAnalyticsData(java.util.List)
    */
   @Override
   public List<WebAnalytics> createAnalyticsData(final List<Object> analyticModel,
      final List<AnalyticsParamsModel> analyticOrders, final String page)
   {
      final List<WebAnalytics> analytics = new ArrayList<WebAnalytics>();
      final Map<String, WebAnalytics> analyticMap = new HashMap<String, WebAnalytics>();
      if (CollectionUtils.isNotEmpty(analyticModel))
      {

         for (final Object model : analyticModel)
         {
            if (isResultsPage(page))
            {
               searchResultsAnalyticsPopulator.populate(model, analyticMap);
            }
            else if (accomodTypeCheck(page)
               || StringUtils.equalsIgnoreCase(page, VILLA_ACCOMODATION_PAGE_MOBILE)
               || StringUtils.equalsIgnoreCase(page, CRUISE_ACCOMODATION_PAGE))
            {
               accommodationAnalyticsPopulator.populate(model, analyticMap);
            }
            else if (isFCBookFlowPage(page))
            {
               bookFlowAnalyticsPopulator.populate(model, analyticMap);
            }
            else if (StringUtils.equalsIgnoreCase(page, DISPLAY_BOOKING_PAGE))
            {
               displayBookingAnalyticsPopulator.populate(model, analyticMap);
            }
            else if (StringUtils.equalsIgnoreCase(page, MANAGE_PAYMENT_CONFIRMATION_PAGE))
            {
               managePaymentConfirmationAnalyticsPopulator.populate(model, analyticMap);
            }
         }
      }
      if (!analyticOrders.isEmpty())
      {
         for (final AnalyticsParamsModel apm : analyticOrders)
         {
            analytics.add(analyticMap.get(apm.getCode()));
         }
      }

      return analytics;
   }

   private boolean accomodTypeCheck(final String page)
   {
      return StringUtils.equalsIgnoreCase(page, ACCOMODATION_PAGE)
         || StringUtils.equalsIgnoreCase(page, ACCOMODATION_PAGE_MOBILE)
         || StringUtils.equalsIgnoreCase(page, VILLA_ACCOMODATION_PAGE);
   }

   /**
    * @return:String
    */
   private boolean isResultsPage(final String pageUid)
   {
      if (sessionService != null)
      {
         smerchSession = sessionService.getAttribute("smerchresultspage");
      }
      return pageTypeCheck(pageUid)
         || StringUtils.equalsIgnoreCase(pageUid, SEARCH_RESULTS_PAGE_LABEL_MOBILE)
         || StringUtils.equalsIgnoreCase(pageUid, NO_RESULTS_PAGE_LABEL_MOBILE)
         || StringUtils.equalsIgnoreCase(pageUid, SINGLE_ACCOM_RESULTS_PAGE_LABEL_MOBILE)
         || StringUtils.equalsIgnoreCase(smerchSession, "smerchresultspage");
   }

   private boolean pageTypeCheck(final String pageUid)
   {
      return StringUtils.equalsIgnoreCase(pageUid, SEARCH_RESULTS_PAGE_LABEL)
         || StringUtils.equalsIgnoreCase(pageUid, NEW_SEARCH_RESULTS_PAGE_LABEL)
         || StringUtils.equalsIgnoreCase(pageUid, SINGLE_ACCOM_RESULTS_PAGE_LABEL)
         || StringUtils.equalsIgnoreCase(pageUid, NO_RESULTS_PAGE_LABEL);
   }

   /**
    * Comparing weather is this is a book flow page or not.
    *
    * @return true if pageuid is from bookflow pages, else false
    */
   private boolean isFCBookFlowPage(final String pageUid)
   {
      return analyticsTypeCheck(pageUid)
         || StringUtils.equalsIgnoreCase(pageUid, PASSENGERS_ANALYTICS)
         || StringUtils.equalsIgnoreCase(pageUid, CONFIRMATION_ANALYTICS);
   }

   private boolean analyticsTypeCheck(final String pageUid)
   {

      return StringUtils.equalsIgnoreCase(pageUid, FLIGHT_ANALYTICS)
         || StringUtils.equalsIgnoreCase(pageUid, ROOM_ANALYTICS)
         || StringUtils.equalsIgnoreCase(pageUid, EXTRA_ANALYTICS);
   }

}
