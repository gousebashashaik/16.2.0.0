/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;

import java.util.Map;
import java.util.Set;

import uk.co.portaltech.tui.components.model.ABTestComponentModel;
import uk.co.portaltech.tui.model.ABTestModel;
import uk.co.portaltech.tui.model.VariantGroupModel;

/**
 * The Interface ABTestComponentService. It provides methods for TUI A/B Testing support.
 *
 * @author s.consolino
 *
 */
public interface ABTestComponentService {

    /**
     * Gets a random A/B variation for an A/B test session.
     *
     * If the scope of the A/B Test Component in input is REQUEST, then every time will determine an A/B variation of
     * the test and returns it.
     *
     * Otherwise, if the scope of the A/B Test Component in input is SESSION, the first time determines a an A/B
     * variation for the current session, sets it to users session and returns it. Each time the A/B test, the same
     * variant is returned.
     *
     * @param abTestComponent
     *            The {@link ABTestComponentModel} object that represents the actual A/B test session.
     * @return The {@link SimpleCMSComponentModel} object that represents the selected A/B variation for this A/B test
     *         session. test session.
     */
    SimpleCMSComponentModel getRandomCMSComponent(ABTestComponentModel abTestComponent);

    /**
     * @param abTestComponent
     * @param variantName
     * @return The {@link SimpleCMSComponentModel} object that represents the selected A/B variation for this A/B test
     *         variant group. test session.
     */
    SimpleCMSComponentModel getRandomCMSComponentFromVariantGroup(
            ABTestComponentModel abTestComponent, String variantName);


    /**
     * This method returns the varient group model based on percentage specified for the group.
     * @return The {@link VariantGroupModel} object that represents the A/B.../Z variation for this A/B test
     */
    Map<ABTestModel, VariantGroupModel> getRandomVariantGroup(ABTestComponentModel abTestComponent);



    /**
     * @param abTestComponent
     * @param testNames
     * @return VariantGroupModel
     */
    Map<ABTestModel, VariantGroupModel> getVariant(ABTestComponentModel abTestComponent,
            Set<String> testNames);



    /**
     *
     * @return Variant Code for new User
     */
    String getVariantCodeForNewUser(String testCode);

    /**
     * This method checks if variantGroup is there in the system. If yes then it returns its percentage else retrun 0
     * @param variantCode
     * @return percentage
     */
    int getVariantPercentageByCode(String variantCode);

}
