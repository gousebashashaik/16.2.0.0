/**
 *
 */
package uk.co.tui.th.book.facade.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.HighLights;
import uk.co.tui.book.domain.lite.InclusivePackage;
import uk.co.tui.book.domain.lite.InventoryType;
import uk.co.tui.book.exception.BookServiceException;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.book.payment.request.PaymentRequest;
import uk.co.tui.book.services.PackageBookingService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.inventory.CheckPriceAvailabilityService;
import uk.co.tui.book.services.payment.PackagePaymentService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.services.data.MultiCentreData;
import uk.co.tui.services.item.LinkedItemService;
import uk.co.tui.th.book.constants.SessionObjectKeys;
import uk.co.tui.th.book.facade.BookFacade;
import uk.co.tui.th.book.populators.AlertPassengerViewDataPopulator;
import uk.co.tui.th.book.populators.AlertPopulator;
import uk.co.tui.th.book.populators.BookingDetailsViewDataPopulator;
import uk.co.tui.th.book.populators.HolidayInclusivePackagePopulator;
import uk.co.tui.th.book.store.CarHireUpgradeExtraFacilityStore;
import uk.co.tui.th.book.store.PackageExtraFacilityStore;
import uk.co.tui.th.book.view.data.AlertViewData;
import uk.co.tui.th.book.view.data.BookingDetailsViewData;
import uk.co.tui.th.exception.TUIBusinessException;
import uk.co.tui.th.exception.TUISystemException;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * The Class BookFacadeImpl.
 *
 * @author extps3
 */
public class BookFacadeImpl implements BookFacade
{

   /** The holiday inclusive package populator. */
   @Resource(name = "thHolidayInclusivePackagePopulator")
   private HolidayInclusivePackagePopulator holidayInclusivePackagePopulator;

   /** The model service. */
   @Resource
   private ModelService packageModelService;

   @Resource
   private PackageComponentService packageComponentService;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The info book inventory service. */
   @Resource
   private CheckPriceAvailabilityService checkPriceAvailabilityService;

   @Resource
   private PackageBookingService packageBookingService;

   /** The Payment Service . */
   @Resource
   private PackagePaymentService packagePaymentService;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The linked item service. */
   @Resource(name = "mainStreamTrvelLinkedItemService")
   private LinkedItemService linkedItemService;

   /** AlertViewData Populator . */
   @Resource(name = "thAlertPopulator")
   private AlertPopulator alertPopulator;

   /** AlertPassengerViewDataPopulator */
   @Resource(name = "thAlertPassengerViewDataPopulator")
   private AlertPassengerViewDataPopulator alertPassengerViewDataPopulator;

   @Resource(name = "thBookingDetailsViewDataPopulator")
   private BookingDetailsViewDataPopulator bookingDetailsViewDataPopulator;

   /** The Constant LOGGER. */
   // private static final Logger LOGGER =

   private static final TUILogUtils LOGGER = new TUILogUtils("BookFacadeImpl");

   /** The Constant TUI_EXCEPTION. */
   private static final String TUI_EXCEPTION = "TUISystemException : ";

   private static final String MULTICOMERROR_PREFIX = "MC";

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** Custom Sold Out Error Code */

   public static final String CUSTOM_SOLDOUTERRORCODE = "99999";

   public static final String SALES_CHANNEL = "WEB";

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.BookFacade#populateInclusivePackage
    * (uk.co.portaltech.travel.model.results.Holiday)
    */
   @Override
   public BasePackage populatePackage(final Holiday holiday)
   {

      final BasePackage inclusivePackage = new InclusivePackage();
      holidayInclusivePackagePopulator.populate(holiday, inclusivePackage);
      packageModelService.save(inclusivePackage);
      // store the packageCode for future reference
      // sessionService.setAttribute("packageCode",

      // store the packageModel
      sessionService.setAttribute("bookFlowPackageData", inclusivePackage);
      return inclusivePackage;
   }

   /**
    * Checks if the holiday package's price is changed.
    *
    * @param holidayPackage the holiday package
    * @param latestPacakgeFromInventory the latest pacakge from inventory
    * @return true, if there is a price change
    */
   public boolean hasPriceChanged(final BasePackage holidayPackage,
      final BasePackage latestPacakgeFromInventory)
   {
      if (holidayPackage.equals(latestPacakgeFromInventory))
      {
         return true;
      }
      return false;
   }

   /**
    * This method handles the infants not yet born scenario against the inventory.
    */
   @Override
   public AlertViewData checkInfantNotYetBornCase()
   {
      final AlertViewData alertViewdata = new AlertViewData();
      final Feedback feedback = new Feedback();
      alertPassengerViewDataPopulator.populate(feedback, alertViewdata);
      return alertViewdata;
   }

