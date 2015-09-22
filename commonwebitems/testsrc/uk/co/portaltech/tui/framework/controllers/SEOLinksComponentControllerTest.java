/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;

import de.hybris.platform.category.CategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.SEOLinksComponentModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class SEOLinksComponentControllerTest {
	

	
	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	
    @Mock
    private CategoryService categoryService;
    
	
	
    private final TUILogUtils LOG = new TUILogUtils("SEOLinksComponentControllerTest");
	
	
	private LocationData locationData;	
	
	private SEOLinksComponentModel sEOLinksComponentModel;
	
	
	 List<Object> featureValues =
             new ArrayList(Arrays.asList(new String[] { "entertainment", "Spa" }));
     
	


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		locationData = new LocationData();
		sEOLinksComponentModel = new SEOLinksComponentModel();
      
		dummyValuesForLocationData();
		dummySEOLinksComponentModel();
	}

	
	
	public LocationData dummyValuesForLocationData()
	{
		locationData.setCount("4");
		locationData.putFeatureValue("usps", featureValues);
		locationData.setUrl("http://localhost:9001/holiday/location/essential-info/Spain-ESP");
		locationData.setCode("ESP");
		return locationData;
	}
	
	@SuppressWarnings("boxing")
	public SEOLinksComponentModel dummySEOLinksComponentModel()
	{
		sEOLinksComponentModel.setUid("WF_COM_801");
		sEOLinksComponentModel.setVisible(true);
		sEOLinksComponentModel.setName("SeoLinks");
		return sEOLinksComponentModel;
	}

	@Test
	public void testviewSeoLinksComponent() 
	{		
		String locationCode="ESP";
		
		   when(locationFacade.getLocationData(locationCode)).thenReturn(locationData);
		
		Assert.assertEquals("http://localhost:9001/holiday/location/essential-info/Spain-ESP", locationData.getUrl());
		Assert.assertEquals("ESP", locationData.getCode());
		
	}
	
	
	


}
