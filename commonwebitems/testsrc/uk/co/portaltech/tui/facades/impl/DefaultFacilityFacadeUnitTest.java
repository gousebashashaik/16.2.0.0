/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.daos.FacilityDao;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.services.FacilityService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.FacilityConverter;
import uk.co.portaltech.tui.facades.FacilityFacade;
import uk.co.portaltech.tui.facades.impl.DefaultFacilityFacade;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * 
 * Class to test components implementing the {@link FacilityFacade} interface
 * 
 * @author l.furrer
 */
@UnitTest
public class DefaultFacilityFacadeUnitTest {
    
	@InjectMocks
    private DefaultFacilityFacade facilityFacade=new DefaultFacilityFacade();
    
	@Mock
    private FacilityService       facilityService;
    
	@Mock
    private FacilityConverter     facilityConverter;
    
	@Mock
    private CMSSiteService        siteService;
	
	@Mock
	private SessionService        sessionService;

	@Mock
    private FacilityDao           facilityDao;

    
    private final static String          FACILITY_CODE = "facility001";
    private final static String          FACILITY_NAME = "Facility 001";
    
    /**
     * Convenience method to create a dummy facility Model
     */
    private FacilityModel createDummyFacilityModel()
    {
        FacilityModel facilityModel = new FacilityModel();
        facilityModel.setCode(FACILITY_CODE);
        facilityModel.setName(FACILITY_NAME, Locale.ENGLISH);
        return facilityModel;
    }
    
    /**
     * Convenience method to create a dummy facility Data
     */
    private FacilityViewData createDummyFacilityData()
    {
        FacilityViewData facilityData = new FacilityViewData();
        facilityData.setName(FACILITY_NAME);
        return facilityData;
    }
    
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        facilityService = createMock(FacilityService.class);
        facilityConverter = createMock(FacilityConverter.class);
        
        facilityFacade.setFacilityService(facilityService);
        facilityFacade.setFacilityConverter(facilityConverter);
    }
    
    @Test
    public void testGetFacility()
    {
    	BrandDetails brandDetails=new BrandDetails();
    	List<String> brandPks=new ArrayList<String>(); 
    	brandDetails.setRelevantBrands(brandPks);
    	
        FacilityModel facilityModel = createDummyFacilityModel();
        CatalogVersionModel catalogVersionModel=new CatalogVersionModel();
        Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
        Mockito.when(sessionService.getAttribute(Mockito.anyString())).thenReturn(brandDetails);
        Mockito.when(facilityDao.findFacilitiesByCode(Mockito.anyString(),Mockito.any(CatalogVersionModel.class),Mockito.anyList())).thenReturn(facilityModel);
        
        FacilityViewData facilityData = facilityFacade.getFacility(FACILITY_CODE);
        
        assertEquals(null, facilityData);
        assertEquals(FACILITY_NAME, createDummyFacilityData().getName());
        
    }
    
}
