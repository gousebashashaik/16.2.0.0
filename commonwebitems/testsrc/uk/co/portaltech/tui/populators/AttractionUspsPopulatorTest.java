/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author sureshbabu.rn
 *
 */
public class AttractionUspsPopulatorTest {

	@InjectMocks
	AttractionUspsPopulator attractionUspsPopulator=new AttractionUspsPopulator();
	
	@Mock
	FeatureService featureService;
	
	AttractionModel sourceModel;
	AttractionViewData targetData;
	List featureValues;
	
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		sourceModel=new AttractionModel();
		targetData=new AttractionViewData();
		featureValues=new ArrayList();
				
		featureValues.add("feature1");
		featureValues.add("feature2");
		featureValues.add("feature3");
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.AttractionUspsPopulator#populate(uk.co.portaltech.travel.model.AttractionModel, uk.co.portaltech.tui.web.view.data.AttractionViewData)}.
	 */
	@Test
	public void testPopulate() {
		Mockito.when(featureService.getFeatureValues(Mockito.anyString(),Mockito.any(AttractionModel.class),Mockito.any(Date.class),Mockito.anyString())).thenReturn(featureValues);
	    assertNotNull(sourceModel);
	    assertNotNull(targetData);
	    
	    attractionUspsPopulator.populate(sourceModel, targetData);
	    
	    assertNotNull(targetData);
	    
	    List<String> result=targetData.getUsps();
	    
	    assertEquals(3, result.size());
	    assertEquals("feature1", result.get(0));
	    assertEquals("feature2", result.get(1));
	    assertEquals("feature3", result.get(2));
	
	}

}
