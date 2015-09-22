/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.tui.components.model.LocationNameComponentModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class LocationNameComponentControllerTest {
	

	

	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("LocationNameComponentControllerTest");
	
	private MediaViewData mediaviewdata;
	
	private LocationData locationData;	
	
	private LocationNameComponentModel locationNameComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		locationData = new LocationData();
		locationNameComponentModel = new LocationNameComponentModel();
		dummyValuesForLocationData();
	}

	
	
	public LocationData dummyValuesForLocationData()
	{
		
		
		mediaviewdata = new MediaViewData();
		mediaviewdata.setCode("0987");
		mediaviewdata.setMime("jpg");
		mediaviewdata.setSize("Small");
		mediaviewdata.setDescription("small image");
		List<MediaViewData> galleryImages =  new ArrayList<MediaViewData>();
		galleryImages.add(mediaviewdata);		
		locationData.setLocationType(LocationType.COUNTRY);
		locationData.setName("Location name");
		locationData.setCount("4");
		locationData.setPriceFrom("$100");
		locationData.setGalleryImages(galleryImages);
		locationData.setThingstodoMapUrl("http://www.firstchoice.co.uk");
		locationData.setUrl("http://www.firstchoice.co.uk/holiday/location/essential-info/Spain-ESP");
		return locationData;
	}

	@Test
	public void testviewHeroCarouselComponentForLocation() 
	{		
		String componentUID = "WF_COM_007-1";	
		String categoryCode="ESP";
		
		when(componentFacade.getHeroCarouselViewData(componentUID, categoryCode, "location")).thenReturn(locationData);

		Assert.assertEquals("$100", locationData.getPriceFrom());
		Assert.assertEquals("4", locationData.getCount());
		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/location/essential-info/Spain-ESP", locationData.getUrl());
		Assert.assertEquals("Location name", locationData.getName());


		

	}
	
	
	




}
