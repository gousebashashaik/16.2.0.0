/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.bootstrap.annotations.UnitTest;

import com.enterprisedt.util.debug.Logger;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.tui.components.model.AccommodationLocationComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author arun.y
 *
 */

@UnitTest
public class AccommodationLocationComponentControllerTest {
	
	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	//private final Logger LOG = Logger.getLogger(AccommodationLocationComponentControllerTest.class);
	private final TUILogUtils LOG = new TUILogUtils("AccommodationLocationComponentControllerTest");
	
	private AccommodationViewData accommodationViewData;
	
	private LocationData locationData;	
	
	private AccommodationLocationComponentModel accommodationLocationComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		locationData = new LocationData();
		accommodationLocationComponentModel = new AccommodationLocationComponentModel();
		
		dummyValuesForAccommodationViewData();
		
		dummyValuesForLocationData();
	}

	public AccommodationViewData dummyValuesForAccommodationViewData()
	{
		accommodationViewData.setPriceFrom("$100");
		accommodationViewData.setStayPeriod("7 Days");
		accommodationViewData.setAccommodationType("Hotel");
		accommodationViewData.setDeparturePoint("Bangalore");
		accommodationViewData.setDepartureCode("BGLR");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setReviewRating("4.5");
		accommodationViewData.setLocationMapUrl("http://www.firstchoice.co.uk");
		return accommodationViewData;
	}
	
	public LocationData dummyValuesForLocationData()
	{
		locationData.setLocationType(LocationType.RESORT);
		locationData.setCount("4");
		locationData.setPriceFrom("$100");
		locationData.setThingstodoMapUrl("http://www.firstchoice.co.uk");
		return locationData;
	}

	@Test
	public void testViewAccommodationLocationInfoComponent() 
	{		
		String componentUID = "WF_COM_071-1";		
		
		when(accomodationFacade.getAccommodationLocationInfo("Premier")).thenReturn(accommodationViewData);
		when(locationFacade.getLocationForAccommodation("SplashWorld")).thenReturn(locationData);
		
		try
		{
			when((AccommodationLocationComponentModel) componentFacade.getComponent(componentUID)).thenReturn(accommodationLocationComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("7 Days", accommodationViewData.getStayPeriod());
		Assert.assertEquals("4.5", accommodationViewData.getReviewRating());
		Assert.assertEquals("$100", locationData.getPriceFrom());
		Assert.assertEquals("4", locationData.getCount());
	}
}
