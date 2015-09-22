/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.tui.converters.AccommodationConverter;
import uk.co.portaltech.tui.facades.impl.DefaultAccomodationFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SustainableTourismComponentViewData;
/**
 * @author niranjani.r
 *
 */

public class DefaultAccommodationServiceTest {
	
	   @InjectMocks
	    private final DefaultAccomodationFacade Facade = new DefaultAccomodationFacade();
	    
	    @Mock
	    private AccommodationService accommodationService;
	    
	    @Mock
	    private CMSSiteService siteService;
	    
	    @Mock
	    private AccommodationConverter accommodationConverter;
	      
	    private static final String EMPTY_ACCOMMCODE = "";
	    
	    	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
	}
	
    private CatalogVersionModel getCatalogVersion(){
    	
    	final CatalogVersionModel catalog= new CatalogVersionModel();
         catalog.setVersion("online");
    	 return catalog;
    }
	private  AccommodationModel getEmptyAccommodationModel(){
		final AccommodationModel accommodationModel= new AccommodationModel();
        accommodationModel.setCode("");
        return accommodationModel;
	}
	
	private AccommodationViewData getEmptyAccommodationViewData() {
		 final Map<String, List<Object>> featureMap= new HashMap<String, List<Object>>();
		 final AccommodationViewData accommodationViewData= new AccommodationViewData();
         accommodationViewData.putFeatureCodesAndValues(featureMap);
         return accommodationViewData;
	}
	
	
	private SustainableTourismComponentViewData getEmptySustainableTourismComponentViewData(){
		
		final SustainableTourismComponentViewData view = new SustainableTourismComponentViewData();
		 view.setTitle("");
		 view.setDescription("");
		return view;
		
	}
	
	
	@Test
	public void testingForAccommodationTravelLifeAwardWithEmptyAccommodationData() {
		
		 final CatalogVersionModel catalog= getCatalogVersion();
		 final AccommodationModel accomm=getEmptyAccommodationModel();
		 final AccommodationViewData accommodationViewData=getEmptyAccommodationViewData();
		 final SustainableTourismComponentViewData view = getEmptySustainableTourismComponentViewData();
		// mocking all the  required services 
		 Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		 Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(EMPTY_ACCOMMCODE,catalog, null)).thenReturn(accomm);
		 Mockito.when(accommodationConverter.convert(accomm)).thenReturn(accommodationViewData);
	  	 final SustainableTourismComponentViewData viewData= Facade.getAccommodationTravelLifeAwardInfo("");
		 assertEquals(viewData.getTitle(),view.getTitle());
		 	
	}


}