   /**
    * this method checks the price and availability of the selected package against the inventory.
    */
   @Override
   public List<AlertViewData> checkPriceAndAvailability()
      throws uk.co.tui.th.exception.TUIBusinessException, FlightSoldOutException
   {

      List<Feedback> feedBackList = Collections.emptyList();
      try
      {
         final BasePackage basePackage = packageCartService.getBasePackage();
         feedBackList =
            checkPriceAvailabilityService.updatePriceAndAvailability(basePackage,
               InventoryType.ATCOM == basePackage.getInventory().getInventoryType());
      }
      catch (final BookServiceException e)
      {
         handleBookServiceException(e);
      }

      List<AlertViewData> alertViewDataList = new ArrayList<AlertViewData>();
      alertViewDataList = alertPopulator.populateTotalCostAlert(alertViewDataList, feedBackList);

      return alertViewDataList;
   }

   /**
    * @param e
    * @throws FlightSoldOutException
    * @throws TUIBusinessException
    */
   private void handleBookServiceException(final BookServiceException e)
      throws FlightSoldOutException, TUIBusinessException
   {
      final List<String> multicomErrorCodes =
         Arrays.asList(StringUtils.split(Config.getParameter("multicom_error_codes"), ','));
      final String errorCode = StringUtils.trim(e.getErrorCode());
      if (multicomErrorCodes.contains(errorCode)
         || StringUtils.startsWithIgnoreCase(errorCode, MULTICOMERROR_PREFIX))
      {
         throw new FlightSoldOutException(e.getErrorCode(), e);
      }
      LOGGER.error(TUI_EXCEPTION + e.getMessage());
      final List<String> soldOutErrorCodes =
         Arrays.asList(StringUtils.split(Config.getParameter("soldOutErrorCodes"), ','));
      if (isSoldoutError(e.getErrorCode(), soldOutErrorCodes))
      {
         throw new uk.co.tui.th.exception.TUIBusinessException(e.getErrorCode(),
            e.getCustomMessage(), e);
      }
      else
      {
         throw new TUISystemException(e.getErrorCode(), e.getCustomMessage(), e);
      }
   }

   /**
    * Checks if is soldout error.
    *
    * @param errorCode the error code
    * @param soldOutErrorCodes
    * @return true if the error is of type soldout error
    */
   private boolean isSoldoutError(final String errorCode, final List<String> soldOutErrorCodes)
   {
      // Comparison logic has been moved to TibcoSoapFaultHandler
      return PackageUtilityService.isSoldOutErrorMatches(errorCode, soldOutErrorCodes);
   }

   /**
    * This method is called to render the confirmation page after successful booking.
    *
    */
   @Override
   public BookingDetailsViewData confirmBooking()
   {

      final BookingDetailsViewData bookingDetailsViewData = new BookingDetailsViewData();

      try
      {
         packageBookingService.confirmBooking();
      }
      catch (final BookServiceException e)
      {

         LOGGER.error("BookingService Exception", e);
      }

      bookingDetailsViewDataPopulator.populate(packageCartService.getBasePackage(),
         bookingDetailsViewData);

      return bookingDetailsViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.th.book.facade.BookFacade#getPaymentPageRelativeUrl(uk.co.tui.
    * domain.model.InclusivePackageModel, java.lang.String, java.util.Map)
    */
   @Override
   public String getPaymentPageRelativeUrl(final Map<String, String> urlMap, final String hostName,
      final String contextPath) throws FlightSoldOutException, TUIBusinessException
   {

      String[] paymentTokens = null;

      final String brandType = cmsSiteService.getCurrentSite().getUid();

      final PaymentRequest paymentRequest = new PaymentRequest();

      paymentRequest.setHostApplicationUrl(hostName);
      paymentRequest.getStepIndicators().putAll(urlMap);

      paymentRequest.setPrePaymentUrl(StringUtil.append(hostName,
         Config.getParameter(brandType + "_PRE_PAYMENT_URL")));
      paymentRequest.setPaymentFailureUrl(StringUtil.append(hostName,
         Config.getParameter(brandType + "_PAYMENT_FAILURE_URL")));
      paymentRequest.setClientURL(StringUtil.append(hostName, contextPath));
      paymentRequest.setBookingSessionIdentifier(sessionService.getCurrentSession().getSessionId());

      paymentRequest.setClientApplication(brandType);

      paymentRequest.setSalesChannel(SALES_CHANNEL);

      populateWebAnalyticsData(paymentRequest);
      populateMultiCentreData(paymentRequest);

      final Map<String, String> toolTipsMap = new HashMap<String, String>();
      toolTipsMap.putAll(staticContentServ.getPaymentContents());
      paymentRequest.setToolTips(toolTipsMap);
      // Info book would be fired here
      populateAlertViewData(paymentRequest);
      paymentTokens = packagePaymentService.getPaymentTokens(paymentRequest);
      // YTODO: correct below code.
      paymentRequest.setToken(paymentTokens[0]);
      paymentRequest.setServerInstance(paymentTokens[1]);

      sessionService.setAttribute(SessionObjectKeys.PAYMENTREQUEST, paymentRequest);

      return getPaymentPageRelativeUrl(paymentTokens);
   }

