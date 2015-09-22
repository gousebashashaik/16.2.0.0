/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.Date;

import uk.co.portaltech.tui.model.AlertPageModel;
import uk.co.portaltech.tui.model.DarkSiteModel;
import uk.co.portaltech.tui.model.MobileWebAlertsModel;
import uk.co.portaltech.tui.model.WebAlertsModel;

/**
 * @author niranjani.r
 *
 */
public interface AlertFacade {

    /**
     * Method to modify the format of updated time.
     *
     * @param modifiedDate
     * @return updated time in the format "dd MMM yyyy HH:mm" .
     */
    String getUpdatedAlertTime(Date modifiedDate);

    /**
     * Method to get the webalertModel.
     *
     * @return WebAlertsModel
     */
    WebAlertsModel getWebAlerts();
    /**
     * Method to get the DarkSiteModel.
     *
     * @return DarkSiteModel
     */
    DarkSiteModel getDarkSite();

    /**
     * Method to get the AlertPageModel.
     *
     * @return AlertPageModel
     */

    AlertPageModel getAlert();

    MobileWebAlertsModel getMobileWebAlerts();


}
