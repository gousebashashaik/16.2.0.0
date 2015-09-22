/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.PlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.PlacesToStayWrapper;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author niranjani.r
 *
 */
public class PlacesToStayCarouselComponentControllerTest {

    @Mock
    private AccommodationFacade accomodationFacade;
       
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
    
    PlacesToStayCarouselComponentModel placesToStay= new PlacesToStayCarouselComponentModel();
    PlacesToStayWrapper wrapper= new PlacesToStayWrapper();
        
    
    private final TUILogUtils LOG = new TUILogUtils("PlacesToStayCarouselComponentControllerTest");

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CatalogVersionModel model = new CatalogVersionModel();
        CatalogModel catalog = new CatalogModel();
        catalog.setId("fc-catalog");
        model.setCatalog(catalog);
        model.setVersion("Online");
        model.setCatalog(catalogmodel);
        getDummyCarouselModel();
        getDummywrapper();
    }
    private PlacesToStayCarouselComponentModel getDummyCarouselModel(){
        placesToStay.setCatalogVersion(model);
        placesToStay.setUid("places_to_stay");
        placesToStay.setVisibleItems(Integer.valueOf(3));
        
        return placesToStay;
    }

    private PlacesToStayWrapper getDummywrapper(){
        LocationData location1= new LocationData();
        location1.setCode("001");
        location1.setName("majorca");
        
        LocationData location2= new LocationData();
        location2.setCode("002");
        location2.setName("Ibiza");
        
        List<LocationData> locationList= new ArrayList<LocationData>();
        locationList.add(location1);
        locationList.add(location2);
        wrapper.setTopLocationName("Spain");
        wrapper.setLocations(locationList);
        return wrapper;
        
    }
    @Test
    public void testPlacesToStayForLocations() {
        
        try {
            when(componentFacade.getComponent("places_to_stay")).thenReturn(placesToStay);
            when(componentFacade.getPlacesToStayCarouselData("ESP","OVERVIEW","","places_to_stay")).thenReturn(wrapper);
            assertThat(wrapper.getTopLocationName(),is("Spain"));
            assertThat(wrapper.getLocations().get(0).getCode(),is("001")); 
        } catch (NoSuchComponentException e) {
            LOG.error("Component Not Found");
        }
       
    }

}
