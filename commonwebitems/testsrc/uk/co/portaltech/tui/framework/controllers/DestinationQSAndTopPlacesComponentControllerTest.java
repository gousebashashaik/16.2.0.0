/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.enterprisedt.util.debug.Logger;

import uk.co.portaltech.tui.components.model.DestinationQSAndTopPlacesComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author venkataharish.k
 * 
 */

public class DestinationQSAndTopPlacesComponentControllerTest {

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private TopPlacesWrapper wrapper;

	private LocationData locationData;
	private DestinationQSAndTopPlacesComponentModel component;
	
	
	private final TUILogUtils LOG = new TUILogUtils("DestinationQSAndTopPlacesComponentControllerTest");

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);	
		locationData = new LocationData();
		component = new DestinationQSAndTopPlacesComponentModel();
		dummyDataForComponent();
		dummyDataforTopPlaces();
	}

	private void dummyDataForComponent()
	{
		component.setInspirationURL("http://www.firstchoice.co.uk/holiday/destinations");
		component.setTitle("Find Your Perfect Holiday Destination");
		component.setLocationTitle("Top 5 Destinations");
		component.setAccommTitle("Top 5 Hotels");
		component.setInspirationTitle("Not sure where to go on Holiday?");
		component.setInspirationDesc("Need some inspiration?");
	}
	
	private void dummyDataforTopPlaces()
	{
		locationData.setThingstodoMapUrl("http://www.firstchoice.uk");
		locationData.setCount("4");
		wrapper.setLocation(locationData);
	}
	
	@Test
	public void testViewComponent()
	{		
		final String componentUid = "WF-COM-906-1";
		try {
			Mockito.when((DestinationQSAndTopPlacesComponentModel) componentFacade.getComponent(componentUid)).thenReturn(component);
		} catch (NoSuchComponentException e) {
			LOG.error("No such component available");
		}
		Mockito.when(componentFacade.getTopPlacesForDestQS(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.any(DestinationQSAndTopPlacesComponentModel.class))).thenReturn(wrapper);
		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/destinations", component.getInspirationURL());
		Assert.assertEquals("Top 5 Hotels", component.getAccommTitle());
	}
}
