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

import org.apache.commons.lang.StringUtils;
import org.fest.util.Collections;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.components.model.WhenToGoEditorialComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.async.logging.TUILogUtils;

import org.apache.log4j.Logger;

/**
 * @author veena.pn
 *
 */
public class HeroCarouselComponentControllerTest {
	

	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("HeroCarouselComponentControllerTest");
	
	private AccommodationViewData accommodationViewData;
	
	private MediaViewData mediaviewdata;
	
	private LocationData locationData;	
	
	private HeroCarouselComponentModel heroCarouselComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		locationData = new LocationData();
		heroCarouselComponentModel = new HeroCarouselComponentModel();
		
		dummyValuesForAccommodationViewData();
		
		dummyValuesForLocationData();
	}

	public AccommodationViewData dummyValuesForAccommodationViewData()
	{  
		
		mediaviewdata =new MediaViewData();
		mediaviewdata.setCode("0987");
		mediaviewdata.setMime("jpg");
		mediaviewdata.setSize("Small");
		mediaviewdata.setDescription("small image");
		List<MediaViewData> galleryImages =  new ArrayList<MediaViewData>();
		galleryImages.add(mediaviewdata);
		accommodationViewData.setPriceFrom("$100");
		accommodationViewData.setStayPeriod("7 Days");
		accommodationViewData.setAccommodationType("Hotel");
		accommodationViewData.setDeparturePoint("Bangalore");
		accommodationViewData.setDepartureCode("BGLR");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setReviewRating("4.5");
		accommodationViewData.setLocationMapUrl("http://www.firstchoice.co.uk");
		accommodationViewData.setGalleryImages(galleryImages);
		accommodationViewData.setUrl("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164");
		return accommodationViewData;
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
		assertThat(locationData.getGalleryImages() , is(notNullValue()));

	}
	
	
	@Test
	public void testviewHeroCarouselComponentForAccommodation() 
	{		
		String componentUID = "WF_COM_007-2";		
		String productCode="Premier";
		when( componentFacade.getHeroCarouselViewData(componentUID, productCode, "accommodation")).thenReturn(accommodationViewData);
		
		Assert.assertEquals("7 Days", accommodationViewData.getStayPeriod());
		Assert.assertEquals("4.5", accommodationViewData.getReviewRating());
		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164", accommodationViewData.getUrl());
		assertThat(accommodationViewData.getGalleryImages() , is(notNullValue()));
	}


}
