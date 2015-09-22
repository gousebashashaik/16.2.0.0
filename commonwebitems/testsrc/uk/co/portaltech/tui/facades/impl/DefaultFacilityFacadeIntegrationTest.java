/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static junit.framework.Assert.*;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Locale;


import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.tui.facades.FacilityFacade;
import uk.co.portaltech.tui.facades.impl.DefaultFacilityFacade;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * 
 * Class for integration tests of {@link FacilityFacade}
 * 
 * @author l.furrer
 *
 */
public class DefaultFacilityFacadeIntegrationTest extends ServicelayerTransactionalTest {
    
    private DefaultFacilityFacade facilityFacade;
    
    private ModelService modelService;
    
    private FacilityModel facilityModel;
    
    private final static String FACILITY_CODE = "facility001";
    private final static String FACILITY_NAME = "Facility 001";
    
    @Before
    public void setUp()
    {
        modelService = Registry.getApplicationContext().getBean("modelService", ModelService.class);
        facilityFacade = Registry.getApplicationContext().getBean("facilityFacade", DefaultFacilityFacade.class);
        
        facilityModel = new FacilityModel();
        facilityModel.setCode(FACILITY_CODE);
        facilityModel.setName(FACILITY_NAME, Locale.ENGLISH);
    }
    
    @Test
    public void testGetFacility()
    {
        FacilityViewData facilityData = facilityFacade.getFacility(FACILITY_CODE);
        assertNull("facilityData must be null at the forst call", facilityData);
        
        try {
           
            fail("UnknownIdentifierException expected");
        } 
        catch (UnknownIdentifierException e)
        {
            // Ok
        }
        
        modelService.save(facilityModel);
        
       
        assertNotNull(facilityData);
        assertEquals("The name of the facility must be " + FACILITY_NAME, FACILITY_NAME, facilityData.getName());
    }
    
}
