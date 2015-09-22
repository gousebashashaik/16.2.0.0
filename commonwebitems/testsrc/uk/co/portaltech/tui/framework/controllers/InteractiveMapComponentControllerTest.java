package uk.co.portaltech.tui.framework.controllers;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.MapFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;


/**
 * @author gopinath.n
 * 
 */
public class InteractiveMapComponentControllerTest
{

	
	private final TUILogUtils LOGGER = new TUILogUtils("InteractiveMapComponentControllerTest");

	private static final String COMPONENT_UID = "WF_COM_002-1";
	private static final String CATEGORY_CODE = "africa,asia,caribbean,europe,north america,south america";
	private static final String PAGE_TYPE = "pageType";
	private static final String SEO_TYPE = "seoType";
	private static final String PRODUCT_CODE = "000351";


	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private SimpleCMSComponentModel componentModel;


	private MapDataWrapper mapDataWrapper;

	private AccommodationViewData viewData;

	@Mock
	private MapFacade mapFacade;


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	public MapDataWrapper dummydataforMapDataWrapper()
	{
		mapDataWrapper = new MapDataWrapper();
		mapDataWrapper.setLocationType("locationType");
		mapDataWrapper.setTopLocationName("topLocationName");
		return mapDataWrapper;
	}

	public void dummydataforAccommodationViewData()
	{
		viewData = new AccommodationViewData();
		viewData.setUrl("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sharm-El-Sheikh");

	}

	@Test
	public void testGetInteractiveMapData()
	{
		try
		{
			Mockito.when(componentFacade.getComponent(COMPONENT_UID)).thenReturn(componentModel);
		}
		catch (final NoSuchComponentException e)
		{
			LOGGER.error("No Such Component Found" + e);
		}
		Mockito.when(mapFacade.getInteractiveMapForLocations(CATEGORY_CODE)).thenReturn(dummydataforMapDataWrapper());
		try
		{
			Mockito.when(componentFacade.getInteractiveMapData(CATEGORY_CODE, PAGE_TYPE, SEO_TYPE, COMPONENT_UID)).thenReturn(
					dummydataforMapDataWrapper());
		}
		catch (final NoSuchComponentException e)
		{
			LOGGER.error("No Such Component Found" + e);
		}
		Mockito.when(mapFacade.getInteractiveMapForBookAccommodation(PRODUCT_CODE)).thenReturn(dummydataforMapDataWrapper());
		Mockito.when(mapFacade.getInteractiveMapForAccommodation(PRODUCT_CODE)).thenReturn(dummydataforMapDataWrapper());

		try
		{
			Assert.assertNotNull(COMPONENT_UID, componentFacade.getComponent(COMPONENT_UID));
		}
		catch (final NoSuchComponentException e)
		{
			LOGGER.error("No Such component found" + e);
		}
	}
}
