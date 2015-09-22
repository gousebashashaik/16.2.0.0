/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

import com.enterprisedt.util.debug.Logger;


/**
 * @author gopinath.n
 * 
 */
public class DealsComponentControllerTest
{

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private SimpleCMSComponentModel component;

	private AccommodationViewData viewData;

	@Mock
	private List<AccommodationViewData> dealsData;


	private static final String COMPONENT_UID = "WF_COM_070-1";

	private final Logger logger = Logger.getLogger(DealsComponentControllerTest.class);

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		viewData = new AccommodationViewData();
	}

	public List<AccommodationViewData> dummydataforAccommodationViewData()
	{

		viewData.setCode("code");
		viewData.setAccommodationType("accommodationType");
		viewData.setPriceFrom("2000");
		dealsData.add(viewData);
		return dealsData;

	}

	@Test
	public void testViewDealsComponent()
	{
		try
		{
			Mockito.when(
					componentFacade.getDealsData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
							Mockito.anyString())).thenReturn(dealsData);
		}
		catch (final NoSuchComponentException e)
		{
			logger.error("No Such Component Found");
			e.printStackTrace();
		}
		try
		{
			Mockito.when(componentFacade.getComponent(COMPONENT_UID)).thenReturn(component);
		}
		catch (final NoSuchComponentException e)
		{
			logger.error("No component with uid [" + COMPONENT_UID + "]");
			e.printStackTrace();
		}
		Assert.assertEquals(dealsData, dummydataforAccommodationViewData());
	}

	@Test
	public void testGetDealsData()
	{
		try
		{
			Mockito.when(
					componentFacade.getDealsData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
							Mockito.anyString())).thenReturn(dealsData);
		}
		catch (final NoSuchComponentException e)
		{
			logger.error("No Such Component Found");
			e.printStackTrace();
		}
		Assert.assertNotNull(dealsData);
		Assert.assertEquals(dealsData, dummydataforAccommodationViewData());
	}
}
