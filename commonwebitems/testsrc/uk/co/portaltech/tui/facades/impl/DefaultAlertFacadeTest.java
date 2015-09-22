/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.services.AlertsService;
import uk.co.portaltech.tui.facades.impl.DefaultAlertFacade;
import uk.co.portaltech.tui.model.AlertPageModel;
import uk.co.portaltech.tui.model.DarkSiteModel;
import uk.co.portaltech.tui.model.MobileWebAlertsModel;
import uk.co.portaltech.tui.model.WebAlertsModel;


/**
 * @author gaurav.b
 * 
 */
@UnitTest
public class DefaultAlertFacadeTest
{
	private DefaultAlertFacade alertFacade;
	private AlertsService alertsService;
	private WebAlertsModel webAlertsModel;
	private DarkSiteModel darkSiteModel;
	private AlertPageModel alertPageModel;
	private MobileWebAlertsModel mobileWebAlertsModel;

	@Before
	public void setUp()
	{
		alertFacade = new DefaultAlertFacade();
		alertsService = mock(AlertsService.class);
		webAlertsModel = mock(WebAlertsModel.class);
		darkSiteModel = mock(DarkSiteModel.class);
		alertPageModel = mock(AlertPageModel.class);
		mobileWebAlertsModel = mock(MobileWebAlertsModel.class);

		alertFacade.setAlertsService(alertsService);
	}

	@Test
	public void testGetUpdatedAlertTime()
	{
		final String updatedDate = alertFacade.getUpdatedAlertTime(new Date());
		assertNotNull(updatedDate);
	}

	@Test
	public void testGetWebAlerts()
	{
		given(alertsService.getWebAlerts()).willReturn(webAlertsModel);
		assertNotNull(alertFacade.getWebAlerts());
	}

	@Test
	public void testGetDarkSite()
	{
		given(alertsService.getDarkSite()).willReturn(darkSiteModel);
		assertNotNull(alertFacade.getDarkSite());
	}

	@Test
	public void testGetAlert()
	{
		given(alertsService.getAlert()).willReturn(alertPageModel);
		assertNotNull(alertFacade.getAlert());
	}

	@Test
	public void testGetMobileWebAlerts()
	{
		given(alertsService.getMobileWebAlerts()).willReturn(mobileWebAlertsModel);
		assertNotNull(alertFacade.getMobileWebAlerts());
	}

}
