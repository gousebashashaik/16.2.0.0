/**
 *
 */
package uk.co.portaltech.tui.services.restriction;

import de.hybris.platform.cms2.exceptions.RestrictionEvaluationException;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSRestrictionService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import uk.co.portaltech.tui.model.restriction.TUICategoryPageTypeRestrictionModel;

/**
 * @author James Johnstone
 *
 */
public class TuiRestrictionService extends DefaultCMSRestrictionService {

    @Resource
    private SessionService      sessionService;

    private static final String RESTRICTION          = "TUICategoryPageTypeRestriction";

    /**
     * Intercept the in-built evaluate method to provide extra restriction checking for when the restriction is a
     * category page type restriction.
     */
    @Override
    public boolean evaluate(AbstractRestrictionModel restriction, RestrictionData context) throws RestrictionEvaluationException {
        String type = restriction.getTypeCode();
        if (type.equals(RESTRICTION)) {
            String retrievedCategoryCode = sessionService.getAttribute("categoryCode");
            return ((TUICategoryPageTypeRestrictionModel) restriction).getTravelCategoryPageCode().equals(retrievedCategoryCode);
        } else {
            return super.evaluate(restriction, context);
        }
    }
}
