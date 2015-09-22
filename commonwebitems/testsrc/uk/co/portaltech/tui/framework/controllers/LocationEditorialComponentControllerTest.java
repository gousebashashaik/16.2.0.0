/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.ArrayList;
import java.util.List;

import com.enterprisedt.util.debug.Logger;

import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.tui.components.model.LocationEditorialComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;

/**
 * @author arun.y
 *
 */

@UnitTest
public class LocationEditorialComponentControllerTest {
	
	@Mock
	private ComponentFacade componentFacade;
    
    @Mock
    private LocationFacade  locationFacade;
    
    @Mock
    private AccommodationFacade accommodationFacade;
    
    private LocationEditorialComponentModel locationEditorialComponentModel;
    
    private final Logger LOG = Logger.getLogger(LocationEditorialComponentControllerTest.class);
	
	private CatalogVersionModel catalogVersionModel;
	
	private CatalogModel catalogModel;
	
	private List<AccommodationViewData> accViewDataList;
	
	private AccommodationViewData accViewData;
	
	private List<LocationData> locDataList;
	
	private LocationData locData;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		locationEditorialComponentModel = new LocationEditorialComponentModel();
		catalogVersionModel = new CatalogVersionModel();
		catalogModel = new CatalogModel();
		accViewDataList = new ArrayList<AccommodationViewData>();
		accViewData = new AccommodationViewData();
		locDataList = new ArrayList<LocationData>();
		locData = new LocationData();
		
		catalogModel.setId("fc-catalog");
        catalogVersionModel.setCatalog(catalogModel);
        catalogVersionModel.setVersion("Online");
        
        getLocationEditorialComponentInfo();
        getLocDataListInfo();
        getAccViewDataListInfo();
	}
	
	public LocationEditorialComponentModel getLocationEditorialComponentInfo()
	{
		locationEditorialComponentModel.setUid("WF_COM_050-1");
		locationEditorialComponentModel.setVisibleItems(Integer.valueOf(30));
		locationEditorialComponentModel.setCatalogVersion(catalogVersionModel);
		locationEditorialComponentModel.setName("Location Editorial Component");
		return locationEditorialComponentModel;
	}
	
	public List<LocationData> getLocDataListInfo()
	{
		locData.setLocationType(LocationType.COUNTRY);
		locData.setThingstodoMapUrl("/holiday/location/things-to-do/Spain-ESP");
		locData.setLegacySystemUrl("holiday/location/overview/Spain-ESP");
		locData.setGalleryImageVisibleItems(30);
		locData.setCount("10");
		locDataList.add(locData);
		return locDataList;
	}
	
	public List<AccommodationViewData> getAccViewDataListInfo()
	{
		accViewData.setReviewRating("4");
		accViewData.setStayPeriod("7 Days");
		accViewData.setPriceFrom("$250");
		accViewData.setGalleryImageVisibleItems(30);
		accViewData.settRating("4 Plus");
		accViewData.setDeparturePoint("Spain");
		accViewData.setDepartureCode("ESP");
		accViewDataList.add(accViewData);
		return accViewDataList;
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.framework.controllers.LocationEditorialComponentController#viewTabbedComponent()}.
	 */
	@Test
	public void testViewTabbedComponent() 
	{
		try
		{
			when(componentFacade.getComponent(locationEditorialComponentModel.getUid())).thenReturn(locationEditorialComponentModel);
			when(locationFacade.getChildLocationsData(Mockito.anyString(), Mockito.anyInt())).thenReturn(locDataList);
			when(accommodationFacade.getAccommodations(Mockito.anyString(), Mockito.anyInt())).thenReturn(accViewDataList);
			assertThat(locationEditorialComponentModel.getUid(), is("WF_COM_050-1"));
			assertThat(locationEditorialComponentModel.getVisibleItems(), is(Integer.valueOf(30)));
			assertThat(locationEditorialComponentModel.getName(), is("Location Editorial Component"));
			assertThat(locDataList, is(notNullValue()));
			assertThat(locData.getThingstodoMapUrl(), is("/holiday/location/things-to-do/Spain-ESP"));
			assertThat(locData.getLocationType(), is(LocationType.COUNTRY));
			assertThat(accViewDataList, is(notNullValue()));
			assertThat(accViewData.getDepartureCode(), is("ESP"));
			assertThat(accViewData.gettRating(), is("4 Plus"));
			assertThat(accViewData.getStayPeriod(), is("7 Days"));
		}
		catch(NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
	}

}
