/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.components.model.LocationKeyFactsComponentModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author veena.pn
 *
 */
public class LocationKeyFactsComponentControllerTest {
	

	

	

	@Mock
	private AccommodationFacade accomodationFacade;
	
	@Mock
	private LocationFacade locationFacade;
	
	@Mock
	private ComponentFacade componentFacade;
	
	@Mock
	 private CategoryService     categoryService;
	    
	@Mock
	 private CMSSiteService      cmsSiteService;
	    
	@Mock
	private CatalogVersionModel model;
	@Mock
	private CatalogModel catalogmodel;
	
	
	private final TUILogUtils LOG = new TUILogUtils("LocationNameComponentControllerTest");
	
	private MediaViewData mediaviewdata;
	
	private LocationData locationData;	
	
	
	private LocationKeyFactsComponentModel locationKeyFactsComponentModel;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		locationData = new LocationData();
		CatalogVersionModel model = new CatalogVersionModel();
		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		model.setCatalog(catalogmodel);
		locationKeyFactsComponentModel = new LocationKeyFactsComponentModel();
		dummyValuesForLocationData();
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
		locationData.setName("Location name");
		locationData.setCount("4");
		locationData.setPriceFrom("$100");
		locationData.setGalleryImages(galleryImages);
		List<Object> currencyCodes=  new ArrayList<Object>();
		currencyCodes.add("Euro");
		currencyCodes.add("Dollar");
		List<Object> languages=  new ArrayList<Object>();
		languages.add("Spanish");
		languages.add("Portuguese");
        Map<String, List<Object>> featureValues = new HashMap<String, List<Object>>();
        featureValues.put("currency", currencyCodes);
        featureValues.put("LANGUAGE",languages);
        locationData.putFeatureCodesAndValues(featureValues);
		locationData.setThingstodoMapUrl("http://www.firstchoice.co.uk");
		locationData.setUrl("http://www.firstchoice.co.uk/holiday/location/essential-info/Spain-ESP");
		return locationData;
	}

	@Test
	public void testviewHeroCarouselComponentForLocation() 
	{		
		String componentUID = "WF_COM_007-1";	
		String categoryCode="ESP";
		CatalogVersionModel model = new CatalogVersionModel();
		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		 LocationModel locationModel=new LocationModel();
		 locationModel.setCatalogVersion(model);
		 locationModel.setCode("ESP");
		 List<Object> result= new ArrayList<Object>();
		 result.add("Spanish");
	    result.add("Portuguese");
        when((LocationModel) categoryService.getCategoryForCode(locationModel.getCatalogVersion(), categoryCode)).thenReturn(locationModel);
        when(locationFacade.getLocationKeyFactsData(locationModel.getCode())).thenReturn(locationData);

		Assert.assertEquals("$100", locationData.getPriceFrom());
		Assert.assertEquals("4", locationData.getCount());
		Assert.assertEquals("http://www.firstchoice.co.uk/holiday/location/essential-info/Spain-ESP", locationData.getUrl());
		Assert.assertEquals("Location name", locationData.getName());
		assertThat(locationData.getFeatureCodesAndValues(), is(notNullValue()));
		Assert.assertEquals(locationData.getFeatureValues("LANGUAGE"),result);

	}
	
	


}