   /**
    * Populate multi centre data.
    *
    * @param paymentRequest the payment request
    */
   private void populateMultiCentreData(final PaymentRequest paymentRequest)
   {
      if (packageCartService.getBasePackage().getListOfHighlights()
         .contains(HighLights.MULTI_CENTRE))
      {
         paymentRequest.setMultiCentreData(convertMultiCentreData(linkedItemService
            .getMultiCentreData(packageComponentService
               .getStay(packageCartService.getBasePackage()).getCode())));
      }

   }

   /**
    * Convert multi centre data.
    *
    * @param multiCentreData the multi centre data
    * @return the map
    */
   private Map<String, List<String>> convertMultiCentreData(
      final List<MultiCentreData> multiCentreData)
   {
      final String name = "NAME";
      final String duration = "DURATION";
      final String location = "LOCATION";
      final String nights = " nights ";
      final Map<String, List<String>> paymentData = new HashMap<String, List<String>>();
      paymentData.put(name, new ArrayList<String>());
      paymentData.put(location, new ArrayList<String>());
      paymentData.put(duration, new ArrayList<String>());
      for (final MultiCentreData eachData : multiCentreData)
      {
         paymentData.get(name).add(eachData.getName());
         paymentData.get(location).add((String) eachData.getLocations().values().iterator().next());
         paymentData.get(duration).add(
            new StringBuilder(eachData.getDuration()).append(nights).append(eachData.getName())
               .toString());
      }
      return paymentData;
   }

   /**
    * Populate alert view data.
    *
    * @param paymentRequest the payment request
    * @throws TUIBusinessException
    */
   private void populateAlertViewData(final PaymentRequest paymentRequest)
      throws FlightSoldOutException, TUIBusinessException
   {
      List<AlertViewData> alertViewDataList = Collections.emptyList();
      final StringBuilder alertViewData = new StringBuilder();
      alertViewDataList = checkPriceAndAvailability();
      for (final AlertViewData data : alertViewDataList)
      {
         alertViewData.append(data.getMessageText());
      }
      paymentRequest.setAlertMessage(alertViewData.toString());
   }

   /**
    * Populates the web analytics data onto payment request
    *
    * @param paymentRequest
    */
   private void populateWebAnalyticsData(final PaymentRequest paymentRequest)
   {

      final List<WebAnalytics> webAnalyticsData = sessionService.getAttribute("webAnalyticData");

      if (CollectionUtils.isNotEmpty(webAnalyticsData))
      {

         final Map<String, String> webAnalyticsMap = new HashMap<String, String>();
         for (final WebAnalytics webAnalytics : webAnalyticsData)
         {
            if (webAnalytics != null)
            {
               webAnalyticsMap.put(webAnalytics.getKey(), webAnalytics.getValue());
               paymentRequest.setWebAnalyticsData(webAnalyticsMap);
            }
         }
      }
   }

   /**
    * Gets the payment page relative url.
    *
    * @param paymentTokens the payment tokens
    * @return paymentPage Relative Url
    */
   private String getPaymentPageRelativeUrl(final String[] paymentTokens)
   {
      final MessageFormat url = new MessageFormat(CommonwebitemsConstants.PAYMENT_RELATIVE_URL);
      return url.format(paymentTokens);
   }

   @Override
   public void flushSessionObjects()
   {
      final PackageExtraFacilityStore packageExtraFacilityStore =
         sessionService.getAttribute("PackageExtraFacilityStore");
      if (packageExtraFacilityStore != null)
      {
         packageExtraFacilityStore.flush();
         sessionService.removeAttribute("PackageExtraFacilityStore");
      }
      final CarHireUpgradeExtraFacilityStore carHireUpgradeExtraFacilityStore =
         sessionService.getAttribute("carHireUpgradeExtraFacilityStore");
      if (carHireUpgradeExtraFacilityStore != null)
      {
         carHireUpgradeExtraFacilityStore.flush();
         sessionService.removeAttribute("carHireUpgradeExtraFacilityStore");
      }
   }

   @Override
   public boolean isPreAuthSuccess()
   {
      return packageBookingService.isPreAutherisationSuccess();
   }
}