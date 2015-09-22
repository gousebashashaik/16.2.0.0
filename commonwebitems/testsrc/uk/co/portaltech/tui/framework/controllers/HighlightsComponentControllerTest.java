/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.components.model.HighlightsComponentModel;
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
public class HighlightsComponentControllerTest {
	
	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
    @Mock
    private CategoryService categoryService;
    
	
	private final Logger LOG = Logger.getLogger(HighlightsComponentControllerTest.class);
	
	
	private LocationData locationData;	
	
	private AccommodationViewData accommodationViewData;	
	private HighlightsComponentModel highlightsComponentModel;
	
	
	 List<Object> featureValues =
             new ArrayList(Arrays.asList(new String[] { "entertainment", "Spa" }));
     
	


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		locationData = new LocationData();
		accommodationViewData = new AccommodationViewData();
		highlightsComponentModel = new HighlightsComponentModel();
      
		dummyValuesForLocationData();
		dummyValuesForAccommodationViewData();
	}

	
	
	public LocationData dummyValuesForLocationData()
	{
		locationData.setLocationType(LocationType.RESORT);
		locationData.setCount("4");
		locationData.putFeatureValue("usps", featureValues);
		locationData.setUrl("http://localhost:9001/holiday/location/essential-info/Spain-ESP");

		return locationData;
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

	@Test
	public void testviewHighlightsForLocation() 
	{		
		String componentUID = "WF_COM_009-1";	
		String categoryCode="ESP";
		  CategoryModel categoryModel = categoryService.getCategoryForCode(categoryCode);
		  if(categoryModel instanceof LocationModel)
		  {
		   when(componentFacade.getLocationHighlights(componentUID, categoryCode)).thenReturn(locationData);
		  }
						
		try
		{
			when((HighlightsComponentModel) componentFacade.getComponent(componentUID)).thenReturn(highlightsComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		

		Assert.assertEquals("http://localhost:9001/holiday/location/essential-info/Spain-ESP", locationData.getUrl());
		Assert.assertEquals("4", locationData.getCount());
		
	}
	
	@Test
	public void testviewHighlightsForAccommodation() 
	{		
		String componentUID = "WF_COM_009-1";	
		String productCode="SpalshWorld";
		
		   when(componentFacade.getAccommodationHighlights(componentUID, productCode)).thenReturn(accommodationViewData);
		  
						
		try
		{
			when((HighlightsComponentModel) componentFacade.getComponent(componentUID)).thenReturn(highlightsComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		

		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/accommodation/overview/Sarigerme/Holiday-Village-Turkey-030164", accommodationViewData.getUrl());
		Assert.assertEquals("Hotel", accommodationViewData.getAccommodationType());
		
	}
	
}
