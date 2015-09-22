package uk.co.portaltech.tui.facades.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import uk.co.portaltech.travel.services.AlertsService;
import uk.co.portaltech.tui.facades.AlertFacade;
import uk.co.portaltech.tui.model.AlertPageModel;
import uk.co.portaltech.tui.model.DarkSiteModel;
import uk.co.portaltech.tui.model.MobileWebAlertsModel;
import uk.co.portaltech.tui.model.WebAlertsModel;


/**
 * @author niranjani.r
 *
 */
public class DefaultAlertFacade implements AlertFacade
{

    @Resource
    private AlertsService alertsService;

    @Override
    public String getUpdatedAlertTime(final Date modifiedDate)
    {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,MMM dd  HH:mm:ss Z yyyy", Locale.US);
        dateFormat.applyPattern("dd MMM yyyy HH:mm");
        return dateFormat.format(modifiedDate);

    }

    @Override
    public WebAlertsModel getWebAlerts()
    {

        return alertsService.getWebAlerts();

    }

    @Override
    public DarkSiteModel getDarkSite()
    {

        return alertsService.getDarkSite();

    }


    @Override
    public AlertPageModel getAlert()
    {
        return alertsService.getAlert();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.AlertFacade#getMobileWebAlerts()
     */
    @Override
    public MobileWebAlertsModel getMobileWebAlerts()
    {

        return alertsService.getMobileWebAlerts();
    }

    /**
     * @param alertsService
     *           the alertsService to set
     */
    public void setAlertsService(final AlertsService alertsService)
    {
        this.alertsService = alertsService;
    }



}
