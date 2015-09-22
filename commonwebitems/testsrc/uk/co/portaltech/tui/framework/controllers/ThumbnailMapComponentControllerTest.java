/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.tui.components.model.HighlightsComponentModel;
import uk.co.portaltech.tui.components.model.ThumbnailMapComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class ThumbnailMapComponentControllerTest {
	


	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("ThumbnailMapComponentControllerTest");
	
	private AccommodationViewData accommodationViewData;
	
	private LocationData locationData;	
	
	private ThumbnailMapComponentModel thumbnailMapComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		locationData = new LocationData();
		thumbnailMapComponentModel = new ThumbnailMapComponentModel();
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
		accommodationViewData.setUrl("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164");
		return accommodationViewData;
	}
	
	public LocationData dummyValuesForLocationData()
	{
		locationData.setLocationType(LocationType.COUNTRY);
		locationData.setCount("4");
		locationData.setPriceFrom("$100");
		locationData.setThingstodoMapUrl("http://www.firstchoice.co.uk");
		locationData.setUrl("http://www.firstchoice.co.uk/holiday/location/essential-info/Spain-ESP");
		return locationData;
	}

	@Test
	public void testviewThumbnailMapDataForlLocation() 
	{		
		String componentUID = "WF_COM_012-1";	
		String categoryCode="ESP";
		
		when(locationFacade.getThumbnailMapData(categoryCode)).thenReturn(locationData);
		
		try
		{
			when((ThumbnailMapComponentModel) componentFacade.getComponent(componentUID)).thenReturn(thumbnailMapComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("$100", locationData.getPriceFrom());
		Assert.assertEquals("4", locationData.getCount());
		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/location/essential-info/Spain-ESP", locationData.getUrl());
		Assert.assertEquals("http://www.firstchoice.co.uk", locationData.getThingstodoMapUrl());

	}
	
	
	@Test
	public void testviewThumbnailMapDataForAccommodation() 
	{		
		String componentUID = "WF_COM_012-1";	
		String productCode="SpalshWorld";
		
		   when(accomodationFacade.getAccommodationThumbnailMapData(productCode)).thenReturn(accommodationViewData);
		  
						
		try
		{
			when((ThumbnailMapComponentModel) componentFacade.getComponent(componentUID)).thenReturn(thumbnailMapComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		

		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164", accommodationViewData.getUrl());
		Assert.assertEquals("Hotel", accommodationViewData.getAccommodationType());
		Assert.assertEquals("http://www.firstchoice.co.uk", accommodationViewData.getLocationMapUrl());

		
	}
	
	
	
	

}
