/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.AccommodationEditorialIntroductionComponentModel;
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
public class AccommodationEditorialIntroductionComponentControllerTest {
	
	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("AccommodationEditorialIntroductionComponentControllerTest");
	
	private AccommodationViewData accommodationViewData;
	
	private LocationData locationData;	
	
	private AccommodationEditorialIntroductionComponentModel accommodationEditorialIntroductionComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		locationData = new LocationData();
		accommodationEditorialIntroductionComponentModel = new AccommodationEditorialIntroductionComponentModel();
		
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
		List<Object> introduction=  new ArrayList<Object>();
		introduction.add("This is intro1");
        Map<String, List<Object>> featureValues = new HashMap<String, List<Object>>();
        featureValues.put("introduction", introduction);
        accommodationViewData.putFeatureCodesAndValues(featureValues);
		return accommodationViewData;
	}
	
	

	@Test
	public void testViewAccommodationEditorIntroComponent() 
	{		
		String componentUID = "WF_COM_071-1";	
		List<Object> result= new ArrayList<Object>();
		 result.add("This is intro1");
		
		
		 when(accomodationFacade.getRequiredAccommodationEditorialIntroduction("Premier")).thenReturn(accommodationViewData);
		try
		{
			when((AccommodationEditorialIntroductionComponentModel) componentFacade.getComponent(componentUID)).thenReturn(accommodationEditorialIntroductionComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("7 Days", accommodationViewData.getStayPeriod());
		Assert.assertEquals("4.5", accommodationViewData.getReviewRating());
		assertThat(accommodationViewData.getFeatureCodesAndValues(), is(notNullValue()));
		Assert.assertEquals(accommodationViewData.getFeatureValues("introduction"),result);
	}

}
