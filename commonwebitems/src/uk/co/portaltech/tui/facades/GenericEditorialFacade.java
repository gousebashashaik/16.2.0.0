/**
 *
 */
package uk.co.portaltech.tui.facades;


/**
 * @author pts
 *
 */
public interface GenericEditorialFacade {

    /**
     * @param component
     * @param productCode
     * @param categoryCode
     * @return String
     */

    /**
     * @param productCode
     * @param categoryCode
     * @param featureCode
     * @return featureValue
     */
    String getConfiguredFeatureValueForCode(String productCode, String categoryCode, String featureCode);
}
