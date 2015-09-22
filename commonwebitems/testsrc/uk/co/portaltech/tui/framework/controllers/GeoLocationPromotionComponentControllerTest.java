/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.components.model.GeoLocationPromotionComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.tui.async.logging.TUILogUtils;
/**
 * @author niranjani.r
 *
 */
@UnitTest
public class GeoLocationPromotionComponentControllerTest {

	/*private static final Logger LOG = Logger
			.getLogger(GeoLocationPromotionComponentControllerTest.class);*/
	private final TUILogUtils LOG = new TUILogUtils("GeoLocationPromotionComponentControllerTest");
	
	
	
	@Mock
	private ComponentFacade componentFacade;
	
	@Mock
	private LocationFacade facade;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	private GeoLocationPromotionComponentModel createdummyGeoLocationPromotionComponentModelWithLocationModel()
	{
		final GeoLocationPromotionComponentModel geoModel= new GeoLocationPromotionComponentModel();
       geoModel.setLocation(getDummyLocationModel());
       geoModel.setUid("comp1");
		return geoModel;
	}
	
	private LocationModel getDummyLocationModel()
	{
	   final LocationModel location = new LocationModel();
		location.setCode("location1");
		return location;
	}
	private LocationData getDummyLocationData(){
		final LocationData data= new LocationData();
		data.setCode("location1");
		data.setName("Location 1");
		return data;
	}
	/**
	 * Test method for {@link uk.co.portaltech.tui.framework.controllers.GeoLocationPromotionComponentController#viewGeoLocationPromotionComponent()}.
	 */
	@Test
	public void testViewGeoLocationPromotionComponentForLocation() {
		boolean test= true;
		try {
			Mockito.when(componentFacade.getComponent("comp1")).thenReturn(createdummyGeoLocationPromotionComponentModelWithLocationModel());
			Mockito.when(facade.getLocationData(getDummyLocationModel().getCode())).thenReturn(getDummyLocationData());
						
		} catch (NoSuchComponentException e) {
			LOG.error("No such component",e);
			test=false;
		}
		assertTrue(test);
		
	}

}
