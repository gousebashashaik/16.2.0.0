/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.enterprisedt.util.debug.Logger;

import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.BookingData;

/**
 * @author arun.y
 *
 */

@UnitTest
public class BookingComponentControllerTest {
	
	@Mock
	private ComponentFacade componentFacade;
	
	private final Logger LOG = Logger.getLogger(BookingComponentControllerTest.class);
	
	private AccommodationViewData accommodationViewData;
	
	private BookingData bookingData;
	
	private SimpleCMSComponentModel simpleCMSComponentModel;
	
	private static final String UID = "WF_COM_072-1";
	
	private static final String PCODE = "SplashWorld";

	@Before
	public void setUp() throws Exception 
	{	
		MockitoAnnotations.initMocks(this);
		accommodationViewData = new AccommodationViewData();
		bookingData = new BookingData();
		simpleCMSComponentModel = new SimpleCMSComponentModel();
		
		dummyValuesForBookingData();
		
		dummyValuesForAccommodationViewData();
	}
	
	public BookingData dummyValuesForBookingData()
	{
		bookingData.setComponentUid(UID);
        bookingData.setProductCode(PCODE);
        bookingData.setCategoryCode("FSP");
        bookingData.setSiteUrl("http://www.firstchoice.co.uk");
        return bookingData;
	}
	
	public AccommodationViewData dummyValuesForAccommodationViewData()
	{
		accommodationViewData.setPriceFrom("$100");
		accommodationViewData.setStayPeriod("7 Days");
		accommodationViewData.setAccommodationType("Hotel");
		accommodationViewData.setDeparturePoint("London");
		accommodationViewData.setRoomOccupancy("4");
		accommodationViewData.setReviewRating("4.5");
		accommodationViewData.setBookingData(dummyValuesForBookingData());
		return accommodationViewData;
	}


	@Test
	public void testViewComponent() 
	{
		when(componentFacade.getAccommodationPrice(Mockito.anyString(), Mockito.anyString())).thenReturn(accommodationViewData);

		try
		{
			when(componentFacade.getComponent(UID)).thenReturn(simpleCMSComponentModel);
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		
		Assert.assertEquals("WF_COM_072-1", bookingData.getComponentUid());
		Assert.assertEquals("SplashWorld", bookingData.getProductCode());
		Assert.assertEquals("FSP", bookingData.getCategoryCode());
		Assert.assertEquals("$100", accommodationViewData.getPriceFrom());
		Assert.assertEquals("London", accommodationViewData.getDeparturePoint());
		Assert.assertEquals("4.5", accommodationViewData.getReviewRating());
	}
}
