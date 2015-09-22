/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
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

import uk.co.portaltech.tui.components.model.AccommodationRoomsCarouselComponentModel;
import uk.co.portaltech.tui.components.model.FacilitiesHighlightsComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

import com.enterprisedt.util.debug.Logger;

/**
 * @author niranjani.r
 *
 */
public class FacilitiesHighlightsComponentControllerTest {

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
    
    private final Logger LOG = Logger.getLogger(FacilitiesHighlightsComponentControllerTest.class);
    
    AccommodationViewData accomm= new AccommodationViewData();
    FacilitiesHighlightsComponentModel facilityModel= new FacilitiesHighlightsComponentModel();
    
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
        getDummyFacilitiesHighlights();
        getDummyAccommodationViewData();
        
    }
  private AccommodationViewData  getDummyAccommodationViewData()
    {
        FacilityViewData facility1 = new FacilityViewData();
      
        facility1.setDescription("description1");
        
        FacilityViewData facility2 = new FacilityViewData();
       
        facility2.setDescription("description2");
        
        List<FacilityViewData> facilities= new ArrayList<FacilityViewData>();
        facilities.add(facility1);
        facilities.add(facility2);
        
        accomm.setFacilities(facilities);
        accomm.setCode("code1");
       
     return accomm;
    }
  
  private FacilitiesHighlightsComponentModel getDummyFacilitiesHighlights(){
      facilityModel.setCatalogVersion(model);
      facilityModel.setUid("facility123");
      return facilityModel;
  }

    /**
     * Test method for {@link uk.co.portaltech.tui.framework.controllers.FacilitiesHighlightsComponentController#viewFacilitiesHighlightsComponent()}.
     */
    @Test
    public void testViewFacilitiesHighlightsComponent() {
        try {
            when(componentFacade.getComponent(facilityModel.getUid())).thenReturn(facilityModel);
            when(accomodationFacade.getAccommodationFacilities(accomm.getCode())).thenReturn(accomm);
            assertThat(accomm.getFacilities().get(0).getDescription(),is("description1"));
        } catch (NoSuchComponentException e) {
            LOG.error("Component Not Found");
        }
    }

}
