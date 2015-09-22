/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.AccommodationRoomsComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class AccommodationRoomsComponentControllerTest {
	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	private final Logger LOG = Logger.getLogger(AccommodationRoomsComponentControllerTest.class);
	
	private AccommodationViewData accommodationViewData;
	
	private LocationData locationData;	
	
	private AccommodationRoomsComponentModel accommodationRoomsComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		locationData = new LocationData();
		accommodationRoomsComponentModel = new AccommodationRoomsComponentModel();
		
		dummyValuesForAccommodationViewData();
		
		
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
	
	

	@Test
	public void testViewAccommodationRoomsComponent() 
	{		
		String componentUID = "WF_COM_046-1";	
		List<Object> result= new ArrayList<Object>();
		 result.add("This is intro1");
		
		
		 when( accomodationFacade.getRoomsForAccommodation("Premier", 0)).thenReturn(accommodationViewData);
		try
		{
			when((AccommodationRoomsComponentModel) componentFacade.getComponent(componentUID)).thenReturn(accommodationRoomsComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("7 Days", accommodationViewData.getStayPeriod());
		Assert.assertEquals("4.5", accommodationViewData.getReviewRating());
		
	}


}
