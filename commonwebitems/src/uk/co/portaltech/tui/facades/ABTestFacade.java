/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import uk.co.portaltech.tui.web.view.data.ABTestViewData;

/**
 * Facade interface to manage A/B Testing
 *
 * @author s.consolino
 *
 */
public interface ABTestFacade
{
   /**
    * Retrieves the information of the selected A/B variation for an A/B test session.
    *
    * @param componentUid Uid of the A/B Test Component that manages this A/B test session
    * @return The {@link ABTestViewData} object that contains the information about the selected A/B
    *         variation for this A/B test session
    */
   ABTestViewData getABTestData(String componentUid);

   /**
    * Retrieves the information of the selected A/B variation for an A/B test session from the
    * specified Variant Group.
    *
    * @param componentUid Uid of the A/B Test Component that manages this A/B test session
    * @param variantName The Variant Group name that manages this A/B test session
    * @return The {@link ABTestViewData} object that contains the information about the selected A/B
    *         variation for this A/B test session
    */
   ABTestViewData getABTestData(String componentUid, String variantName);

   /**
    *
    * @param componentUid
    * @return variant name.
    */
   String getVariantForNewUser(String componentUid);

   /**
    * @param componentUid
    * @param testNames
    * @return variantName
    */
   ABTestViewData getVariant(String componentUid, Set<String> testNames);

   /**
    * This method checks if variantGroup is there in the system. If yes then it returns its
    * percentage else retrun 0
    *
    * @param variantCode
    * @return percentage
    */
   int getVariantPercentageByCode(String variantCode);

   /**
    * This method gets variant code for a given testCode.
    *
    * @param testCode
    * @param request
    *
    * @return percentage
    */
   String getVariantCode(String testCode, HttpServletRequest request);

   /**
    * This method checks and adds cookie for the variant.
    *
    * @param componentUid
    * @param abtestCodeInSession
    * @param isCookieAbsentInRequestWrapper
    *
    * @return variantCode
    */
   String checkForDefault(String componentUid, HttpServletRequest request,
      String abtestCodeInSession, boolean isCookieAbsentInRequestWrapper);

   /**
    * This method checks and adds cookie for the variant.
    *
    * @param testCode
    * @param abTestVariantCode
    * @param request
    */
   String checkVariantCodeAndCookies(String testCode, String abTestVariantCode,
      HttpServletRequest request);

}
