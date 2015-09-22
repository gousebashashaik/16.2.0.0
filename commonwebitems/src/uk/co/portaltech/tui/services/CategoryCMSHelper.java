/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author James Johnstone
 *
 */
public class CategoryCMSHelper {

    @Resource
    private SessionService sessionService;


    private static final TUILogUtils LOG = new TUILogUtils("CategoryCMSHelper");

    public void setCategoryCode(String categoryCode) {
        LOG.debug("Setting Session Attribute categoryPageCode to " + categoryCode);
        sessionService.setAttribute("categoryCode", categoryCode);
    }

    public void setContextType(String contextType) {
        LOG.debug("Setting Session Attribute contextType to " + contextType);
        sessionService.setAttribute("contextType", contextType);
    }

}
