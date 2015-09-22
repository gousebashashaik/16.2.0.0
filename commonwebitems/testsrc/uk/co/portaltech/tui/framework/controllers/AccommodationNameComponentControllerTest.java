/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.AccommodationNameComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;


/**
 * @author gopinath.n
 * 
 */
public class AccommodationNameComponentControllerTest
{

	/**
	 * @throws java.lang.Exception
	 */

	

	@Mock
	private AccommodationFacade accommodationFacade;

	@Mock
	private ComponentFacade componentFacade;

	private AccommodationViewData accommodationViewData;

	private AccommodationNameComponentModel componentModel;



	private static final String CODE = "WF_COM_070-1";
	private static final String NAME = "Accommodation Name";
	private static final String REVIEW_RATING = "5";

	
	private final TUILogUtils LOG = new TUILogUtils("AccommodationNameComponentControllerTest");

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}


	public AccommodationViewData dummyDataforAccommodationViewData()
	{
		accommodationViewData = new AccommodationViewData();
		accommodationViewData.setAccommodationType("accommodationType");
		accommodationViewData.setCode(CODE);
		accommodationViewData.setName(NAME);
		accommodationViewData.setPriceFrom("3000");
		accommodationViewData.setReviewRating(REVIEW_RATING);
		accommodationViewData.setUrl("http://www.firstchoice.co.uk/holiday/location/overview");
		return accommodationViewData;
	}

	public AccommodationNameComponentModel dummyDataforAccommodationNameComponentModel()
	{
		componentModel = new AccommodationNameComponentModel();
		componentModel.setUid(CODE);
		componentModel.setName(NAME);

		return componentModel;

	}


	@Test
	public void testGetAccommodationName()
	{
		Mockito.when(accommodationFacade.getAccommodationNameComponentData(CODE)).thenReturn(dummyDataforAccommodationViewData());
		try
		{
			Mockito.when(componentFacade.getComponent(CODE)).thenReturn(dummyDataforAccommodationNameComponentModel());
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No such component" + e);
			e.printStackTrace();
		}
		Assert.assertEquals(CODE, accommodationViewData.getCode());
		Assert.assertEquals(NAME, accommodationViewData.getName());
		Assert.assertNotNull(REVIEW_RATING, accommodationViewData.getReviewRating());
		
		Assert.assertEquals(CODE, componentModel.getUid());
		Assert.assertEquals(NAME, componentModel.getName());
	}

}
