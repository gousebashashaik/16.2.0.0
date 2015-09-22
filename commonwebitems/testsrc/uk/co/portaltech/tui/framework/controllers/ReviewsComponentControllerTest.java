/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.ReviewsComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.TripadvisorData;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class ReviewsComponentControllerTest {
	
	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	private final Logger LOG = Logger.getLogger(ReviewsComponentControllerTest.class);
	
	private AccommodationViewData accommodationViewData;
	
	private TripadvisorData tripadvisorData;	
	
	private ReviewsComponentModel reviewsComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		tripadvisorData = new TripadvisorData();
		reviewsComponentModel = new ReviewsComponentModel();
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
		tripadvisorData.setRatingBar("ratingBar");
		tripadvisorData.setReviewsUrl("http://firstchoice.co.uk/holiday/destinations");
		tripadvisorData.setReviewsCount(100);
		accommodationViewData.setTripadvisorData(tripadvisorData);
		return accommodationViewData;
	}
	
	

	@SuppressWarnings("boxing")
	@Test
	public void testViewtripAdvisorSummaryComponent() 
	{		
		String componentUID = "WF_COM_040-1";	
		
		
		
		try
		{
			when((ReviewsComponentModel) componentFacade.getComponent(componentUID)).thenReturn(reviewsComponentModel);
		    when( accomodationFacade.getTripAdvisorReviewData("ProductCode", 5)).thenReturn(accommodationViewData);
		
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("7 Days", accommodationViewData.getStayPeriod());
		Assert.assertEquals("4.5", accommodationViewData.getReviewRating());
		assertThat(accommodationViewData.getTripadvisorData().getReviewsCount(),is(100));
		Assert.assertEquals(accommodationViewData.getTripadvisorData().getReviewsUrl(), "http://firstchoice.co.uk/holiday/destinations");
		
	}

}
