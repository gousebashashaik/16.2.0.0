/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.HotelInformationComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.HotelInformationViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * @author niranjani.r
 *
 */
@UnitTest
public class HotelInformationComponentControllerTest {

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
    HotelInformationComponentModel hotelInfo=new HotelInformationComponentModel();
    HotelInformationViewData hotel= new HotelInformationViewData();
    AccommodationViewData accom= new AccommodationViewData();
    
    
    private final TUILogUtils LOG = new TUILogUtils("HotelInformationComponentControllerTest");

    @Before
    public void setUp() throws Exception 
    {
        MockitoAnnotations.initMocks(this);
       
        CatalogVersionModel model = new CatalogVersionModel();
        CatalogModel catalog = new CatalogModel();
        catalog.setId("fc-catalog");
        model.setCatalog(catalog);
        model.setVersion("Online");
        model.setCatalog(catalogmodel);
        getHotelInfo();
        getAccommodationData();
        getHotelInfoData();
    }
    private HotelInformationViewData getHotelInfoData()
    {
    
        hotel.setAccomLocationUrl("/holiday/accommodation/location/Paphos/Constantinou-Bros-Hotel-Athena-Beach-009177");
        hotel.setFacilitiesUrl("/holiday/accommodation/facilities/Paphos/Constantinou-Bros-Hotel-Athena-Beach-009177");
        return hotel;
    }
    private AccommodationViewData getAccommodationData(){
       
        accom.setUrl("/holiday/accommodation/overview/Paphos/Constantinou-Bros-Hotel-Athena-Beach-009177");
        accom.setCode("009177");
        return accom;
        
    }
    private HotelInformationComponentModel getHotelInfo(){
      
        hotelInfo.setCatalogVersion(model);
        hotelInfo.setUid("hotel");
        return hotelInfo;
    }
    @Test
    public void testHotelInformationComponentForPageUrl(){
        try {
            when(componentFacade.getComponent(hotelInfo.getUid())).thenReturn(hotelInfo);
             when(accomodationFacade.getAccommodationByCode("009177")).thenReturn(accom);
             assertThat(accom.getUrl().replace("overview", "location"),is(hotel.getAccomLocationUrl()));
            
        } catch (NoSuchComponentException e) {
           LOG.error(" component not found");
        }
    }
}
