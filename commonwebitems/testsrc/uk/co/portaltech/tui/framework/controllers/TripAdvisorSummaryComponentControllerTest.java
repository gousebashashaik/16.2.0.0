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
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import com.enterprisedt.util.debug.Logger;

import uk.co.portaltech.tui.components.model.TripAdvisorSummaryComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.TripadvisorData;

/**
 * @author niranjani.r
 * 
 */
public class TripAdvisorSummaryComponentControllerTest {
    @Mock
    private AccommodationFacade accomodationFacade;

    @Mock
    private ComponentFacade componentFacade;

    @Mock
    private TypeService typeService;

    @Mock
    private CMSSiteService cmsSiteService;

    @Mock
    private ProductService productService;
    
    @Mock
    private CatalogVersionModel model;
    
    @Mock
    private CatalogModel catalogmodel;
        
    private ModelAndView mv = new ModelAndView();
    private TripAdvisorSummaryComponentModel tripAdvisor = new TripAdvisorSummaryComponentModel();
    private ProductModel product= new ProductModel();
    private final Logger LOG = Logger.getLogger(TripAdvisorSummaryComponentControllerTest.class);
    private AccommodationViewData accommdation = new AccommodationViewData();
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
        getDummyModel();
        getTripAdvisorModel();
        getProductModel();
        getaccommodationViewData();
    }
    
    private ModelAndView getDummyModel(){
        mv.setViewName("trip_mobile");
        return  mv;
        
    }
    
    private TripAdvisorSummaryComponentModel getTripAdvisorModel(){
         tripAdvisor.setUid("trip1");
         tripAdvisor.setCatalogVersion(model);
         return tripAdvisor;
        
    }
    
    private ProductModel getProductModel(){
        product.setCatalogVersion(model);
        product.setCode("product1");
        return product;
    }
    
    private AccommodationViewData getaccommodationViewData(){
        TripadvisorData tripData1= new  TripadvisorData();
        tripData1.setRatingBar("4");
        tripData1.setLogo("logo1");
        accommdation.setTripadvisorData(tripData1);
        accommdation.setCode("accomm1");
        return accommdation;
    }

    /**
     * Test method for
     * {@link uk.co.portaltech.tui.framework.controllers.TripAdvisorSummaryComponentController#viewtripAdvisorSummaryComponent()}
     * .
     */
    @Test
    public void testViewtripAdvisorSummaryComponentforRating() {
        try {
            when(componentFacade.getComponent("trip1")).thenReturn(tripAdvisor);
            when(productService.getProductForCode(model, "product1")).thenReturn(product);
            when( accomodationFacade.getTripAdvisorSummaryData(product.getCode(), Integer.valueOf(0))).thenReturn(accommdation);
            assertThat(accommdation.getTripadvisorData().getRatingBar(),is("4"));
            
        } catch (NoSuchComponentException e) {
            LOG.error("Component not found");
        }
       
    }

}
