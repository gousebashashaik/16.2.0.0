/**
 *
 */
package uk.co.tui.manage.facade.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.tui.web.view.data.WebAnalytics;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.payment.request.PaymentRequest;
import uk.co.tui.book.services.PackageBookingService;
import uk.co.tui.book.services.payment.PackagePaymentService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.view.data.AlertViewData;
import uk.co.tui.manage.facade.PaymentFacade;
import uk.co.tui.manage.services.exception.TUIBusinessException;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author premkumar.nd
 *
 */
public class DefaultPaymentFacade implements PaymentFacade
{

   private static final TUILogUtils LOG = new TUILogUtils("DefaultPaymentFacade");

   // @Resource

   @Resource
   private PackagePaymentService managePackagePaymentService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The package component service. */
   // @Resource

   @Resource
   private SessionService sessionService;

   @Resource
   private PackageBookingService packageBookingService;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   private static final String KEY_HOTEL = "HOTEL";

   private static final String KEY_HOME = "HOME";

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.manage.facade.PaymentFacade#getPaymentPageRelativeURL(java.util.Map)
    */
   @Override
   public String getPaymentPageRelativeURL(final Map<String, String> urlMap, final String host)
      throws TUIBusinessException
   {
      LOG.info("Entering DefaultPaymentFacade->getPaymentPageRelativeURL()");

      final String brandType = cmsSiteService.getCurrentSite().getUid();
      final String salesChannel = "MANAGE" + brandType;
      LOG.info("Entering DefaultPaymentFacade->getPaymentPageRelativeURL()- >brandType" + brandType);
      final PaymentRequest paymentRequest = new PaymentRequest();
      urlMap.put(KEY_HOTEL,
         StringUtil.append(host, Config.getParameter(brandType + "_BACK_TO_DISPBOOK_URL")));
      urlMap.put(KEY_HOME,
         StringUtil.append(host, Config.getParameter(brandType + "_MANAGE_HOME_URL")));

      paymentRequest.setHostApplicationUrl(host);
      paymentRequest.getStepIndicators().putAll(urlMap);

      paymentRequest.setPrePaymentUrl(StringUtil.append(host,
         Config.getParameter(brandType + "_BACK_TO_DISPBOOK_URL")));
      paymentRequest.setPaymentFailureUrl(StringUtil.append(host,
         Config.getParameter(salesChannel + "_PAYMENT_FAILURE_URL")));
      paymentRequest.setBookingSessionIdentifier(sessionService.getCurrentSession().getSessionId());

      paymentRequest.setClientApplication(brandType);

      paymentRequest.setSalesChannel(salesChannel);

      populateWebAnalyticsData(paymentRequest);

      final Map<String, String> toolTipsMap = new HashMap<String, String>();
      toolTipsMap.putAll(staticContentServ.getPaymentContents());
      paymentRequest.setToolTips(toolTipsMap);
      // Info book would be fired here
      populateAlertViewData(paymentRequest);
      final String[] paymentTokens = managePackagePaymentService.getPaymentTokens(paymentRequest);

      // YTODO: correct below code.
      paymentRequest.setToken(paymentTokens[0]);
      paymentRequest.setServerInstance(paymentTokens[1]);

      sessionService.setAttribute(SessionObjectKeys.PAYMENTREQUEST, paymentRequest);

      return getPaymentPageRelativeUrl(paymentTokens);
   }

   /**
    * Populates the web analytics data onto payment request.
    *
    * @param paymentRequest the payment request
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
    * @param paymentRequest
    * @throws TUIBusinessException
    */
   private void populateAlertViewData(final PaymentRequest paymentRequest)
      throws TUIBusinessException
   {
      final List<AlertViewData> alertViewDataList = Collections.emptyList();
      final StringBuilder alertViewData = new StringBuilder();
      for (final AlertViewData data : alertViewDataList)
      {
         alertViewData.append(data.getMessageText());
      }

      paymentRequest.setAlertMessage(alertViewData.toString());

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

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.manage.facade.PaymentFacade#isPreAuthSuccess()
    */
   @Override
   public boolean isPreAuthSuccess()
   {
      return packageBookingService.isPreAutherisationSuccess();
   }
}
