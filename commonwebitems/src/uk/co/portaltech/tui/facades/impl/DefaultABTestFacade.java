package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.components.model.ABTestComponentModel;
import uk.co.portaltech.tui.facades.ABTestFacade;
import uk.co.portaltech.tui.model.ABTestModel;
import uk.co.portaltech.tui.model.VariantGroupModel;
import uk.co.portaltech.tui.services.ABTestComponentService;
import uk.co.portaltech.tui.services.TuiPageService;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * Default implementation of the {@link ABTestFacade} interface
 *
 * @author s.consolino
 *
 */
public class DefaultABTestFacade implements ABTestFacade
{
   private static final TUILogUtils LOGGER = new TUILogUtils("DefaultABTestFacade");

   private static final String COOKIE_PREFIX = "fc.multivariatetest.cookieprefix";

   private static final String AB = "ab_";

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource
   private ABTestComponentService abTestComponentService;

   @Resource
   private Converter<SimpleCMSComponentModel, ABTestViewData> abTestDataConverter;

   @Resource
   private Populator<ABTestComponentModel, ABTestViewData> abTestDataPopulatorFromABTestComponent;

   @Resource
   private Populator<SimpleCMSComponentModel, ABTestViewData> abTestDataPopulatorFromCMSComponent;

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private TuiPageService tuiPageService;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#getABTestData(java.lang.String)
    */
   @Override
   public ABTestViewData getABTestData(final String componentUid)
   {
      ABTestComponentModel abTestComponent = null;
      SimpleCMSComponentModel simpleCMSComponent = null;
      try
      {
         // Retries the model of the A/B Test component with uid in input
         abTestComponent =
            (ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(componentUid);
         // Retrieves the model of the CMS Component that represents the selected A/B variation
         simpleCMSComponent = abTestComponentService.getRandomCMSComponent(abTestComponent);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOGGER.error("A/B Test Component with UID [" + componentUid + "] not found.", e);
      }
      return populateABTestViewData(abTestComponent, simpleCMSComponent);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#getABTestData(java.lang.String,
    * java.lang.String)
    */
   @Override
   public ABTestViewData getABTestData(final String componentUid, final String variantCode)
   {
      ABTestComponentModel abTestComponent = null;
      SimpleCMSComponentModel simpleCMSComponent = null;
      try
      {
         // Retries the model of the A/B Test component with uid in input
         abTestComponent =
            (ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(componentUid);
         // Retrieves the model of the CMS Component that represents the selected A/B variation
         simpleCMSComponent =
            abTestComponentService.getRandomCMSComponentFromVariantGroup(abTestComponent,
               variantCode);

      }
      catch (final CMSItemNotFoundException e)
      {
         LOGGER.error("A/B Test Component with UID [" + componentUid + "] not found.", e);
      }
      return populateABTestViewData(abTestComponent, simpleCMSComponent, variantCode);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#getVariant(java.lang.String, java.util.Set)
    */
   @Override
   public ABTestViewData getVariant(final String componentUid, final Set<String> testNames)
   {
      ABTestComponentModel abTestComponent = null;
      Map<ABTestModel, VariantGroupModel> abtestVariantGroup = null;
      try
      {
         // Retries the model of the A/B Test component with uid in input
         abTestComponent =
            (ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(componentUid);
         abtestVariantGroup = abTestComponentService.getVariant(abTestComponent, testNames);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOGGER.error("A/B Test Component with UID [" + componentUid + "] not found.", e);
      }
      return populateABTestViewData(abTestComponent, abtestVariantGroup);
   }

   /**
    * @param abTestComponent
    * @param abtestVariantGroup
    * @return abTestViewData
    */
   private ABTestViewData populateABTestViewData(final ABTestComponentModel abTestComponent,
      final Map<ABTestModel, VariantGroupModel> abtestVariantGroup)
   {
      ABTestViewData abTestViewData = null;
      // Generates the ABTestViewData object that contains the information about the selected A/B
      // variation
      abTestViewData = abTestDataConverter.convert(abTestComponent);
      if (abtestVariantGroup != null & !abtestVariantGroup.isEmpty())
      {
         for (final Entry<ABTestModel, VariantGroupModel> entry : abtestVariantGroup.entrySet())
         {
            final ABTestModel abtestModel = entry.getKey();
            final VariantGroupModel variantGroupModel = entry.getValue();
            abTestViewData.setTestName(abtestModel.getTestCode());
            abTestViewData.setVariantCode(variantGroupModel.getVariantCode());
         }
      }
      return abTestViewData;
   }

   /**
    * @param abTestComponent
    * @param simpleCMSComponent
    * @param variantName
    * @return abTestViewData
    */
   private ABTestViewData populateABTestViewData(final ABTestComponentModel abTestComponent,
      final SimpleCMSComponentModel simpleCMSComponent, final String variantName)
   {
      final ABTestViewData abTestViewData =
         populateABTestViewData(abTestComponent, simpleCMSComponent);
      // should have used populators, but variant name is part of VariantGroupModel.
      if (abTestViewData != null)
      {
         abTestViewData.setVariantCode(variantName);
         abTestViewData.setTestName(abTestComponent.getAbTest().getTestCode());
      }
      return abTestViewData;
   }

   /**
    * @param abTestComponent
    * @param simpleCMSComponent
    * @return returns AbTestView Data.
    */
   private ABTestViewData populateABTestViewData(final ABTestComponentModel abTestComponent,
      final SimpleCMSComponentModel simpleCMSComponent)
   {
      ABTestViewData abTestViewData = null;
      if (simpleCMSComponent != null)
      {
         // Generates the ABTestViewData object that contains the information about the selected A/B
         // variation
         abTestViewData = abTestDataConverter.convert(simpleCMSComponent);
         abTestDataPopulatorFromABTestComponent.populate(abTestComponent, abTestViewData);
         abTestDataPopulatorFromCMSComponent.populate(simpleCMSComponent, abTestViewData);
         LOGGER.debug(abTestViewData.toString());
      }
      return abTestViewData;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#getVariantForNewUser()
    */
   @Override
   public String getVariantForNewUser(final String testCode)
   {

      return abTestComponentService.getVariantCodeForNewUser(testCode);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#getVariantPercentageByCode(java.lang.String)
    */
   @Override
   public int getVariantPercentageByCode(final String variantCode)
   {
      return abTestComponentService.getVariantPercentageByCode(variantCode);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#getVariantCode(java.lang.String,
    * javax.servlet.http.HttpServletRequest)
    */
   @Override
   public String getVariantCode(final String testCode, final HttpServletRequest request)
   {

      String variantCode = "";
      final Cookie[] cookies = request.getCookies();
      final String cookieName =
         configurationService.getConfiguration().getString("fc.multivariatetest.cookieprefix",
            "ab_")
            + testCode;

      if (testCode != null && cookies != null)
      {
         variantCode = getCookieValue(cookies, cookieName);
      }

      // If the variant code is blank from cookie.. i.e user has deleted
      // cookie while he's in the same session or user is coming to the site
      // via a deep link, pick the cookie variant from session. This is
      // because of the same issue again. Cookies wont be available here while
      // the page is loading for the first time.

      variantCode = getVariantCodeFromSession(request, variantCode, cookieName);

      LOGGER.debug("AB variant for price overlay -> " + variantCode);

      return variantCode;
   }

   /**
    * @param request
    * @param variantCode
    * @param cookieName
    * @return variantCode
    */
   private String getVariantCodeFromSession(final HttpServletRequest request,
      final String variantCode, final String cookieName)
   {
      if (StringUtils.isBlank(variantCode))
      {
         return (String) request.getSession().getAttribute(cookieName);
      }
      return variantCode;
   }

   /**
    * @param variantCode
    * @param cookies
    * @param cookieName
    * @return
    */
   private String getCookieValue(final Cookie[] cookies, final String cookieName)
   {
      for (final Cookie cookie : cookies)
      {
         if ((cookieName).equalsIgnoreCase(cookie.getName()))
         {
            return cookie.getValue();
         }
      }
      return StringUtils.EMPTY;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ABTestFacade#checkForDefault(java.lang.String,
    * javax.servlet.http.HttpServletRequest)
    */
   @Override
   public String checkForDefault(final String componentUid, final HttpServletRequest request,
      final String abtestCodeInSession, final boolean isCookieAbsentInRequestWrapper)
   {
      String cookieName = null;
      final Cookie[] cookies = request.getCookies();
      String testCode = null;
      try
      {
         testCode = tuiPageService.resolveABTestcode(componentUid);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOGGER.info(" Could not find Component with the UID" + componentUid);
      }
      String abTestVariantCode =
         getVariantCodeFromCookies(testCode, cookies, abtestCodeInSession, request);
      cookieName = checkCookieValue(cookieName, cookies, testCode);
      if (isCookieAbsentInRequestWrapper)
      {
         abTestVariantCode = checkVariantCodeAndCookies(testCode, abTestVariantCode, request);
      }
      return abTestVariantCode;
   }

   /**
    * @param testCode
    * @return variant Code.
    */
   private String getVariantCodeFromCookies(final String testCode, final Cookie[] cookies,
      final String abtestCodeInSession, final HttpServletRequest request)
   {

      // Cookie will be set in the response. So, if there are multiple AB test
      // components in a page though the first component has set a variant in
      // the cookie, for the 2nd component, this cookie wont be available and
      // hence there is a chance that it may set another cookie with different
      // variant. This is resulting in loading a mix of phoenix and iscape
      // components in iscape/phoenix ab testing. The below check makes sure
      // that once a variant is determined by a test, it wont get changed.

      if (StringUtils.isNotBlank(abtestCodeInSession))
      {
         request.setAttribute("setCookie", abtestCodeInSession);
         // Creating cookie in case user deletes the cookie while in the same session.

         return abtestCodeInSession;
      }

      if (cookies != null)
      {
         return getCookieValue(cookies,
            configurationService.getConfiguration().getString(COOKIE_PREFIX, AB) + testCode);
      }
      // No cookie is found. Could be a brand new request.
      return null;
   }

   /**
    * @param cookieName
    * @param cookies
    * @param testCode
    * @return cookie name
    */
   private String checkCookieValue(final String cookieName, final Cookie[] cookies,
      final String testCode)
   {
      String cookieName1 = cookieName;
      if (cookies != null)
      {
         for (final Cookie cookie : cookies)
         {
            if ((configurationService.getConfiguration().getString(COOKIE_PREFIX, AB) + testCode)
               .equalsIgnoreCase(cookie.getName()))
            {
               cookieName1 = cookie.getName();
            }
         }
      }
      return cookieName1;
   }

   /**
    * @param testCode
    * @param abTestVariantCode
    * @return alternate variant code
    */
   public String checkVariantCodeAndCookies(final String testCode, final String abTestVariantCode,
      final HttpServletRequest request)
   {
      String abTestVariantCode1 = abTestVariantCode;
      if (abTestVariantCode1 == null || getVariantPercentageByCode(abTestVariantCode1) <= 0)
      {
         abTestVariantCode1 = getVariantCodeAndStoreCookies(testCode, request);
      }
      return abTestVariantCode1;
   }

   /**
    * @param testCode
    * @return variantCode
    */
   private String getVariantCodeAndStoreCookies(final String testCode,
      final HttpServletRequest request)
   {

      String variantCode = null;
      variantCode = getVariantForNewUser(testCode);
      request.setAttribute("setCookie", variantCode);
      request.setAttribute("setInRequestWrapper", variantCode);

      // Setting variant name in session which can be used to check if a variant has been set
      // for a test so that any further AB test components should not set the cookie

      return variantCode;
   }

}
