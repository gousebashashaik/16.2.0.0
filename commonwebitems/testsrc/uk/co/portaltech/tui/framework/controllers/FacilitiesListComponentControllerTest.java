/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.FacilitiesListComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class FacilitiesListComponentControllerTest {
	

	

	

	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("LocationNameComponentControllerTest");
	
	private MediaViewData mediaviewdata;
	
	private AccommodationViewData accommodationViewData;	
	
	private FacilitiesListComponentModel facilitiesListComponentModel;
	
	private FacilityViewData facilityViewData ;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData=new AccommodationViewData();
		facilityViewData= new FacilityViewData();
		facilitiesListComponentModel = new FacilitiesListComponentModel();
		dummyValuesForAccommodationViewData();
	}

	public AccommodationViewData dummyValuesForAccommodationViewData()
	{  
		
		mediaviewdata =new MediaViewData();
		mediaviewdata.setCode("0987");
		mediaviewdata.setMime("jpg");
		mediaviewdata.setSize("Small");
		mediaviewdata.setDescription("small image");
		List<MediaViewData> galleryImages =  new ArrayList<MediaViewData>();
		List<FacilityViewData> facilitylist = new ArrayList<FacilityViewData>();
		galleryImages.add(mediaviewdata);
		accommodationViewData.setPriceFrom("$100");
		accommodationViewData.setStayPeriod("7 Days");
		accommodationViewData.setAccommodationType("Hotel");
		accommodationViewData.setDeparturePoint("Bangalore");
		accommodationViewData.setDepartureCode("BGLR");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setReviewRating("4.5");
		accommodationViewData.setLocationMapUrl("http://www.firstchoice.co.uk");
		facilityViewData.setDescription("Description1");
		facilityViewData.setFacilityType("facilityType1");
		facilityViewData.setName("name1");
		facilitylist.add(facilityViewData);
		
		accommodationViewData.setGalleryImages(galleryImages);
		accommodationViewData.setFacilities(facilitylist);
		accommodationViewData.setUrl("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164");
		return accommodationViewData;
	}
	
	
	@Test
	public void testviewFacilitiesListComponent() 
	{		
		String componentUID = "WF_COM_069-1";	
		String productCode="027942";
		
		when(accomodationFacade.getAccommodationFacilities(productCode)).thenReturn(accommodationViewData);
		try
		{
			when((FacilitiesListComponentModel) componentFacade.getComponent(componentUID)).thenReturn(facilitiesListComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("$100", accommodationViewData.getPriceFrom());
		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164", accommodationViewData.getUrl());
		assertThat(accommodationViewData.getFacilities(), is(notNullValue()));

		

	}
	
	
	






}
